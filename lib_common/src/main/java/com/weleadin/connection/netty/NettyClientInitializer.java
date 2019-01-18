package com.weleadin.connection.netty;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel>{

    private NettyListener listener;

    public NettyClientInitializer(NettyListener listener) {
        if(listener == null){
            throw new IllegalArgumentException("listener == null ");
        }
        this.listener = listener;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 注意这个"\NotificationUtil"连包问题的分割符
        ByteBuf buf = Unpooled.copiedBuffer("\t".getBytes());
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast(new DelimiterBasedFrameDecoder(2048,buf));
        pipeline.addLast("IdleStateHandler",
                new IdleStateHandler(0,60,0, TimeUnit.SECONDS));// 一分钟一次心跳
        pipeline.addLast("StringDecoder",new StringDecoder());
        pipeline.addLast("StringEncoder",new StringEncoder());
        pipeline.addLast(new NettyClientHandler(listener));
    }
}
