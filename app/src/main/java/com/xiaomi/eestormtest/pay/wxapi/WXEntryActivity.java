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
	
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.entry);
        
        // ͨ��WXAPIFactory��������ȡIWXAPI��ʵ��
    	api = WXAPIFactory.createWXAPI(this,"APP_ID", false);
		// ����appע�ᵽ΢��
		api.registerApp("APP_ID");


    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
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

	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {

		
		Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();
		if(resp instanceof SendAuth.Resp){
			SendAuth.Resp newResp = (SendAuth.Resp) resp;
			//��ȡ΢�Ŵ��ص�code
			String code = newResp.code;
			Toast.makeText(this, code, Toast.LENGTH_LONG).show();
		}
		/**
		 * �õ�code֮��Ȼ��ͨ������Ľӿڻ�ȡ��token��openid
		 * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		 * �õ�������������ʾ
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

		 * openid������Ϊ�û���Ψһ��ʶ����openid�����������Ϳ���ʵ�ֵ�¼״̬�ļ���ˡ�
		 * �����Ҫ��ȡ�û�����Ϣ�������ǳƣ�ͷ�񣬿���ʹ������Ľӿڣ�
		 * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
		 * �õ��������ǣ�
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

		 �������ݣ�https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317853&lang=zh_CN
		 */

	}
}