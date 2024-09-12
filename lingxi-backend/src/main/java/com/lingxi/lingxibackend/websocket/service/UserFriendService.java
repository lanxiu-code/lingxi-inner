package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.entity.UserFriend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingxi.lingxibackend.websocket.domain.vo.request.friend.FriendApplyReq;

/**
* @author 蓝朽
* @description 针对表【user_friend(用户联系人表)】的数据库操作Service
* @createDate 2024-09-03 21:54:58
*/
public interface UserFriendService extends IService<UserFriend> {
    /*
    * 关注好友
    * */
    void apply(Long id, FriendApplyReq friendApplyReq);
}
