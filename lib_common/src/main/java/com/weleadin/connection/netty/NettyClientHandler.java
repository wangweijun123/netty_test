package com.weleadin.connection.netty;


import android.util.Log;

import com.google.gson.JsonObject;
import com.weleadin.connection.service.NettyService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private static final String TAG = NettyService.TAG;
    private NettyListener listener;


    public NettyClientHandler(NettyListener linstener) {
        this.listener = linstener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyClient.getInstance().setConnectStatus(true);
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.e(TAG,"连接被断开 ChannelHandlerContext:"+ctx+", name:"+Thread.currentThread().getName()
        +"  id:"+Thread.currentThread().getId());
        NettyClient.getInstance().setConnectStatus(false);
   //     ctx.close();
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED);
        NettyClient.getInstance().reconnect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Log.e(TAG, "来自服务器的消息 ====》" + msg);
        listener.onMessageResponse(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Log.e(TAG,"exceptionCaught:"+cause.getMessage());
        ctx.close();

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE){
                /*try{
                    //
//                    Log.e(TAG, "发送心跳");
//                    ctx.channel().writeAndFlush("这是心跳信息");
                } catch (Exception e){
                    e.printStackTrace();
                }*/
                Log.e(TAG, "发送心跳");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("instructions","HEART_BEAT");
                ctx.channel().writeAndFlush(jsonObject.toString()+"\t").addListener(new FutureListener() {
                    @Override
                    public void success() {
//                        Log.e(TAG,"发送心跳 success");
                    }

                    @Override
                    public void error() {
//                        Log.e(TAG,"发送心跳 failure");
                    }
                });
            }
        }
    }
}
