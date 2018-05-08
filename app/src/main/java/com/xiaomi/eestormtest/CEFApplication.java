package com.xiaomi.eestormtest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.eestorm.eeslibrary.Channel;
import com.eestorm.eeslibrary.ConfigurationSettings;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by v-shbo on 2018/1/31.
 */

public class CEFApplication extends Application {

    private static CEFApplication instant;

    public String regid ;

    public static CEFApplication getInstant(){
        return instant;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化push推送服务

        instant = this;
    }

}