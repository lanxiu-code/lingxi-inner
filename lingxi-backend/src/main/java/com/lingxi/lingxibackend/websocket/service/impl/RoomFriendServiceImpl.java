package com.lingxi.lingxibackend.websocket.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomFriend;
import com.lingxi.lingxibackend.websocket.domain.enums.NormalOrNoEnum;
import com.lingxi.lingxibackend.websocket.service.RoomFriendService;
import com.lingxi.lingxibackend.websocket.mapper.RoomFriendMapper;
import org.springframework.stereotype.Service;

/**
* @author 蓝朽
* @description 针对表【room_friend(单聊房间表)】的数据库操作Service实现
* @createDate 2024-09-03 10:32:05
*/
@Service
public class RoomFriendServiceImpl extends ServiceImpl<RoomFriendMapper, RoomFriend>
    implements RoomFriendService{

    @Override
    public RoomFriend getByRoomId(Long roomID) {
        return lambdaQuery()
                .eq(RoomFriend::getRoomId, roomID)
                .one();
    }

    @Override
    public RoomFriend getByKey(String roomKey) {
        return lambdaQuery().eq(RoomFriend::getRoomKey, roomKey).one();
    }

    @Override
    public void restoreRoom(Long id) {
        lambdaUpdate()
                .eq(RoomFriend::getId, id)
                .set(RoomFriend::getStatus, NormalOrNoEnum.NORMAL.getStatus())
                .update();
    }
}




