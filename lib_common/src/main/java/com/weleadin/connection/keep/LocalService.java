package com.weleadin.connection.keep;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.weleadin.connection.service.NettyService;
import com.wld.process.IMyAidlInterface;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public class LocalService extends Service {

    public static final String TAG = NettyService.TAG;
    private MyBinder myBinder;

    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double
                aDouble, String aString) throws RemoteException {

        }
    }

    ServiceConnection serviceConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG,"local service onCreate() "+this);

        myBinder = new MyBinder();
        serviceConnection = new ServiceConnection();
        //使Service变成前台服务
        startForeground(20,  NettyService.createNotification());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(new Intent(this, InnnerService.class));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, NettyService.class), serviceConnection,
                BIND_AUTO_CREATE);

        return super.onStartCommand(intent, flags, startId);
    }

    class ServiceConnection implements android.content.ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接后回调
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG,"remote 进程可能被干掉了，拉活");
            //连接中断后回调
            startService(new Intent(LocalService.this, NettyService.class));
            bindService(new Intent(LocalService.this, NettyService.class),serviceConnection,
                    BIND_AUTO_CREATE);
        }
    }

    public static class InnnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            // 前台foreground service, 为了不弹出通知
            startForeground(20, NettyService.createNotification());
            stopSelf();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
