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

import java.util.Random;

public class NettyService extends Service implements NettyListener {

    public static final String TAG = NettyService.class.getSimpleName();

    public static final String ACTION_MESSAGE = "com.weleadin.action.MESSAGE";
    public static final String ACTION_CLICK = "com.weleadin.action.CLICK";


    private static final int NOTIFICATION_ID = 20;

    private MyBinder myBinder;

    private  ServiceConnection serviceConnection;

    private int notificationId = 1;

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
    private String userId;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocalService.class), serviceConnection,
                BIND_AUTO_CREATE);

        userId = intent.getStringExtra("userId");
        connect();
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
        jsonObject.addProperty("usrCode", userId);
        NettyClient.getInstance().sendMessage2(jsonObject,null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NettyClient.getInstance().setReconncetNum(0);
        NettyClient.getInstance().disconnect();
    }

    @Override
    public void onMessageResponse(String jsonMSG) {
        try {
            MessageBean messageBean = JSON.parseObject(jsonMSG,MessageBean.class);
            Log.e(TAG,messageBean.toString());
            if (MessageBean.EVENT_MESSAGE_PUSH.equals(messageBean.getInstructions())) {
                // 通知其他module
                Intent intent = new Intent(ACTION_MESSAGE);
                intent.putExtra("message", jsonMSG);
                intent.putExtra("messageBean", messageBean);
                sendBroadcast(intent);

                new NotificationUtil(getApplicationContext(), notificationId++).
                        sendSingleLineNotification("", messageBean.getMsgTitle(), messageBean.getContent(),
                                R.drawable.ic_qr_pay_ewm_02,createBroadcast(messageBean),
                                false, false, false);
                handleSuccess(messageBean);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleSuccess(MessageBean messageBean) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("instructions","CLIENT_RESPOND");
        jsonObject.addProperty("status", "complete");
        jsonObject.addProperty("msgId", messageBean.getMsgId());
        NettyClient.getInstance().sendMessage2(jsonObject,null);
    }

    private PendingIntent createBroadcast(MessageBean messageBean) {
        Intent setAlertIntent=new Intent(this,AlertReceiver.class);
        setAlertIntent.putExtra("messageBean", messageBean);
        return PendingIntent.getBroadcast(getApplicationContext(), 1, setAlertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        Log.e(TAG,"status:"+statusCode);
        if(statusCode == NettyListener.STATUS_CONNECT_SUCCESS){
            //连接成功后发送消息
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

    public static void startLongConnectionService(Context context, String userId) {
        Intent intent = new Intent(context, NettyService.class);
        intent.putExtra("userId", userId);
        context.startService(intent);
        context.startService(new Intent(context, LocalService.class));
    }
}
