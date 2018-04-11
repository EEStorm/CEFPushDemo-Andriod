package com.xiaomi.eestormtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String s = GetPostUrl.post("https://virtserver.swaggerhub.com/MS-ACE/CEF/1.0.0/users",new HashMap<String, String>());

        String eid = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
            eid = jsonObject.getString("eid");
        }catch(JSONException e){

        }
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("tags");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonPerson = result.getJSONObject(i);
                String eara = jsonPerson.getString("name");
            }
            eid = jsonObject.getString("customizedUID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.print(eid);

        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,  "eMS5zqIT0RbRbtDoZHAnze12"  );

    }
}
