package com.weleadin.connection.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Intent service;
    Intent nettyService;
    private boolean isFirst = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nettyService = new Intent(this, NettyService.class);
        startService(nettyService);

//        setContentView(R.);
        finish();
    }
}
