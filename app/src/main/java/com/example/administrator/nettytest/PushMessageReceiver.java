package com.example.administrator.nettytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weleadin.connection.service.MessageBean;
import com.weleadin.connection.service.NettyService;

public class PushMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "wangweijun";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        Log.i(TAG, "action :" +action);
        if (NettyService.ACTION_MESSAGE.equals(action)) {
            String hh = intent.getStringExtra("message");
            MessageBean messageBean = (MessageBean) intent.getSerializableExtra("messageBean");
            Log.i(TAG, "messageBean :" +messageBean);
            Log.i(TAG, "hh :" +hh);
        } else if (NettyService.ACTION_CLICK.equals(action)) {
            MessageBean messageBean = (MessageBean) intent.getSerializableExtra("messageBean");
            Log.i(TAG, "messageBean :" +messageBean);
        }

    }
}
