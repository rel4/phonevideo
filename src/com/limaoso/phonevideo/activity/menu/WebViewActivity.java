package com.limaoso.phonevideo.activity.menu;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.utils.UIUtils;

public class WebViewActivity extends BaseActivity {
	private WebView wv;
	private String url;
	private Context context;
	private String title;

	@Override
	protected void initView() {
		
		initVIiew();
		startWebView(url);
	}
	@Override
	protected String getCurrentTitle() {
		if (!"".equals(title)) {
			return title;
		}
		return super.getCurrentTitle();
	}
	@Override
	protected View getRootView() {
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		if (!url.startsWith("http://")) {
			url = "http://" + url;
		}
		context = this;
		return UIUtils.inflate(R.layout.activity_webview);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void initVIiew() {
		wv = (WebView) findViewById(R.id.wv);
		// wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		WebSettings settings = wv.getSettings();
		settings.setJavaScriptEnabled(true);
		// wv.addJavascriptInterface(new Handler(), "handler");
		wv.requestFocusFromTouch();
		wv.setWebViewClient(new WebViewClient() {// 支持超链
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// Toast.makeText(WebViewActivity.this, "网页加载完成", 0).show();
				// s
				// view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
				super.onPageFinished(view, url);
			}
		});
		wv.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		wv.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				// TODO Auto-generated method stub
				// 构建一个Builder来显示网页中的对话框
				Builder builder = new Builder(context);
				builder.setTitle("警告");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// 点击确定按钮之后,继续执行网页中的操作
								result.confirm();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, final JsResult result) {
				// TODO Auto-generated method stub
				Builder builder = new Builder(context);
				builder.setTitle("提示 ：");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				builder.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.cancel();
							}
						});
				builder.setCancelable(false);
				builder.create();
				builder.show();
				return true;
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {// 加载进度
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
			}

		});

	}

	private void startWebView(String url) {
		if (url == null) {
			show("url为空");
			return;
		}

		wv.loadUrl(url);
	}

	public void show(String msg) {
		Toast.makeText(getApplicationContext(), msg, 0).show();
	}

	// 改写物理按键——返回的逻辑
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && wv != null) {
			if (wv.canGoBack()) {
				wv.goBack();// 返回上一页面
				return true;
			} else {
				// System.exit(0);// 退出程序
				setExitSwichLayout();
				isKeyDown=true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

//	class Handler {
//		@JavascriptInterface
//		public void show(String data) {
//			// show(data);
//			if (data.length() > 200) {
//				data = data.substring(0, 200);
//			}
//			showRtspConnectedDialog(data);
//		}
//	}

//	private void showRtspConnectedDialog(String html) {
//		if (html.length() > 200) {
//			html = html.substring(0, 200);
//		}
//		AlertDialog dialog = new AlertDialog.Builder(context).setMessage(html)
//				.setPositiveButton("ok", null).create();
//		dialog.getWindow()
//				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//		dialog.show();
//	}

}
