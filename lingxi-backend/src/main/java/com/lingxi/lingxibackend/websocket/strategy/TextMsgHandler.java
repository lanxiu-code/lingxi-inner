package com.lingxi.lingxibackend.websocket.strategy;

import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.entity.MessageExtra;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.msg.TextMsgReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.msg.TextMsgResp;
import com.lingxi.lingxibackend.websocket.service.MessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgReq>{
    @Resource
    private MessageService messageService;
    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void saveMsg(Message message, TextMsgReq body) {
//        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(message.getId());
        update.setContent(body.getContent());
//        update.setExtra(extra);
        messageService.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        TextMsgResp resp = new TextMsgResp();
        resp.setContent(msg.getContent());
        return resp;
    }

    @Override
    public String showContactMsg(Message msg) {
        return msg.getContent();
    }
}
