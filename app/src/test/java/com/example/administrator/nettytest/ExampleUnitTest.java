package com.example.administrator.nettytest;

import com.example.administrator.encryt.WLDEncryptUtils;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testEnc() {
        String content = "123456";
        String temp = WLDEncryptUtils.encryptMode(content);
        System.out.println(temp);

        String re = WLDEncryptUtils.decryptMode(temp);
        System.out.println(re);

    }
}