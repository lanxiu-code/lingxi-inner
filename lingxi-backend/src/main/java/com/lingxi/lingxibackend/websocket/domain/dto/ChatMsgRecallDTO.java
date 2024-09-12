package com.lingxi.lingxibackend.websocket.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:消息撤回的推送类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgRecallDTO implements Serializable {
    private Long msgId;
    private Long roomId;
    //撤回的用户
    private Long recallUid;
}
