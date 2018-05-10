package com.xiaomi.eestormtest.pay.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
	public static final String APP_ID = "wxd930ea5d5a258f4f";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onResp(BaseResp resp) {
	//	Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
		//	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//	builder.setTitle(R.string.app_tip);
		//	builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
		//	builder.show();

			if (resp.errCode == 0){
				Toast.makeText(this,"支付成功",Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(this,"支付失败",Toast.LENGTH_LONG).show();
			}
			finish();
		}
	}

	@Override
	public void onReq(BaseReq baseReq) {

	}
}