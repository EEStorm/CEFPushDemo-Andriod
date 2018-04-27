package com.xiaomi.eestormtest;

import android.content.Context;
import android.content.Intent;

/**
 * Created by v-shbo on 2018/4/13.
 */

public class CEFDemoMessageReceiver extends  CEFPushMessageReciver {

    public void baiduOnbind(Context context, String responseString){

        System.out.print(responseString);

    }

    public void baiduOnMessage(Context context, String responseString){

    }

    public void baiduOnNotificationArrived(Context context, String responseString){

    }

    public void baiduOnUnbind(Context context, String responseString){

    }

    public void xiaomiThroughMessage(Context context, String responseString){

    }

    public void xiaomiNotificationClicked(Context context, String responseString){

    }
    public void xiaomiNotificationArrived(Context context, String responseString){

    }

}
