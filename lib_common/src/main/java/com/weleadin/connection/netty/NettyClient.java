package com.weleadin.connection.netty;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.weleadin.connection.service.NettyService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static final String TAG = NettyService.TAG;

    private static NettyClient nettyClient = new NettyClient();

    private EventLoopGroup group;

    private NettyListener listener;

    private Channel channel;

    private boolean isConnect = false;

    private int reconnectNum = Integer.MAX_VALUE;

    private long reconnectIntervalTime = 30 * 1000;

    private Bootstrap bootstrap;

    private final Gson gson;

    public NettyClient() {
        gson = new Gson();
    }

    public static NettyClient getInstance(){
        return nettyClient;
    }

    public synchronized NettyClient connect(){
        if(!isConnect){
            group = new NioEventLoopGroup();
            bootstrap = new Bootstrap().group(group)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyClientInitializer(listener));

            try {
                ChannelFuture future = bootstrap.connect(Configs.SOCKET_HOST,Configs.SOCKET_PORT).sync();
                if(future != null && future.isSuccess()){
                    channel = future.channel();
                    isConnect = true;
                }else {
                    isConnect = false;
                }

            }catch (Exception e) {
                e.printStackTrace();
                listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_ERROR);
                reconnect();
            }
        }
        return this;
    }

    public void disconnect(){
        if (channel != null){
            channel.close();
        }
        if(group != null){
            group.shutdownGracefully();
        }


    }

    public void reconnect(){
        if(reconnectNum > 0 && !isConnect){
            reconnectNum --;
            try {
                Thread.sleep(reconnectIntervalTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            disconnect();
            connect();
        }else{
            connect();
        }
    }

    /**
     * 发送消息
     * @param vo
     * @param futureListener
     */
    public void sendMessage(Object vo, FutureListener futureListener){
        boolean flag = channel != null && isConnect;
        if(!flag){
            return;
        }

        final String str = gson.toJson(vo);
        if(futureListener == null){ //  注意这个"\NotificationUtil"连包问题的分割符
            channel.writeAndFlush(str+"\t").addListener(new FutureListener() {
                @Override
                public void success() {
                    Log.e(TAG,"send message success");
                }

                @Override
                public void error() {
                    Log.e(TAG,"send message failure");
                }
            });
        }else{
            channel.writeAndFlush(str).addListener(futureListener);
        }
    }


    public void sendMessage2(JsonObject jsonObject, FutureListener futureListener){
        boolean flag = channel != null && isConnect;
        if(!flag){
            return;
        }

        final String str = jsonObject.toString();
        Log.e(TAG,"send message str:"+str);
        if(futureListener == null){ //  注意这个"\NotificationUtil"连包问题的分割符
            channel.writeAndFlush(str+"\t").addListener(new FutureListener() {
                @Override
                public void success() {
                    Log.e(TAG,"send message success");
                }

                @Override
                public void error() {
                    Log.e(TAG,"send message failure");
                }
            });
        }else{
            channel.writeAndFlush(str).addListener(futureListener);
        }
    }

    /**
     * 设置重连次数
     *
     * @param reconnectNum 重连次数
     */
    public void setReconncetNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    /**
     * 设置重连时间间隔
     *
     * @param reconnectIntervalTime 时间间隔
     */

    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }

    public boolean getConnectStatus() {
        return isConnect;
    }
    /**
     * 设置连接状态
     *
     * @param status
     */
    public void setConnectStatus(boolean status) {
        isConnect = status;
    }

    public void setListener(NettyListener listener){
        if (listener == null){
            throw new IllegalArgumentException("listener == null ");
        }
        this.listener = listener;
    }
}
