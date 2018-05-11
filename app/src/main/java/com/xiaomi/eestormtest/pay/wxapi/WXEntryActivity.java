package com.xiaomi.eestormtest.pay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaomi.eestormtest.R;
import com.xiaomi.mipush.sdk.Constants;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button gotoBtn, regBtn, launchBtn, checkBtn, scanBtn;
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.entry);
        
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this,"APP_ID", false);
		// 将该app注册到微信
		api.registerApp("APP_ID");


    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {

		
		Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();
		if(resp instanceof SendAuth.Resp){
			SendAuth.Resp newResp = (SendAuth.Resp) resp;
			//获取微信传回的code
			String code = newResp.code;
			Toast.makeText(this, code, Toast.LENGTH_LONG).show();
		}
		/**
		 * 拿到code之后，然后通过下面的接口获取到token和openid
		 * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		 * 拿到的数据如下所示
		 *
		 *
		 * {
		 "access_token":"ACCESS_TOKEN",
		 "expires_in":7200,
		 "refresh_token":"REFRESH_TOKEN",
		 "openid":"OPENID",
		 "scope":"SCOPE",
		 "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL"
		 }

		 * openid可以作为用户的唯一标识，将openid保存下来，就可以实现登录状态的检查了。
		 * 如果需要获取用户的信息，例如昵称，头像，可以使用下面的接口：
		 * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
		 * 得到的数据是：
		 *
		 * {
		 "openid":"OPENID",
		 "nickname":"NICKNAME",
		 "sex":1,
		 "province":"PROVINCE",
		 "city":"CITY",
		 "country":"COUNTRY",
		 "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
		 "privilege":[
		 "PRIVILEGE1",
		 "PRIVILEGE2"
		 ],
		 "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

		 }

		 更多数据：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317853&lang=zh_CN
		 */

	}
}