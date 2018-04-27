package com.xiaomi.eestormtest;

import android.app.Application;
import android.app.usage.ConfigurationStats;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by v-shbo on 2018/4/9.
 */


interface HttpCallBackListener {
    void onFinish(String respose);

    void onError(Exception e);
}
public class CEFService extends Application {

    static String EId = "";
    private static HttpCallBackListener ProfileCallback ;
    public static void createEID(final List<String> tags,final String customId,final HttpCallBackListener callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String createEIDUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users";
                requestPost(createEIDUrl,tags,customId,callback,true);

            }
        }).start();


    }

    public static void registerNotification(final String EID,final Channel channel , final HttpCallBackListener callback){

        DemoApplication.getInstant().registerChannel();
        ConfigurationSettings.channel = channel;
        EId = EID;
        ProfileCallback = callback;
    }


    public static void registerNotificationPost(final String userId, final String channelId) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    String registerNotificationUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users/"+EId+"/serviceproviders/notification";
                    JsonObject json = new JsonObject();
                    JsonObject properties = new JsonObject();
                    properties.addProperty("targetId",userId +"-"+ channelId);
                    json.add("properties",properties);
                    if (ConfigurationSettings.channel == Channel.BAIDU){
                        json.addProperty("channel", "android/baidu");
                    }else {
                        json.addProperty("channel", "android/xiaomi");
                    }

                    URL url = new URL(registerNotificationUrl);
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setConnectTimeout(5 * 1000);
                    urlConn.setReadTimeout(5 * 1000);
                    urlConn.setDoOutput(true);
                    urlConn.setDoInput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestMethod("POST");
                    urlConn.setInstanceFollowRedirects(true);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.connect();
                    DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                    dos.write(json.toString().getBytes());
                    dos.flush();
                    dos.close();
                    if (urlConn.getResponseCode() == 200) {
                        String result = streamToString(urlConn.getInputStream());

                        System.out.print( "Post方式请求成功，result--->" );

                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ProfileCallback.onFinish(result);


                    } else {
                        System.out.print("shibai");

                    }
                    urlConn.disconnect();

                } catch (Exception e) {
                    System.out.print(e.toString());
                    ProfileCallback.onError(e);
                }
            }
        }).start();
    }


    private static void requestPost(String urlstr, List<String> tags, String customId,final HttpCallBackListener callback,boolean isNeedEID) {
        try {
            String baseUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users";
            JsonObject json = new JsonObject();
            JsonArray jsonA = new JsonArray();
            for (String tag:tags){
                jsonA.add(tag);
            }
            json.add("tags", jsonA);
            json.addProperty("customId", customId);
            URL url = new URL(urlstr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            if (isNeedEID != true){
                urlConn.setRequestMethod("PUT");
            }
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.connect();
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(json.toString().getBytes());
            dos.flush();
            dos.close();
            if (urlConn.getResponseCode() == 200) {
                String result = streamToString(urlConn.getInputStream());

                System.out.print( "Post方式请求成功，result--->" );

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    EId = jsonObj.optString("eid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (callback != null){
                    if (isNeedEID == true){
                        callback.onFinish(EId);
                    }else {
                        callback.onFinish(result);
                    }

                }

            } else {
                System.out.print("shibai");

            }
            urlConn.disconnect();

        } catch (Exception e) {
            System.out.print(e.toString());
            callback.onError(e);
        }

    }



    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {

            return null;
        }
    }

}
