package com.lingxi.lingxibackend.websocket.service;

import com.lingxi.lingxibackend.websocket.domain.vo.response.ws.WSBaseResp;
import io.netty.channel.Channel;

import javax.servlet.http.HttpServletRequest;

/*
* WS服务接口
* */
public interface WebSocketService {
    /**
     * 处理所有ws连接的事件
     *
     * @param channel
     */
    void connect(Channel channel);

    /**
     * 处理ws断开连接的事件
     *
     * @param channel
     */
    void removed(Channel channel);

    /**
     * 主动认证登录
     *
     * @param channel
     */
    void authorize(Channel channel, String token);
    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     * @param skipUid    需要跳过的人
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid);

    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp);

    void sendToUid(WSBaseResp<?> wsBaseResp, Long uid);
}
