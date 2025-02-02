package com.lingxi.lingxibackend.websocket.domain.vo.request.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Description: 文本消息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextMsgReq implements Serializable {
    // 消息内容
    @NotBlank(message = "内容不能为空")
    @Size(max = 1024, message = "消息内容过长，服务器扛不住啊，兄dei")
    private String content;

    // 回复的消息id,如果没有别传就好
    private Long replyMsgId;

    // 艾特的uid
    @Size(max = 10, message = "一次别艾特这么多人")
    private List<Long> atUidList;
}
