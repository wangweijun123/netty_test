package com.weleadin.connection.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.weleadin.connection.keep.LocalService;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        startService(new Intent(this, NettyService.class));
//        startService(new Intent(this, LocalService.class));
//        setContentView(R.);
        finish();
    }
}
