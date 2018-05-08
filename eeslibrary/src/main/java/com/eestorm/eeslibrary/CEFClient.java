package com.eestorm.eeslibrary;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.eestorm.eeslibrary.Channel;
import com.eestorm.eeslibrary.ConfigurationSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by v-shbo on 2018/4/9.
 */


public class CEFClient extends Application {

    static String EId = "";
    private static HttpCallBackListener ProfileCallback;
    private static Context context;

    public static final String MI_APP_ID = "2882303761517683551";
    public static final String MI_APP_KEY = "5801768389551";
    public static final String BAIDU_APP_KEY = "eMS5zqIT0RbRbtDoZHAnze12";
    String createEIDUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users";
    List<String> tags;
    String customId;
    final String[] localEid = {""};

    public CEFClient(Context context) {
        this.context = context;
    }


    /**
     * 需求做成同步效果，但是官方不允许同步请求网络，所以，线程等待，然后拿到localEid     myThread.join();
     * <p>
     * The need to make a synchronization effect, but the Google official does not allow synchronous request network,
     * so the thread waits, then get the localEid     myThread.join();
     *
     * @param tags
     * @param customId
     * @return
     */
    public String createEID(final List<String> tags, final String customId) {
        this.tags = tags;
        this.customId = customId;

        Thread myThread = new LocalThread();
        myThread.start();
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("httprequest", "====createEID===" + EId + "  == :" + localEid[0]);
        return localEid[0];
    }

    public class LocalThread extends Thread {
        @Override
        public void run() {
            super.run();
            localEid[0] = requestPost(createEIDUrl, tags, customId, true);
        }
    }


    public static void createEID(final List<String> tags, final String customId, final HttpCallBackListener callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String createEIDUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users";
                requestPost(createEIDUrl, tags, customId, callback, true);

            }
        }).start();


    }

    public static void registerNotification(final String EID, final Channel channel, final HttpCallBackListener callback) {

        registerChannel();
        ConfigurationSettings.channel = channel;
        EId = EID;
        ProfileCallback = callback;
    }


    public static void registerNotificationPost(final String userId, final String channelId) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String registerNotificationUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users/" + EId + "/serviceproviders/notification";
                    JsonObject json = new JsonObject();
                    JsonObject properties = new JsonObject();
                    properties.addProperty("targetId", userId + "-" + channelId);
                    json.add("properties", properties);
                    if (ConfigurationSettings.channel == Channel.BAIDU) {
                        json.addProperty("channel", "android/baidu");
                    } else {
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

                        System.out.print("Post方式请求成功，result--->");

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


    private static String requestPost(String urlstr, List<String> tags, String customId, boolean isNeedEID) {
        try {
            String baseUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users";
            JsonObject json = new JsonObject();
            JsonArray jsonA = new JsonArray();
            for (String tag : tags) {
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
            if (isNeedEID != true) {
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
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    EId = jsonObj.optString("eid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("httprequest", "===========失败=========== ");
            }
            urlConn.disconnect();

        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return EId;
    }

    private static void requestPost(String urlstr, List<String> tags, String customId, final HttpCallBackListener callback, boolean isNeedEID) {
        try {
            String baseUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/users";
            JsonObject json = new JsonObject();
            JsonArray jsonA = new JsonArray();
            for (String tag : tags) {
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
            if (isNeedEID != true) {
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

                System.out.print("Post方式请求成功，result--->");

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(result);
                    EId = jsonObj.optString("eid");
                } catch (JSONException e) {
                    e.printStackTrace();
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

    public static void registerChannel() {
        if (shouldInit()) {
            if (ConfigurationSettings.channel == Channel.BAIDU) {
                String BD_key = readMetaDataFromApplication(context, "baidu_app_key");
                PushManager.startWork(context.getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, BD_key);
            } else {
                String id = readMetaDataFromApplication(context, "mi_app_id");
                String key = readMetaDataFromApplication(context, "MI_APP_KEY");
                MiPushClient.registerPush(context.getApplicationContext(), id, key);

                MiPushClient.setAlias(context.getApplicationContext(), "alias", null);
                MiPushClient.setUserAccount(context.getApplicationContext(), "userAccount", null);
            }
        }
    }

    /**
     * 读取application 节点  meta-data 信息
     * <p>
     * Read application node meta-data information
     */
    private static String readMetaDataFromApplication(Context context, String tag) {
        String mTag = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            mTag = appInfo.metaData.getString(tag);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mTag;
    }

    private static boolean shouldInit() {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
