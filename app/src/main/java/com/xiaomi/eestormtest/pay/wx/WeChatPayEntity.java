package com.xiaomi.eestormtest.pay.wx;

import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by v-bachan on 2018/5/9.
 */

public class WeChatPayEntity {
    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String packageValue;
    public String sign;
    public String extData;
}
