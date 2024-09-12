package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.entity.Room;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomFriend;
import com.lingxi.lingxibackend.websocket.domain.entity.RoomGroup;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.CursorPageBaseReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatRoomResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;

import java.util.Date;
import java.util.List;

/**
* @author 蓝朽
* @description 针对表【room(房间表)】的数据库操作Service
* @createDate 2024-09-03 10:32:05
*/
public interface RoomService extends IService<Room> {
    /**
     * 创建一个单聊房间
     */
    RoomFriend createFriendRoom(List<Long> uidList);

    RoomFriend getFriendRoom(Long uid1, Long uid2);

    /**
     * 禁用一个单聊房间
     */
    void disableFriendRoom(List<Long> uidList);

    /**
     * 创建一个群聊房间
     */
    RoomGroup createGroupRoom(Long uid);
    /**
     * 会话列表
     */
    CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq pageBaseReq, Long id);

    ChatRoomResp getContactDetail(Long id, long id1);

    void refreshActiveTime(Long roomId, Long messageId, Date createTime);
}
