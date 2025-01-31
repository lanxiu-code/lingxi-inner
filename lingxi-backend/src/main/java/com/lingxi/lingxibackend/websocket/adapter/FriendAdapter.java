package com.lingxi.lingxibackend.websocket.adapter;



import com.lingxi.lingxibackend.websocket.domain.entity.UserApply;
import com.lingxi.lingxibackend.websocket.domain.entity.UserFriend;
import com.lingxi.lingxibackend.websocket.domain.vo.request.friend.FriendApplyReq;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Description: 好友适配器
 */
public class FriendAdapter {
    public static UserApply buildFriendApply(Long uid, FriendApplyReq request) {
        UserApply userApplyNew = new UserApply();
        userApplyNew.setUid(uid);
        userApplyNew.setMsg(request.getMsg());
        userApplyNew.setTargetId(request.getTargetUid());
        return userApplyNew;
    }

}
