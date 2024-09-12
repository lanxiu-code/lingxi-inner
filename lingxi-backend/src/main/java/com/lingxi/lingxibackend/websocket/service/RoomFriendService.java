package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.entity.RoomFriend;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 蓝朽
* @description 针对表【room_friend(单聊房间表)】的数据库操作Service
* @createDate 2024-09-03 10:32:05
*/
public interface RoomFriendService extends IService<RoomFriend> {
    RoomFriend getByRoomId(Long roomID);

    RoomFriend getByKey(String roomKey);

    void restoreRoom(Long id);
}
