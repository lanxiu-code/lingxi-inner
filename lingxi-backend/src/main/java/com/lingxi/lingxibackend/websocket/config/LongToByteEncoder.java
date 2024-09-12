package com.lingxi.lingxibackend.websocket.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class LongToByteEncoder extends ChannelDuplexHandler {

    // 编码器
    public void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) {
        out.writeLong(msg);
    }
}
