package com.xiaomi.eestormtest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by v-shbo on 2018/1/31.
 */

public class CEFApplication extends Application {

    private static CEFApplication instant;

    public static final String APP_ID = "2882303761517683551";
    public static final String APP_KEY = "5801768389551";
    public static final String TAG = "com.xiaomi.eestormtest";

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
    public void registerChannel(){
        if(shouldInit()) {
            if (ConfigurationSettings.channel == Channel.BAIDU){
                PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,  "eMS5zqIT0RbRbtDoZHAnze12"  );
            }else {
                MiPushClient.registerPush(this, APP_ID, APP_KEY);

                MiPushClient.setAlias(getApplicationContext(),"alias",null);
                MiPushClient.setUserAccount(getApplicationContext(),"userAccount", null);

            }

        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}