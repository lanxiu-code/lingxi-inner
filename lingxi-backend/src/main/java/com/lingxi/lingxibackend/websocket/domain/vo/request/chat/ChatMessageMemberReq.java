package com.lingxi.lingxibackend.websocket.domain.vo.request.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description: 消息列表请求
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageMemberReq implements Serializable {
    // 会话id
    @NotNull
    private Long roomId;
}
