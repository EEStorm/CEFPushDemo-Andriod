package com.eestorm.eeslibrary;

/**
 * Created by v-bachan on 2018/5/4.
 */

public interface HttpCallBackListener {
    void onFinish(String respose);

    void onError(Exception e);
}