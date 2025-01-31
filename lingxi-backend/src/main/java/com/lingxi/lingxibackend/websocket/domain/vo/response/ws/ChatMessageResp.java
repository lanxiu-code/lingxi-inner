package com.lingxi.lingxibackend.websocket.domain.vo.response.ws;
import cn.hutool.core.bean.BeanUtil;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.vo.UserVO;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.strategy.AbstractMsgHandler;
import com.lingxi.lingxibackend.websocket.strategy.MsgHandlerFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description: 消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResp implements Serializable {
    /*
    * 发送者信息
    * */
    private UserVO fromUser;
    /*
     * 消息详情
     * */
    private MessageInfo message;

    @Data
    public static class MessageInfo {
        // 消息id
        private Long id;
        // 房间id
        private Long roomId;
        // 消息发送时间
        private Date sendTime;
        // 消息类型 1正常文本 2.撤回消息
        private Integer type;
        // 消息内容不同的消息类型
        private Object body;
    }

    public static ChatMessageResp objToResp(Message message, Map<Long, UserVO> userVOMap) {
        ChatMessageResp chatMessageResp = new ChatMessageResp();
        ChatMessageResp.MessageInfo messageVO = new ChatMessageResp.MessageInfo();
        BeanUtil.copyProperties(message, messageVO);
        messageVO.setSendTime(message.getCreateTime());
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(message.getType());
        if (Objects.nonNull(msgHandler)) {
            messageVO.setBody(msgHandler.showMsg(message));
        }
        chatMessageResp.setFromUser(userVOMap.get(message.getFromUid()));
        chatMessageResp.setMessage(messageVO);
        return chatMessageResp;
    }
}
