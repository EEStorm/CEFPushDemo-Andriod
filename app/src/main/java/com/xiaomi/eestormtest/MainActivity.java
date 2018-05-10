package com.xiaomi.eestormtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.eestorm.eeslibrary.Channel;
import com.eestorm.eeslibrary.HttpCallBackListener;
import com.xiaomi.eestormtest.pay.PayActivity;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;

import com.eestorm.eeslibrary.CEFClient;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    public String regid ;
    static String EID = "111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //使用findViewById 得到TextView对象
        editText = (EditText)findViewById(R.id.regid);
        //使用setText()方法修改文本
        regid = MiPushClient.getRegId(getApplicationContext());
        editText.setText("regid===========\n"+regid);
        editText.setSingleLine(false);



        List tags = new ArrayList();
        tags.add("test");
        EID =  new CEFClient(this).createEID(tags, "storm");
        Log.i("httprequest","====MainActivity==="+EID);
        CEFClient.registerNotification(EID, Channel.BAIDU,new HttpCallBackListener() {
            @Override
            public void onFinish(String respose) {
                System.out.print(respose);

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void toPay(View view){
        Intent intent = new Intent();
        intent.setClass(this, PayActivity.class);
        startActivity(intent);
    }
}
