package com.limaoso.phonevideo.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.InformNum;
import com.limaoso.phonevideo.bean.UserBean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.IconCompress;
import com.limaoso.phonevideo.utils.IconUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.SDUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 闪屏界面
 * 
 * @author liang
 * 
 */
public class SplashActivity extends Activity {
	private HttpHelp httpHelp;
	private ImageView splash_backgroud;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		splash_backgroud = (ImageView) findViewById(R.id.splash_backgroud);
		if (PrefUtils.getBoolean(UIUtils.getContext(),
				GlobalConstant.IS_LOGINED, false)) {
			UIUtils.getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// 进入app判断是否有用户信息状态值
					goMainActivity();
				}
			}, 2000);
		} else {
			// splash_backgroud.setVisibility(View.GONE);
			initNet();
		}

	}

	private void initNet() {
		SDUtils.getRootFile(getApplicationContext());
		httpHelp = new HttpHelp();
		final Intent i = new Intent(UIUtils.getContext(), GuideActivity.class);
		httpHelp = new HttpHelp();
		String isLoginUrl = NetworkConfig.getUser();
		httpHelp.sendGet(isLoginUrl, UserBean.class,
				new MyRequestCallBack<UserBean>() {
					@Override
					public void onSucceed(UserBean bean) {
						if (bean == null) {
							finish();
							return;
						}
						// 记录获取用户信息状态
						if ("1".equals(bean.status)) {
							saveUserData(bean);
							PrefUtils.setBoolean(UIUtils.getContext(),
									GlobalConstant.IS_LOGINED, true);
							if (PrefUtils.getBoolean(UIUtils.getContext(),
									GlobalConstant.QUIT, false)) {
								PrefUtils.setBoolean(UIUtils.getContext(),
										GlobalConstant.QUIT, false);
								goMainActivity();
							} else {
								i.putExtra("type", bean.status);
								startActivity(i);
							}
						} else {
							saveUserData(bean);
							i.putExtra("type", bean.status);
							startActivity(i);
						}
						finish();
					}
				});

	}

	/**
	 * 进入mainActivity 的方法。
	 */
	private void goMainActivity() {
		startActivity(new Intent(UIUtils.getContext(), MainActivity.class));
		if (IconUtils.changeBitmap(GlobalConstant.HEAD_ICON_SAVEPATH,
				"limaohomeicon.jpg", GlobalConstant.HOMEPAGE_HEADICON2)) {
			PrefUtils.setString(GlobalConstant.BG_HOME_PAGE,
					GlobalConstant.HEAD_ICON_SAVEPATH + "limaohomeicon.jpg");
		}

		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this); // 统计时长
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	/**
	 * 保存用户信息
	 * 
	 * @param bean
	 */
	private void saveUserData(UserBean bean) {

		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_UCODE,
				bean.cont.ucode);
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_NICKNAME,
				bean.cont.nickname);
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_ID,
				bean.cont.uid);
		PrefUtils.setString(GlobalConstant.USER_PASSWORD, bean.cont.password);
		PrefUtils.setString(UIUtils.getContext(),
				GlobalConstant.USER_ISEDITPWD, bean.cont.iseditpwd);
		if (bean.cont.birth != null && CheckUtils.isYMD(bean.cont.birth)) {
			PrefUtils.setString(UIUtils.getContext(),
					GlobalConstant.USER_BIRTHDAY, bean.cont.birth);
		}
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_SCORE,
				bean.cont.score);

		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_TEL,
				bean.cont.tel);
		PrefUtils.setString(UIUtils.getContext(),
				GlobalConstant.USER_TEL_VERIFY, bean.cont.tel_verify);
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_EMAIL,
				bean.cont.email);
		PrefUtils.setString(UIUtils.getContext(),
				GlobalConstant.USER_EMAIL_VERIFY, bean.cont.email_verify);

		httpHelp.downLoad(bean.cont.face, GlobalConstant.HEAD_ICON_PATH,
				new LoadRequestCallBack() {

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSucceed(ResponseInfo t) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

				});
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.HEAD_ICON,
				GlobalConstant.HEAD_ICON_PATH);
	}
}
