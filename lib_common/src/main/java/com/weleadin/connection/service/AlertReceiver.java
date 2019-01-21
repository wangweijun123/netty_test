package com.weleadin.connection.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 通知其他module
        // 通知其他module
        Intent in = new Intent(NettyService.ACTION_CLICK);
        in.putExtra("messageBean", intent.getSerializableExtra("messageBean"));
        context.sendBroadcast(in);
    }
}
