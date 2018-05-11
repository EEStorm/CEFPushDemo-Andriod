package com.xiaomi.eestormtest.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eestorm.eeslibrary.CEFClient;
import com.eestorm.eeslibrary.HttpCallBackListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaomi.eestormtest.LogInterceptor;
import com.xiaomi.eestormtest.R;
import com.xiaomi.eestormtest.pay.ali.PayResult;
import com.xiaomi.eestormtest.pay.wx.PayEntity;
import com.xiaomi.eestormtest.pay.wx.PayResponeEntity;
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
import java.util.Map;

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
    /**
     * 支付宝支付业务：入参app_id
     */
    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
    }


    private void loginWithWX(){
        //api注册
        api = WXAPIFactory.createWXAPI(this, "APP_ID", true);
        api.registerApp("APP_ID");

        SendAuth.Req req = new SendAuth.Req();
        //授权读取用户信息
        req.scope = "snsapi_userinfo";

        //自定义信息
        req.state = "wechat_sdk_demo_test";

        //向微信发送请求
        api.sendReq(req);
    }




    public void WXPay(View view) throws IOException {

        List tags = new ArrayList();
        tags.add("test");
        final String url = "https://xzshengwebhookwatcher.azurewebsites.net/api/weChatPaymentWebhook";
        final String EID = new CEFClient(this).createEID(tags, "storm");
        Log.d(TAG, "EID: " + EID);


        new Thread(new Runnable() {
            @Override
            public void run() {
                requestPost("wxa186d3f0aa51c56e", EID, "WeChat", "Test", "TestTradeNumber15", "1", url, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String respose) {
                        Log.d(TAG, "onFinish====requestPost: " + respose);
                        Gson gson = new Gson();
                        PayResponeEntity   mPayResponeEntity = gson.fromJson(respose, PayResponeEntity.class);
                        weChatPay(mPayResponeEntity.getProperties());
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError ====requestPost: " + e.toString());
                    }
                });
            }
        }).start();

    }

    private void AliPay(View view){

    }

    private void initZFB() {

        //orderInfo 后台给的
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
               String str = "app_id=2017111609966863&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA2&version=1.0&return_url=&notify_url=&timestamp=2017-12-01+14%3A42%3A57&sign=rLOtYId3y5rUaXnWl2sEjFJQ7k5ATyanjddS1oNbX69G194VMNBcWKEqLGfmvlpN91sWmasYn24IPX7YhJYNZTkGStyvLednht9LUMClbeDa%2FSxB6DHS0FE77EaAOa970KEhix4fO%2Fu96%2B%2BlJHiPB4m0jf%2Bw43Hc4m7z2Oy86DPJOfFpxb4vPeY9tTurAG8QM18BQAh%2FHw207%2FFkOrSJ1u9x3Vw6ZMdemIRTaFy5HDdu6xYRgR2%2FyVJIq%2B9lHtzQRY4BhzdmdsWBUu7MeFP4uIEtE%2FfQcdK87MoCPZI%2FudtuwzfVwmZKIWQ3%2BaDkbVTDTGeLVNuwlbGl%2FdzHN1NH9Q%3D%3D&biz_content=%7B%22out_trade_no%22%3A%221712010242573400328%22%2C%22total_amount%22%3A%223852.00%22%2C%22subject%22%3A%22a1712010242573400328%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D";
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(str, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler handler = new Handler();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.i("resultStatus", "resultStatus:" + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void weChatPay(WeChatPayEntity entity) {
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);
        PayReq req = new PayReq();
        req.appId = entity.appid;
        req.partnerId = entity.partnerid;
        req.prepayId = entity.prepayid;
        req.nonceStr = entity.noncestr;
        req.timeStamp = entity.timestamp;
        req.packageValue = entity.packageValue;
        req.sign = entity.sign;
        req.extData = "app data"; // optional

        api.sendReq(req);
    }


    void post(String url) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .build();
        List tags = new ArrayList();
        tags.add("test");
        final String EID = new CEFClient(this).createEID(tags, "storm");
        final String notifyUrl = "https://xzshengwebhookwatcher.azurewebsites.net/api/weChatPaymentWebhook";
        PayEntity mPayEntity = new PayEntity();
        mPayEntity.setAppId("wxa186d3f0aa51c56e");
        mPayEntity.setEid(EID);
        mPayEntity.setChannel("WeChat");
        mPayEntity.setTradeNumber("TestTradeNumber15");
        mPayEntity.setAmount("1");
        mPayEntity.setNotifyUrl(notifyUrl);

        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(mPayEntity);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Log.d("cacacacacacac", "json:" + json + "  body:" + requestBody.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("cacacacacacac", "获取数据失败了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("cacacacacacac", "获取数据成功了");
                Log.d("cacacacacacac", "response.code()==" + response.code());
                Log.d("cacacacacacac", "response.body().string()==" + response.body().string());
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }

    private void postDataWithParame() {
        List tags = new ArrayList();
        tags.add("test");
        final String EID = new CEFClient(this).createEID(tags, "storm");
        final String notifyUrl = "https://xzshengwebhookwatcher.azurewebsites.net/api/weChatPaymentWebhook";
        String baseUrl = "http://xzshengpaymentstaging.eastasia.cloudapp.azure.com/serviceProviders/payment/createOrder";
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor())
                .build();//创建OkHttpClient对象。
        final FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("appId", "wxa186d3f0aa51c56e")
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
        Log.d("cacacacacacac", "formBody:" + formBody.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("cacacacacacac", "获取数据失败了");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("cacacacacacac", "获取数据成功了");
                Log.d("cacacacacacac", "response.code()==" + response.code());
                Log.d("cacacacacacac", "response.body().string()==" + response.body().string());
            }
        });//回调方法的使用与get异步请求相同，此时略。
    }

    /**
     * @param appId       optional, only used for check
     * @param eid
     * @param channel
     * @param subject
     * @param tradeNumber
     * @param amount      In RMB 0.01
     * @param notifyUrl
     * @param callback
     */
    private static void requestPost(String appId, String eid, String channel, String subject, String tradeNumber, String amount, String notifyUrl, final HttpCallBackListener callback) {
        try {
            String baseUrl = "http://xzshengpaymentstaging.eastasia.cloudapp.azure.com/serviceProviders/payment/createOrder";
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
                Log.d(TAG, "success=======requestPost: " + result);
                callback.onFinish(result);

            } else {
                Log.d(TAG, "shibai======error: " + urlConn.getResponseCode());
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
