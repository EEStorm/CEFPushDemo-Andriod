package com.xiaomi.eestormtest.pay.wx;

/**
 * Created by v-bachan on 2018/5/10.
 */

public class PayEntity {
    public String appId;
    public String eid;
    public String channel;
    public String subject;
    public String tradeNumber;
    public String amount;
    public String notifyUrl;



    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String toString() {
        return "{" +
                "appId='" + appId + '\'' +
                ", eid='" + eid + '\'' +
                ", channel='" + channel + '\'' +
                ", subject='" + subject + '\'' +
                ", tradeNumber='" + tradeNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
