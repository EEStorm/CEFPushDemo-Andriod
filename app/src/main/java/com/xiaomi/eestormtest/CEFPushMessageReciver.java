package com.xiaomi.eestormtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by v-shbo on 2018/4/12.
 */



public class CEFPushMessageReciver extends BroadcastReceiver{




    public static final String BAIDU_ONBIND = "com.baidupush.onbindResponseString";
    public static final String BAIDU_MESSAGE = "com.baidupush.messageString";
    public static final String BAIDU_NOTIFICATION_ARRIVED = "com.baidupush.onNotificationArrived";
    public static final String BAIDU_ONUNBIND = "com.baidupush.onUnbind";

    public static final String XIAOMI_MESSAGE = "com.xiamipush.messageString";
    public static final String XIAOMI_ONTIFICATIONCLICKED = "com.xiamipush.onNotificationclicked";
    public static final String XIAOMI_NOTIFICATIONARRIVED = "com.xiamipush.onNotificationArrived";


    public static int m = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        CEFDemoMessageReceiver reciver = new CEFDemoMessageReceiver();
        switch (action){
            case BAIDU_ONBIND:
                String responseString = intent.getStringExtra(BAIDU_ONBIND);
                reciver.baiduOnbind(context,responseString);
                testbaiduOnbind(responseString);
                break;
            case BAIDU_MESSAGE:
                String messageString = intent.getStringExtra(BAIDU_MESSAGE);
                reciver.baiduOnMessage(context,messageString);
                break;
            case BAIDU_NOTIFICATION_ARRIVED:
                String onNotificationArrived = intent.getStringExtra(BAIDU_NOTIFICATION_ARRIVED);
                reciver.baiduOnNotificationArrived(context,onNotificationArrived);
                break;
            case BAIDU_ONUNBIND:
                String baiduOnNotificationClicked = intent.getStringExtra(BAIDU_ONUNBIND);
                reciver.baiduOnUnbind(context,baiduOnNotificationClicked);
                break;
            case XIAOMI_MESSAGE:
                String xiaomiMessage = intent.getStringExtra(XIAOMI_MESSAGE);
                reciver.xiaomiThroughMessage(context,xiaomiMessage);
                break;
            case XIAOMI_ONTIFICATIONCLICKED:
                String xiaomiClicked = intent.getStringExtra(XIAOMI_ONTIFICATIONCLICKED);
                reciver.xiaomiNotificationClicked(context,xiaomiClicked);
                break;
            case XIAOMI_NOTIFICATIONARRIVED:
                String xiaomiArrived = intent.getStringExtra(XIAOMI_NOTIFICATIONARRIVED);
                reciver.xiaomiNotificationArrived(context,xiaomiArrived);
                break;
//            case
        }
        Bundle bundle = intent.getExtras();

    }

    public void testbaiduOnbind(String msg) {
        System.out.print("");
    }


//    public void testbaiduOnbind(String responseString){
//
//    }

}
