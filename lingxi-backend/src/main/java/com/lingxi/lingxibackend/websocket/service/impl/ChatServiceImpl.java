package com.lingxi.lingxibackend.websocket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.vo.UserVO;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.websocket.domain.entity.Contact;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageMemberReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessagePageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatMessageResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;
import com.lingxi.lingxibackend.websocket.event.MessageSendEvent;
import com.lingxi.lingxibackend.websocket.service.ChatService;
import com.lingxi.lingxibackend.websocket.service.ContactService;
import com.lingxi.lingxibackend.websocket.service.MessageService;
import com.lingxi.lingxibackend.websocket.strategy.AbstractMsgHandler;
import com.lingxi.lingxibackend.websocket.strategy.MsgHandlerFactory;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private UserService userService;
    @Resource
    private ContactService contactService;
    @Resource
    private MessageService messageService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, @Nullable Long receiveUid) {
        //用最后一条消息id，来限制被踢出的人能看见的最大一条消息
        Long lastMsgId = getLastMsgId(request.getRoomId(), receiveUid);
        CursorPageBaseResp<Message> cursorPage = messageService.getCursorPage(request.getRoomId(), request, lastMsgId);
        if (cursorPage.isEmpty()){
            return CursorPageBaseResp.empty();
        }
        Set<Long> uidSet = cursorPage.getList()
                .stream().map(Message::getFromUid).collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.getUserVOBatch(uidSet);
        List<ChatMessageResp> chatMessageResps = cursorPage.getList().stream()
                .map(msg->ChatMessageResp.objToResp(msg, userVOMap))
                .collect(Collectors.toList());
        return CursorPageBaseResp.init(cursorPage,chatMessageResps);
    }

    @Override
    public Long sendMsg(ChatMessageReq chatMessageReq, Long id) {
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(chatMessageReq.getMsgType());
        // 保存消息到数据库
        Long msgId = msgHandler.checkAndSaveMsg(chatMessageReq, id);
        //发布消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
        return msgId;
    }

    @Override
    public ChatMessageResp getMsgResp(Long msgId, Long userId) {
        Message msg = messageService.getById(msgId);
        Set<Long> uidSet = new HashSet<>(Collections.singletonList(msg.getFromUid()));
        Map<Long, UserVO> userVOMap = userService.getUserVOBatch(uidSet);
        return ChatMessageResp.objToResp(msg,userVOMap);
    }

    @Override
    public void msgRead(Long uid, ChatMessageMemberReq memberReq) {
        Contact contact = contactService
                .lambdaQuery()
                .eq(Contact::getUid,uid)
                .eq(Contact::getRoomId,memberReq.getRoomId()).one();
        if (Objects.nonNull(contact)) {
            Contact update = new Contact();
            update.setId(contact.getId());
            update.setReadTime(new Date());
            contactService.updateById(update);
        } else {
            Contact insert = new Contact();
            insert.setUid(uid);
            insert.setRoomId(memberReq.getRoomId());
            insert.setReadTime(new Date());
            contactService.save(insert);
        }
    }

    private Long getLastMsgId(Long roomId, Long receiveUid) {
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        wrapper
                .eq("roomId", roomId)
                .eq("uid", receiveUid);
        Contact contact = contactService.getOne(wrapper);
        if (Objects.isNull(contact)) {
            return null;
        }
        return contact.getLastMsgId();
    }
}
