package com.lingxi.lingxibackend.websocket.domain.vo.response.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Description: 文本消息返回体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextMsgResp {
    // 消息内容
    private String content;

    // 父消息，如果没有父消息，返回的是null
    private TextMsgResp.ReplyMsg reply;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReplyMsg {
        // 消息id
        private Long id;
        // 用户uid
        private Long uid;
        // 用户名称
        private String username;
        // 消息类型 1正常文本 2.撤回消息
        private Integer type;
        // 消息内容不同的消息类型，见父消息内容体
        private Object body;
        // 是否可消息跳转 0否 1是
        private Integer canCallback;
        // 跳转间隔的消息条数
        private Integer gapCount;
    }
}
