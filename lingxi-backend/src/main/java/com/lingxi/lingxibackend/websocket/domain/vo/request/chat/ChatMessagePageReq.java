package com.lingxi.lingxibackend.websocket.domain.vo.request.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Description: 消息列表请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePageReq extends CursorPageBaseReq {
    //会话id
    @NotNull
    private Long roomId;
}
