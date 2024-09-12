package com.lingxi.lingxibackend.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.lingxi.lingxibackend.websocket.domain.vo.request.ws.WSBaseReq;
import com.lingxi.lingxibackend.websocket.service.WebSocketService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import com.lingxi.lingxibackend.websocket.domain.enums.WSReqTypeEnum;

import static io.netty.handler.codec.stomp.StompHeaders.HEART_BEAT;

@Slf4j
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;
    // 读取客户端发送的请求报文
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
        WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
        switch (wsReqTypeEnum) {
            case HEARTBEAT:
                log.info("收到客户端消息:前端心跳检测");
                break;
            default:
                log.info("收到客户端消息:未知类型");
        }
    }
    // 当web客户端连接后，触发该方法 ,第一个触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.webSocketService = getService();
    }

    private WebSocketService getService() {
        return SpringUtil.getBean(WebSocketService.class);
    }
    // 客户端离线
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
    }

    private void userOffLine(ChannelHandlerContext ctx) {
        webSocketService.removed(ctx.channel());
        ctx.channel().close();
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("触发 channelInactive 掉线![{}]", ctx.channel().id());
    }
    /**
     * 心跳检查，第三个触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE){
                System.out.println("读空闲");
                // todo 关闭用户的连接
                userOffLine(ctx);
            }
        }else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            // 握手成功，获取WebSocket相关的头信息
            webSocketService.connect(ctx.channel());
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (StrUtil.isNotBlank(token)) {
                webSocketService.authorize(ctx.channel(), token);
            }
        }
        super.userEventTriggered(ctx, evt);
    }
    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("异常发生，异常消息 ={}", cause);
        ctx.channel().close();
    }
    /*
    * 第四个触发
    * */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
}
