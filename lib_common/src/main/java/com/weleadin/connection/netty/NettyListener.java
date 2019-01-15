package com.weleadin.connection.netty;

public interface NettyListener {

    byte STATUS_CONNECT_SUCCESS = 1;

    byte STATUS_CONNECT_CLOSED = 2;

    byte STATUS_CONNECT_ERROR = 0;

    /**
     * 对消息处理
     * @param message
     */
    void onMessageResponse(String message);

    /**
     * 当服务状态发生变化时触发
     * @param statusCode
     */
    void onServiceStatusConnectChanged(int statusCode);
}
