package com.eestorm.eeslibrary;

import android.content.Context;

/**
 * Created by v-bachan on 2018/5/7.
 */

public interface CEFMessageReciverInterface {

    public void onMessage(Context context, String responseString);

    public void onNotificationArrived(Context context, String responseString);

    public void onNotificationClicked(Context context, String responseString);
}
