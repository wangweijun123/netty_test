package com.example.administrator.nettytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.encryt.WLDEncryptUtils;
import com.weleadin.connection.service.NettyService;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userId = new Random().nextInt(1000)+"";
        ((TextView)findViewById(R.id.tv)).setText(userId);
        NettyService.startLongConnectionService(getApplicationContext(), userId);

        testEnc();
    }



        public void testEnc() {
            String content = "123456";
            String temp = WLDEncryptUtils.encryptMode(content);
            System.out.println(temp);

            String re = WLDEncryptUtils.decryptMode(temp);
            System.out.println(re);
    }
}
