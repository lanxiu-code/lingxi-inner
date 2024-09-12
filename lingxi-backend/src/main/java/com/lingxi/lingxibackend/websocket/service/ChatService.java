package com.lingxi.lingxibackend.websocket.service;


import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageMemberReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessagePageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.request.chat.ChatMessageReq;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.ChatMessageResp;
import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.CursorPageBaseResp;

import javax.annotation.Nullable;

/**
 * Description: 消息处理类
 */
public interface ChatService {
    /**
     * 获取消息列表
     *
     * @param request
     * @return
     */
    CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, @Nullable Long receiveUid);
    /*
    * 发送消息
    * */
    Long sendMsg(ChatMessageReq chatMessageReq, Long id);

    /*
    * 获取消息结果
    * */
    ChatMessageResp getMsgResp(Long msgId, Long userId);

    void msgRead(Long uid, ChatMessageMemberReq memberReq);
}
