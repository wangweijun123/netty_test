package com.example.administrator.nettytest;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Debug;
import android.util.Log;

public class NettyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initExclude();
    }

    private void initExclude() {
        Debug.startMethodTracing(getCacheDir().getAbsolutePath()+"/app.trace");
        int pid = android.os.Process.myPid();
        String processNameString = "";
        ActivityManager mActivityManager = (ActivityManager) this.getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        try{
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    processNameString = appProcess.processName;
                    break;
                }
            }
        }catch(Exception e){
        }
        Debug.stopMethodTracing();
        Log.e("NettyService","onCreate:"+this + " processNameString:"+processNameString);
        if (!"com.example.administrator.nettytest".equals(processNameString)) {
            return;
        }
    }
}
