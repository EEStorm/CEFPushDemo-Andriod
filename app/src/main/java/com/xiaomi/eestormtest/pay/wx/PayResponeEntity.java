package com.xiaomi.eestormtest.pay.wx;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v-bachan on 2018/5/10.
 */

public class PayResponeEntity {

    /**
     * operationId : 00000000-0000-0000-0000-000000000000
     * status : null
     * properties : {"appid":"wxa186d3f0aa51c56e","partnerid":"1502289851","prepayid":"wx10160932530044ece1f2f0f93537184889","package":"Sign=WXPay","noncestr":"6cd7618274554bf49f17af3d230c7ac1","timestamp":"1525939772","sign":"9F5A1530B58275C0E45B139D36573415","orderId":"1e99e602-e042-450d-8543-ef0db5059e0e"}
     */

    private String operationId;
    private Object status;
    private WeChatPayEntity properties;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public WeChatPayEntity getProperties() {
        return properties;
    }

    public void setProperties(WeChatPayEntity properties) {
        this.properties = properties;
    }

}
