package com.xiaomi.eestormtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;


import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    public String regid ;
    static String EID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        //使用findViewById 得到TextView对象
        editText = (EditText)findViewById(R.id.regid);
        //使用setText()方法修改文本
        regid = MiPushClient.getRegId(getApplicationContext());
        editText.setText("regid===========\n"+regid);
        editText.setSingleLine(false);



        List tags = new ArrayList();
        tags.add("test");
        CEFService.createEID(tags, "storm", new HttpCallBackListener() {
            @Override
            public void onFinish(String respose) {
                EID = respose;

                CEFService.registerNotification(EID, Channel.BAIDU,new HttpCallBackListener() {
                    @Override
                    public void onFinish(String respose) {
                        System.out.print(respose);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }


}
