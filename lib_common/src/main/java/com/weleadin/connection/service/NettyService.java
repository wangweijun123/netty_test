package com.weleadin.connection.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.weleadin.connection.R;
import com.weleadin.connection.keep.LocalService;
import com.weleadin.connection.netty.NettyClient;
import com.weleadin.connection.netty.NettyListener;
import com.weleadin.connection.util.NotificationUtil;
import com.wld.process.IMyAidlInterface;

public class NettyService extends Service implements NettyListener {

    public static final String TAG = NettyService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 20;

    private MyBinder myBinder;

    private  ServiceConnection serviceConnection;

    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double
                aDouble, String aString) throws RemoteException {
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();
        NettyClient.getInstance().setListener(this);
    //    EventBus.getDefault().register(this);
        connect();

        myBinder = new MyBinder();
        serviceConnection = new ServiceConnection();
        //使Service变成前台服务
        startForeground(NOTIFICATION_ID, createNotification());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Log.e(TAG,"启动内部netty inner service");
            startService(new Intent(this, InnnerService.class));
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocalService.class), serviceConnection,
                BIND_AUTO_CREATE);
        return Service.START_STICKY;
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
    public void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
        NettyClient.getInstance().setReconncetNum(0);
        NettyClient.getInstance().disconnect();
    }

    private int notificationId = 1;
    @Override
    public void onMessageResponse(String jsonMSG) {
        try {
            MessageBean messageBean = JSON.parseObject(jsonMSG,MessageBean.class);
            Log.e(TAG,messageBean.toString());
            if (MessageBean.EVENT_MESSAGE_PUSH.equals(messageBean.getInstructions())) {
                new NotificationUtil(getApplicationContext(), notificationId++).
                        sendSingleLineNotification("", messageBean.getMsgTitle(), messageBean.getContent(),
                                R.drawable.ic_qr_pay_ewm_02,createBroadcast(),
                                false, false, false);
                handleSuccess(messageBean);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleSuccess(MessageBean messageBean) {
        // {"instructions":"CLIENT_RESPOND","status","complete","msgId":"213","failReason":"推送失败原因"}
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("instructions","CLIENT_RESPOND");
        jsonObject.addProperty("status", "complete");
        jsonObject.addProperty("msgId", messageBean.getMsgId());
        NettyClient.getInstance().sendMessage2(jsonObject,null);
    }

    private PendingIntent createBroadcast() {
        Intent setAlertIntent=new Intent(this,AlertReceiver.class);
        setAlertIntent.putExtra("try", "i'm just have a try");
        return PendingIntent.getBroadcast(getApplicationContext(), 1, setAlertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        Log.e(TAG,"status:"+statusCode);
        if(statusCode == NettyListener.STATUS_CONNECT_SUCCESS){
//            Log.e(TAG,"连接成功后发送消息");
            sendMessage();
        }
    }


    class ServiceConnection implements android.content.ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接后回调
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG,"主进程可能被干掉了，拉活");
            //连接中断后回调
            startService(new Intent(NettyService.this, LocalService.class));
            bindService(new Intent(NettyService.this, LocalService.class),serviceConnection,
                    BIND_AUTO_CREATE);
        }
    }

    public static class InnnerService extends Service {
        @Override
        public void onCreate() {
            super.onCreate();
            // 前台foreground service, 为了不弹出通知
            startForeground(NOTIFICATION_ID, createNotification());
            stopSelf();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public static Notification createNotification() {
        return  new Notification();
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


    public static void startLongConnectionService(Context context) {
        context.startService(new Intent(context, NettyService.class));
        context.startService(new Intent(context, LocalService.class));
    }
}
