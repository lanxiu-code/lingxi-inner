package com.lingxi.lingxibackend.websocket.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.websocket.domain.entity.Message;
import com.lingxi.lingxibackend.websocket.domain.entity.Room;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageStatusEnum;
import com.lingxi.lingxibackend.websocket.domain.enums.MessageTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageReq;
import com.lingxi.lingxibackend.websocket.service.MessageService;
import com.lingxi.lingxibackend.websocket.service.RoomService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;

/**
 * Description: 消息处理器抽象类
 */
public abstract class AbstractMsgHandler<Req> {
    @Resource
    private MessageService messageService;
    @Resource
    private RoomService roomService;
    private Class<Req> bodyClass;
    /**
     * 消息类型
     */
    abstract MessageTypeEnum getMsgTypeEnum();
    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMsgTypeEnum().getType(), this);
    }

    private Req toBean(Object body) {
        if (bodyClass.isAssignableFrom(body.getClass())) {
            return (Req) body;
        }
        return BeanUtil.toBean(body, bodyClass);
    }

    protected abstract void saveMsg(Message message, Req body);
    /**
     * 展示消息
     */
    public abstract Object showMsg(Message msg);

    public Long checkAndSaveMsg(ChatMessageReq chatMessageReq, Long uid) {
        Req body = this.toBean(chatMessageReq.getBody());
        if(ObjectUtil.isNull(body)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"消息体不能为空");
        }
        Message insert = Message.builder()
                .fromUid(uid)
                .roomId(chatMessageReq.getRoomId())
                .type(chatMessageReq.getMsgType())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
        //统一保存
        messageService.save(insert);
        //子类扩展保存
        saveMsg(insert, body);
        return insert.getId();
    }
    /**
     * 会话列表——展示的消息
     */
    public abstract String showContactMsg(Message msg);
}
