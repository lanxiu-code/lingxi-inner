package com.lingxi.lingxibackend.websocket.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.websocket.domain.dto.ChatMsgRecallDTO;
import com.lingxi.lingxibackend.websocket.domain.enums.ChatActiveStatusEnum;
import com.lingxi.lingxibackend.websocket.domain.enums.WSRespTypeEnum;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.*;
import com.lingxi.lingxibackend.websocket.service.ChatService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Description: ws消息适配器
 */
@Component
public class WSAdapter {
    @Autowired
    private ChatService chatService;

    public static WSBaseResp<?> buildMsgRecall(ChatMsgRecallDTO recallDTO) {
        WSBaseResp<WSMsgRecall> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.RECALL.getType());
        WSMsgRecall recall = new WSMsgRecall();
        BeanUtils.copyProperties(recallDTO, recall);
        wsBaseResp.setData(recall);
        return wsBaseResp;
    }

    public static WSBaseResp<ChatMessageResp> buildMsgSend(ChatMessageResp msgResp) {
        WSBaseResp<ChatMessageResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.MESSAGE.getType());
        wsBaseResp.setData(msgResp);
        return wsBaseResp;
    }

    public static WSBaseResp<WSFriendApply> buildApplySend(WSFriendApply resp) {
        WSBaseResp<WSFriendApply> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.APPLY.getType());
        wsBaseResp.setData(resp);
        return wsBaseResp;
    }

    public static WSBaseResp<WSLoginSuccess> buildLoginSuccessResp(User user, String token) {
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .avatarUrl(user.getAvatarUrl())
                .username(user.getUsername())
                .token(token)
                .uid(user.getId())
                .userRole(user.getUserRole())
                .build();
        wsBaseResp.setData(wsLoginSuccess);
        return wsBaseResp;
    }
    /*
    * token失效
    * */
    public static WSBaseResp<WSLoginSuccess> buildInvalidateTokenResp() {
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }
}
