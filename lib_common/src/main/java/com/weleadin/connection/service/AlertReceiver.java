package com.weleadin.connection.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String hh = intent.getStringExtra("try");
        Log.i("wangweijun", "hh :" +hh);
    }
}
