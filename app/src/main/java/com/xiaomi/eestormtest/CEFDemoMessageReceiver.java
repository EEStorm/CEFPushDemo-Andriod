package com.xiaomi.eestormtest;

import android.content.Context;
import android.util.Log;

import com.eestorm.eeslibrary.CEFMessageReciverInterface;
import com.eestorm.eeslibrary.CEFPushMessageReciver;

/**
 * Created by v-shbo on 2018/4/13.
 */

public class CEFDemoMessageReceiver extends CEFPushMessageReciver implements CEFMessageReciverInterface {
    public static final String TAG = "CEFDemoMessageReceiver";

    @Override
    public void onMessage(Context context, String responseString) {
        Log.d(TAG, "onMessage: "+responseString);
    }

    @Override
    public void onNotificationArrived(Context context, String responseString) {
        Log.d(TAG, "onNotificationArrived: "+responseString);
    }

    @Override
    public void onNotificationClicked(Context context, String responseString) {
        Log.d(TAG, "onNotificationClicked: "+responseString);
    }
}
