package com.lingxi.lingxibackend.websocket.event.listener;

import com.lingxi.lingxibackend.model.vo.UserVO;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.adapter.WSAdapter;
import com.lingxi.lingxibackend.websocket.domain.dto.PushMessageDTO;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.entity.Room;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomFriend;
import com.lingxi.lingxibackend.websocket.domain.enums.RoomTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.enums.WSPushTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatMessageResp;
import com.lingxi.lingxibackend.websocket.event.MessageSendEvent;
import com.lingxi.lingxibackend.websocket.service.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.Resource;
import java.util.*;

/**
 * 消息发送监听器
 */
@Slf4j
@Component
public class MessageSendListener {
    @Resource
    private WebSocketService webSocketService;
    @Resource
    private ChatService chatService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Resource
    private MessageService messageService;
    @Resource
    private RoomService roomService;
    @Resource
    private UserService userService;
    @Resource
    private RoomFriendService roomFriendService;
    @Resource
    private GroupMemberService groupMemberService;
    @Resource
    private ContactService contactService;
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Message message = messageService.getById(event.getMsgId());
        Room room = roomService.getById(message.getRoomId());
        List<Long> memberUidList = new ArrayList<>();
        //所有房间更新房间最新消息
        roomService.refreshActiveTime(room.getId(), message.getId(), message.getCreateTime());
        if (Objects.equals(room.getType(), RoomTypeEnum.GROUP.getType())) {//普通群聊推送所有群成员
            memberUidList = groupMemberService.getMemberUidList(room.getId());
        } else if (Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())) {//单聊对象
            //对单人推送
            RoomFriend roomFriend = roomFriendService.getByRoomId(room.getId());
            memberUidList = Arrays.asList(roomFriend.getUid1(), roomFriend.getUid2());
        }
        //更新所有群成员的会话时间
        contactService.refreshOrCreateActiveTime(room.getId(), memberUidList, message.getId(), message.getCreateTime());
        Set<Long> uidSet = new HashSet<>(Collections.singletonList(message.getFromUid()));
        Map<Long, UserVO> userVOMap = userService.getUserVOBatch(uidSet);
        //推送房间成员
        onMessage(new PushMessageDTO(memberUidList, WSAdapter.buildMsgSend(ChatMessageResp.objToResp(message,userVOMap))));
    }

    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void handlerMsg(@NotNull MessageSendEvent event) {
        Message message = messageService.getById(event.getMsgId());
        System.out.println(message);
    }

    public void onMessage(PushMessageDTO message) {
        WSPushTypeEnum wsPushTypeEnum = WSPushTypeEnum.of(message.getPushType());
        switch (wsPushTypeEnum) {
            case USER:
                message.getUidList().forEach(uid -> {
                    webSocketService.sendToUid(message.getWsBaseMsg(), uid);
                });
                break;
            case ALL:
                webSocketService.sendToAllOnline(message.getWsBaseMsg(), null);
                break;
        }
    }

}
