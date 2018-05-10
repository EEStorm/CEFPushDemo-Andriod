package com.xiaomi.eestormtest.pay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eestorm.eeslibrary.CEFClient;
import com.eestorm.eeslibrary.HttpCallBackListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaomi.eestormtest.R;
import com.xiaomi.eestormtest.pay.wx.WeChatPayEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity {

    private static final String TAG = "PayActivity";
    private static String Eid;
    private IWXAPI api;
    private String APP_ID = "wxb4ba3c02aa476ea1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }

    public void WXPay(View view){


        postDataWithParame();

        /**
         *
         List tags = new ArrayList();
         tags.add("test");
         final String url = "https://xzshengwebhookwatcher.azurewebsites.net/api/weChatPaymentWebhook";
         final String EID = new CEFClient(this).createEID(tags, "storm");
         Log.d(TAG, "EID: "+EID);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String baseUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/serviceProviders/payment/createOrder";
                  String as =  post(baseUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                requestPost("", EID, "WeChat", "Test", "TestTradeNumber15", "1", url, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String respose) {
                        Log.d(TAG, "onFinish====requestPost: "+respose);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError ====requestPost: "+e.toString());
                    }
                });
            }
        }).start();

         */
    }

    private void weChatPay(WeChatPayEntity entity){
        api = WXAPIFactory.createWXAPI(this,APP_ID );
        api.registerApp(APP_ID);
        PayReq req = new PayReq();
        req.appId			= entity.appId;
        req.partnerId		= entity.partnerId;
        req.prepayId		= entity.prepayId;
        req.nonceStr		= entity.nonceStr;
        req.timeStamp		= entity.timeStamp;
        req.packageValue	= entity.packageValue;
        req.sign			= entity.sign;
        req.extData			= "app data"; // optional

        api.sendReq(req);
    }


    OkHttpClient client = new OkHttpClient();
    String post(String url) throws IOException {
        List tags = new ArrayList();
        tags.add("test");
        final String EID = new CEFClient(this).createEID(tags, "storm");
        final String notifyUrl = "https://xzshengwebhookwatcher.azurewebsites.net/api/weChatPaymentWebhook";
        RequestBody formBody = new FormBody.Builder()
                .add("appId", "wxa186d3f0aa51c56e")
                .add("eid", EID)
                .add("channel", "WeChat")
                .add("subject", "test")
                .add("tradeNumber", "TestTradeNumber15")
                .add("amount", "1")
                .add("notifyUrl", notifyUrl)
                .build();

        Log.d("cacacacacacac","formBody:"+formBody.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String res = response.body().string();
            Log.d(TAG, "post: "+res);
            return res;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private void postDataWithParame() {
        List tags = new ArrayList();
        tags.add("test");
        final String EID = new CEFClient(this).createEID(tags, "storm");
        final String notifyUrl = "https://xzshengwebhookwatcher.azurewebsites.net/api/weChatPaymentWebhook";
      //  String baseUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/serviceProviders/payment/createOrder";
        String baseUrl = "http://xzshengpaymentstaging.eastasia.cloudapp.azure.com/serviceProviders/payment/createOrde";
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("appId", "appId")
                .add("eid", EID)
                .add("channel", "WeChat")
                .add("subject", "test")
                .add("tradeNumber", "TestTradeNumber15")
                .add("amount", "1")
                .add("notifyUrl", notifyUrl);//传递键值对参数
        Request request = new Request.Builder()//创建Request 对象。
                .url(baseUrl)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("cacacacacacac","获取数据失败了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("cacacacacacac","获取数据成功了");
                Log.d("cacacacacacac","response.code()=="+response.code());
                Log.d("cacacacacacac","response.body().string()=="+response.body().string());
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }

    /**
     *
     * @param appId   optional, only used for check
     * @param eid
     * @param channel
     * @param subject
     * @param tradeNumber
     * @param amount      In RMB 0.01
     * @param notifyUrl
     * @param callback
     */
    private static void requestPost(String appId,String eid,String channel,String subject,String tradeNumber,String amount,String notifyUrl, final HttpCallBackListener callback) {
        try {
            String baseUrl = "http://cefsfcluster.chinanorth.cloudapp.chinacloudapi.cn/serviceProviders/payment/createOrder";
            JsonObject json = new JsonObject();
            json.addProperty("appId", appId);
            json.addProperty("eid", eid);
            json.addProperty("channel", channel);
            json.addProperty("subject", subject);
            json.addProperty("tradeNumber", tradeNumber);
            json.addProperty("amount", amount);
            json.addProperty("notifyUrl", notifyUrl);
            URL url = new URL(baseUrl);
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
                Log.d(TAG, "success=======requestPost: "+result);
                callback.onFinish(result);

            } else {
                Log.d(TAG, "shibai======error: "+urlConn.getResponseCode());
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
