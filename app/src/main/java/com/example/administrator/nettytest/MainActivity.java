package com.example.administrator.nettytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.weleadin.connection.keep.LocalService;
import com.weleadin.connection.service.NettyService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(getApplicationContext(), com.weleadin.connection.service.MainActivity.class));

        NettyService.startLongConnectionService(getApplicationContext());
    }
}
