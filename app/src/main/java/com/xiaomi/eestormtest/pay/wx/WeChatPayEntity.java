package com.xiaomi.eestormtest.pay.wx;

import com.google.gson.annotations.SerializedName;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by v-bachan on 2018/5/9.
 */

public class WeChatPayEntity {

    /**
     * appid : wxa186d3f0aa51c56e
     * partnerid : 1502289851
     * prepayid : wx10160932530044ece1f2f0f93537184889
     * package : Sign=WXPay
     * noncestr : 6cd7618274554bf49f17af3d230c7ac1
     * timestamp : 1525939772
     * sign : 9F5A1530B58275C0E45B139D36573415
     * orderId : 1e99e602-e042-450d-8543-ef0db5059e0e
     */
    public String appid;
    public String partnerid;
    public String prepayid;

    public String noncestr;
    public String timestamp;
    public String sign;
    public String orderId;

    @SerializedName("package")
    public String packageValue;
    public String extData;
}
