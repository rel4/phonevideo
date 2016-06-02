package com.limaoso.phonevideo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.RetrievePasswordBean;
import com.limaoso.phonevideo.bean.UserBean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

@SuppressLint("JavascriptInterface")
public class RetrievePasswordActivity extends Activity implements
		OnClickListener {
	private TextView tv_title;
	private ImageButton ibtn_left_home_back;
	private WebView wv_webpage;
	private HttpHelp httpHelp;
	private RetrievePasswordBean retrievePasswordBean;
	private WebAppInterface webAppInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_page);
		init();
		initViewData();
		setListener();
		// initWebView();
		// setWebView();
		netWork();

	}

	private void netWork() {
		if (httpHelp == null)
			httpHelp = new HttpHelp();
		httpHelp.sendGet(NetworkConfig.getRetrievePassword(),
				RetrievePasswordBean.class,
				new MyRequestCallBack<RetrievePasswordBean>() {

					@Override
					public void onSucceed(RetrievePasswordBean bean) {
						if (bean == null) {
							return;
						}
						if ("1".equals(bean.status)) {
							retrievePasswordBean = bean;
							initWebView();
							setWebView();
						}
					}
				});
	}

	private void setWebView() {
		wv_webpage.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);// 使用当前WebView处理跳转
				return true;// true表示此事件在此处被处理，不需要再广播
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// 有页面跳转时被回调
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// 页面跳转结束后被回调
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// 出错
			}
		});
	}

	private void initWebView() {
		WebSettings webSettings = wv_webpage.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webAppInterface = new WebAppInterface();
		wv_webpage.addJavascriptInterface(webAppInterface, "Login");
		wv_webpage.loadUrl(retrievePasswordBean.url);
		// wv_webpage
		// .loadUrl("http://mmmapi.limaoso.com/mapi.php//HtmlVerify/retrievepassword");
	}

	private void setListener() {
		ibtn_left_home_back.setOnClickListener(this);
	}

	private void initViewData() {
		tv_title.setText("找回密码");
	}

	private void init() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		ibtn_left_home_back = (ImageButton) findViewById(R.id.ibtn_left_home_back);
		wv_webpage = (WebView) findViewById(R.id.wv_webpage);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ibtn_left_home_back:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 自定义的Android代码和JavaScript代码之间的桥梁类
	 * 
	 * @author 1
	 * 
	 */
	final class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface() {
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void getUcode(String ucode) {
			PrefUtils.setString(UIUtils.getContext(), "user_ucode", ucode);
			PrefUtils.setBoolean(UIUtils.getContext(),
					GlobalConstant.IS_LOGINED, true);
			startActivity(new Intent(RetrievePasswordActivity.this,
					MainActivity.class));
			finish();
		}

		public void showToast(String toast) {
			Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
		}
	}

}
