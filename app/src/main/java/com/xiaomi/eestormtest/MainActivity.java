package com.xiaomi.eestormtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.xiaomi.mipush.sdk.MiPushClient;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    public String regid ;

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


        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,  "eMS5zqIT0RbRbtDoZHAnze12"  );

    }
}
