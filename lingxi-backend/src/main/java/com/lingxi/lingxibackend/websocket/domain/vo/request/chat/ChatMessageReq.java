package com.lingxi.lingxibackend.websocket.domain.vo.request.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 聊天信息点播
 * Description: 消息发送请求体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReq implements Serializable {
    // 房间id
    @NotNull
    private Long roomId;
    // 消息类型
    @NotNull
    private Integer msgType;

    /**
     * 消息内容，类型不同传值不同
     */
    @NotNull
    private Object body;

}
