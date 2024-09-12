package com.lingxi.lingxibackend.websocket.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageStatusEnum;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.msg.TextMsgReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatMessageResp;
import com.lingxi.lingxibackend.websocket.strategy.AbstractMsgHandler;
import com.lingxi.lingxibackend.websocket.strategy.MsgHandlerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 消息适配器
 */
public class MessageAdapter {
    public static final int CAN_CALLBACK_GAP_COUNT = 100;

    public static Message buildMsgSave(ChatMessageReq request, Long uid) {
        return Message.builder()
                .fromUid(uid)
                .roomId(request.getRoomId())
                .type(request.getMsgType())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();

    }

    private static ChatMessageResp.MessageInfo buildMessage(Message message, Long receiveUid) {
        ChatMessageResp.MessageInfo messageVO = new ChatMessageResp.MessageInfo();
        BeanUtil.copyProperties(message, messageVO);
        messageVO.setSendTime(message.getCreateTime());
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(message.getType());
        if (Objects.nonNull(msgHandler)) {
            messageVO.setBody(msgHandler.showMsg(message));
        }
        return messageVO;
    }



    public static ChatMessageReq buildAgreeMsg(Long roomId) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setMsgType(MessageTypeEnum.TEXT.getType());
        TextMsgReq textMsgReq = new TextMsgReq();
        textMsgReq.setContent("我们已经成为好友了，开始聊天吧");
        chatMessageReq.setBody(textMsgReq);
        return chatMessageReq;
    }
}
