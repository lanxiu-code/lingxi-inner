package com.lingxi.lingxibackend.websocket.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.exception.ThrowUtils;
import com.lingxi.lingxibackend.websocket.adapter.FriendAdapter;
import com.lingxi.lingxibackend.websocket.adapter.MessageAdapter;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomFriend;
import com.lingxi.lingxibackend.websocket.domain.entity.UserApply;
import com.lingxi.lingxibackend.websocket.domain.entity.UserFriend;
import com.lingxi.lingxibackend.websocket.domain.vo.request.friend.FriendApplyReq;
import com.lingxi.lingxibackend.websocket.service.ChatService;
import com.lingxi.lingxibackend.websocket.service.RoomService;
import com.lingxi.lingxibackend.websocket.service.UserFriendService;
import com.lingxi.lingxibackend.websocket.mapper.UserFriendMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
* @author 蓝朽
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service实现
* @createDate 2024-09-03 21:54:58
*/
@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend>
    implements UserFriendService{
    @Resource
    private UserFriendMapper userFriendMapper;
    @Resource
    private RoomService roomService;
    @Resource
    private ChatService chatService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(Long uid, FriendApplyReq friendApplyReq) {
        //是否有好友关系
        UserFriend friend = lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, friendApplyReq.getTargetUid())
                .one();
        ThrowUtils.throwIf(ObjectUtil.isNotNull(friend), ErrorCode.PARAMS_ERROR,"你们已经是好友了");
        //申请入库
        friend = new UserFriend();
        friend.setUid(uid);
        friend.setFriendUid(friendApplyReq.getTargetUid());
        save(friend);
        //创建一个聊天房间
        RoomFriend roomFriend = roomService.createFriendRoom(Arrays.asList(uid, friendApplyReq.getTargetUid()));
        //发送一条同意消息。。我们已经是好友了，开始聊天吧
        chatService.sendMsg(MessageAdapter.buildAgreeMsg(roomFriend.getRoomId()), uid);
    }

    @Override
    public List<UserFriend> getFriendList(Long userId) {
        return lambdaQuery()
                .eq(UserFriend::getUid,userId)
                .list();
    }
}




