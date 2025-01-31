package com.lingxi.lingxibackend.websocket.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class ByteToLongDecoder extends ChannelDuplexHandler {
    // 解码器
    public Long decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.readableBytes() >= 8) {
            return in.readLong();
        }
        return null;
    }

}
