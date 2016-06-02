package com.limaoso.phonevideo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.HtmlUrlBean;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.UIUtils;

public class MostHtmlActivity extends BaseActivity {
	private WebView wv_webpage;
	private TextView tv_title;
	private Intent Intent;
	private String htmlUrl;
	private HttpHelp httpHelp;
	private HtmlUrlBean htmlUrlBean;

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected void initView() {
		Intent = getIntent();
		init();
		netWork();

	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.web_page);
	}

	private void netWork() {
		httpHelp = new HttpHelp();
		httpHelp.sendGet(NetworkConfig.getHtmlUrl(), HtmlUrlBean.class,
				new MyRequestCallBack<HtmlUrlBean>() {

					@Override
					public void onSucceed(HtmlUrlBean bean) {
						if (bean == null) {
							return;
						}
						htmlUrlBean = bean;
						initTitle(bean);
						initWebView();
						setWebView();
					}
				});
	}

	private void initTitle(HtmlUrlBean bean) {
		if ("新手指南".equals(Intent.getExtras().get("title"))) {
			tv_title.setText("新手指南");
			htmlUrl = bean.userguide;
		}
		if ("关于狸猫".equals(Intent.getExtras().get("title"))) {
			tv_title.setText("关于狸猫");
			htmlUrl = bean.aboutlimao;
		}
	}

	private void init() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		wv_webpage = (WebView) findViewById(R.id.wv_webpage);
	}

	private void initWebView() {
		WebSettings webSettings = wv_webpage.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		wv_webpage.loadUrl(htmlUrl);
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

}
