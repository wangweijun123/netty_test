package com.weleadin.connection.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.weleadin.connection.R;
import com.weleadin.connection.netty.NettyClient;
import com.weleadin.connection.netty.NettyListener;
import com.weleadin.connection.util.NotificationUtil;

import org.json.JSONObject;


public class NettyService extends Service implements NettyListener {

    private static final String TAG = NettyService.class.getSimpleName();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        NettyClient.getInstance().setListener(this);
    //    EventBus.getDefault().register(this);
        connect();

    }

    private void connect() {
        if (!NettyClient.getInstance().getConnectStatus()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NettyClient.getInstance().connect();//连接服务器
                }
            }).start();
        }
    }

    private void sendMessage(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("instructions","BIND_USER");
        jsonObject.addProperty("usrCode", "u123456789");
        NettyClient.getInstance().sendMessage2(jsonObject,null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
          return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
        NettyClient.getInstance().setReconncetNum(0);
        NettyClient.getInstance().disconnect();
    }

    @Override
    public void onMessageResponse(String jsonMSG) {
        try {
            MessageBean messageBean = JSON.parseObject(jsonMSG,MessageBean.class);
            Log.e(TAG,"messageBean.getInstructions() : "+messageBean.getInstructions());
//            if (MessageBean.EVENT_MESSAGE_PUSH.equals(messageBean.getInstructions())) {
                new NotificationUtil(getApplicationContext(), 1).
                        sendSingleLineNotification("", "this is title", "content",
                                R.drawable.ic_launcher, null, false, false, false);
//            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        Log.e(TAG,"status:"+statusCode);
        if(statusCode == NettyListener.STATUS_CONNECT_SUCCESS){
            Log.e(TAG,"连接成功后发送消息");
            sendMessage();
        }
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(NetworkChangeEvent event) {
        hasNetWork(event.isConnected);
    }
    private void hasNetWork(boolean has) {
            if (has) {
                connect();
            } else {
               *//* NettyClient.getInstance().setReconncetNum(0);
                NettyClient.getInstance().disconnect();*//*
            }
    }*/
}
