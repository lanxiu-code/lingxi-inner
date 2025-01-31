package com.lingxi.lingxibackend.websocket.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.exception.ThrowUtils;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.adapter.ChatAdapter;
import com.lingxi.lingxibackend.websocket.domain.dto.RoomBaseInfo;
import com.lingxi.lingxibackend.websocket.domain.entity.*;
import com.lingxi.lingxibackend.websocket.domain.enums.NormalOrNoEnum;
import com.lingxi.lingxibackend.websocket.domain.enums.RoomTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatRoomResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.service.*;
import com.lingxi.lingxibackend.websocket.mapper.RoomMapper;
import com.lingxi.lingxibackend.websocket.strategy.AbstractMsgHandler;
import com.lingxi.lingxibackend.websocket.strategy.MsgHandlerFactory;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 蓝朽
* @description 针对表【room(房间表)】的数据库操作Service实现
* @createDate 2024-09-03 10:32:05
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService{
    @Resource
    private RoomFriendService roomFriendService;
    @Resource
    private ContactService contactService;
    @Resource
    private UserService userService;
    @Resource
    private RoomGroupService roomGroupService;
    @Resource
    private MessageService messageService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomFriend createFriendRoom(List<Long> uidList) {
        ThrowUtils.throwIf(uidList.size()!=2, ErrorCode.OPERATION_ERROR,"房间创建失败，好友数量不对");
        String roomKey = ChatAdapter.generateRoomKey(uidList);
        RoomFriend roomFriend = roomFriendService.getByKey(roomKey);
        if (Objects.nonNull(roomFriend)) { //如果存在房间就恢复，适用于恢复好友场景
            restoreRoomIfNeed(roomFriend);
        } else {//新建房间
            Room room = createRoom(RoomTypeEnum.FRIEND);
            roomFriend = createFriendRoom(room.getId(), uidList);
        }
        return roomFriend;
    }

    @Override
    public RoomFriend getFriendRoom(Long uid1, Long uid2) {
        return null;
    }

    @Override
    public void disableFriendRoom(List<Long> uidList) {

    }

    @Override
    public RoomGroup createGroupRoom(Long uid) {
        return null;
    }

    @Override
    public CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq pageBaseReq, Long uid) {
        // 查出用户要展示的会话列表
        CursorPageBaseResp<Long> page = null;
        if (Objects.nonNull(uid)) {
            Double hotStart = null;
            // 用户基础会话
            CursorPageBaseResp<Contact> contactPage = contactService.getContactPage(uid, pageBaseReq);
            List<Long> baseRoomIds = contactPage.getList().stream().map(Contact::getRoomId).collect(Collectors.toList());
            if (!contactPage.getIsLast()) {
                hotStart = getCursorOrNull(contactPage.getCursor());
            }
            // 基础会话和热门房间合并
            page = CursorPageBaseResp.init(contactPage, baseRoomIds);
        }
        // 最后组装会话信息（名称，头像，未读数等）
        List<ChatRoomResp> result = buildContactResp(uid, page.getList());
        return CursorPageBaseResp.init(page, result);
    }

    @Override
    public ChatRoomResp getContactDetail(Long userId, long roomId) {
        Room room = getById(roomId);
        if(ObjectUtil.isNull(room)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"房间号有误");
        }
        return buildContactResp(userId, Collections.singletonList(roomId)).get(0);
    }

    @Override
    public void refreshActiveTime(Long roomId, Long messageId, Date createTime) {
        lambdaUpdate()
                .eq(Room::getId, roomId)
                .set(Room::getLastMsgId, messageId)
                .set(Room::getActiveTime, createTime)
                .update();
    }

    private RoomFriend createFriendRoom(Long roomId, List<Long> uidList) {
        RoomFriend insert = ChatAdapter.buildFriendRoom(roomId, uidList);
        roomFriendService.save(insert);
        return insert;
    }

    private Room createRoom(RoomTypeEnum typeEnum) {
        Room insert = ChatAdapter.buildRoom(typeEnum);
        save(insert);
        return insert;
    }
    private void restoreRoomIfNeed(RoomFriend room) {
        if (Objects.equals(room.getStatus(), NormalOrNoEnum.NOT_NORMAL.getStatus())) {
            roomFriendService.restoreRoom(room.getId());
        }
    }
    private Double getCursorOrNull(String cursor) {
        return Optional.ofNullable(cursor).map(Double::parseDouble).orElse(null);
    }
    @NotNull
    private List<ChatRoomResp> buildContactResp(Long uid, List<Long> roomIds) {
        // 表情和头像
        Map<Long, RoomBaseInfo> roomBaseInfoMap = getRoomBaseInfoMap(roomIds, uid);
        // 最后一条消息
        List<Long> msgIds = roomBaseInfoMap.values()
                .stream().map(RoomBaseInfo::getLastMsgId).collect(Collectors.toList());
        List<Message> messages = CollectionUtil.isEmpty(msgIds) ? new ArrayList<>() : messageService.listByIds(msgIds);
        Map<Long, Message> msgMap = messages.stream().collect(Collectors.toMap(Message::getId, Function.identity()));
        Map<Long, User> lastMsgUidMap = userService.lambdaQuery()
                .in(User::getId,messages.stream().map(Message::getFromUid).collect(Collectors.toList()))
                .list().stream().collect(Collectors.toMap(User::getId,Function.identity()));
        List<Contact> contactList = contactService.lambdaQuery()
                .in(Contact::getRoomId, roomIds)
                .eq(Contact::getUid, uid).list();
        // 消息未读数
        Map<Long, Integer> unReadCountMap = contactList.parallelStream()
                .map(contact -> Pair.of(contact.getRoomId(),messageService.getUnReadCount(contact.getRoomId(), contact.getReadTime())))
                .collect(Collectors.toMap(Pair::getKey,Pair::getValue));
        return roomBaseInfoMap.values().stream().map(room -> {
                    ChatRoomResp resp = new ChatRoomResp();
                    RoomBaseInfo roomBaseInfo = roomBaseInfoMap.get(room.getRoomId());
                    resp.setAvatar(roomBaseInfo.getAvatar());
                    resp.setRoomId(room.getRoomId());
                    resp.setActiveTime(room.getActiveTime());
                    resp.setHot_Flag(roomBaseInfo.getHotFlag());
                    resp.setType(roomBaseInfo.getType());
                    resp.setName(roomBaseInfo.getName());
                    Message message = msgMap.get(room.getLastMsgId());
                    if (Objects.nonNull(message)) {
                        AbstractMsgHandler strategyNoNull = MsgHandlerFactory.getStrategyNoNull(message.getType());
                        resp.setText(lastMsgUidMap.get(message.getFromUid()).getUsername() + ":" + strategyNoNull.showContactMsg(message));
                    }
                    resp.setUnreadCount(unReadCountMap.getOrDefault(room.getRoomId(), 0));
                    return resp;
                }).sorted(Comparator.comparing(ChatRoomResp::getActiveTime).reversed())
                .collect(Collectors.toList());
    }
    private Map<Long, RoomBaseInfo> getRoomBaseInfoMap(List<Long> roomIds, Long uid) {
        // 去重
        List<Long> reqIds = roomIds.stream().distinct().collect(Collectors.toList());
        Map<Long, Room> roomMap = lambdaQuery()
                .in(Room::getId, reqIds)
                .list()
                .stream().collect(Collectors.toMap(Room::getId,Function.identity()));
        // 房间根据好友和群组类型分组
        Map<Integer, List<Long>> groupRoomIdMap = roomMap.values().stream().collect(Collectors.groupingBy(Room::getType,
                Collectors.mapping(Room::getId, Collectors.toList())));
        // 获取群组信息
        List<Long> groupRoomId = groupRoomIdMap.get(RoomTypeEnum.GROUP.getType());
        // 获取好友信息
        List<Long> friendRoomId = groupRoomIdMap.get(RoomTypeEnum.FRIEND.getType());
        Map<Long, User> friendRoomMap = getFriendRoomMap(friendRoomId, uid);
        return roomMap.values().stream().map(room -> {
            RoomBaseInfo roomBaseInfo = new RoomBaseInfo();
            roomBaseInfo.setRoomId(room.getId());
            roomBaseInfo.setType(room.getType());
            roomBaseInfo.setHotFlag(room.getHotFlag());
            roomBaseInfo.setLastMsgId(room.getLastMsgId());
            roomBaseInfo.setActiveTime(room.getActiveTime());
            if (RoomTypeEnum.of(room.getType()) == RoomTypeEnum.GROUP) {
                Map<Long, RoomGroup> roomInfoBatch = roomGroupService.lambdaQuery()
                        .in(RoomGroup::getId, groupRoomId)
                        .list().stream().collect(Collectors.toMap(RoomGroup::getId,Function.identity()));
                RoomGroup roomGroup = roomInfoBatch.get(room.getId());
                roomBaseInfo.setName(roomGroup.getName());
                roomBaseInfo.setAvatar(roomGroup.getAvatar());
            } else if (RoomTypeEnum.of(room.getType()) == RoomTypeEnum.FRIEND) {
                User user = friendRoomMap.get(room.getId());
                roomBaseInfo.setName(user.getUsername());
                roomBaseInfo.setAvatar(user.getAvatarUrl());
            }
            return roomBaseInfo;
        }).collect(Collectors.toMap(RoomBaseInfo::getRoomId, Function.identity()));
    }
    private Map<Long, User> getFriendRoomMap(List<Long> roomIds, Long uid) {
        if (CollectionUtil.isEmpty(roomIds)) {
            return new HashMap<>();
        }
        Map<Long, List<RoomFriend>> roomFriendMap = roomFriendService
                .lambdaQuery().in(RoomFriend::getRoomId, roomIds)
                .list().stream().collect(Collectors.groupingBy(RoomFriend::getId));
        List<RoomFriend> roomMapValues = roomFriendMap.values().stream().map(list ->list.get(0)).collect(Collectors.toList());
        Set<Long> friendUidSet = ChatAdapter.getFriendUidSet(roomMapValues, uid);

        Map<Long, List<User>> userBatch = userService.lambdaQuery()
                .in(User::getId, friendUidSet).list()
                .stream().collect(Collectors.groupingBy(User::getId));
        return roomMapValues
                .stream()
                .collect(Collectors.toMap(RoomFriend::getRoomId, roomFriend -> {
                    Long friendUid = ChatAdapter.getFriendUid(roomFriend, uid);
                    return userBatch.get(friendUid).get(0);
                }));
    }
}




