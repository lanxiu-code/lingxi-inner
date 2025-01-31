package com.lingxi.lingxibackend.websocket.domain.vo.request.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomRequest implements Serializable {
    private Long userId;
    private Long targetUserId;
}
