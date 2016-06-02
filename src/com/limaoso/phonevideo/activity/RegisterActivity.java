package com.limaoso.phonevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.UserBean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.PhoneInfoUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

/**
 * 注册界面，获取保存用户的基本信息，并且实现6秒钟后自动进入主界面
 * 
 * @author liang
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	private HttpHelp httpHelp;

	private TextView tv_timer;// 倒计时提示文字
	private TextView user_name; // 自动分配的用户账号
	private TextView pwd_name; // 自动分配的用户密码
	private TextView user_name_number;
	private TextView user_password_number;
	private TextView tv_home_page; // 进入首页按钮
	private TextView tv_setting_page; // 进入设置页面按钮

	private String username; // 用户账号
	private String userPwd; // 用户密码

	private PhoneInfoUtils phone;

	@Override
	protected void initView() {
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		user_name = (TextView) findViewById(R.id.user_name);
		pwd_name = (TextView) findViewById(R.id.pwd_name);
		tv_home_page = (TextView) findViewById(R.id.tv_home_page);
		tv_setting_page = (TextView) findViewById(R.id.tv_setting_page);

		user_name_number = (TextView) findViewById(R.id.user_name_number);
		user_password_number = (TextView) findViewById(R.id.user_password_number);
		// 保存用户的资料到SharedPreferences本地中

		// saveUserDataToPref();
		// username=PrefUtils.getString(UIUtils.getContext(),
		// "et_user_name", null);
		user_name_number.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_ID, "100000888"));
		user_password_number.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_PASSWORD, "123456"));
		tv_home_page.setOnClickListener(this);
		tv_setting_page.setOnClickListener(this);
		// 设置倒计时启动装置
		countdown();

	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.activity_register);
	}

	/**
	 * 保存用户的资料到SharedPreferences本地中
	 */
	private void saveUserDataToPref() {
		username = user_name.getText().toString().trim();
		userPwd = pwd_name.getText().toString().trim();
		httpHelp = new HttpHelp();
		httpHelp.sendGet(NetworkConfig.getUser(), UserBean.class,
				new MyRequestCallBack<UserBean>() {

					@Override
					public void onSucceed(UserBean bean) {
						if (bean == null) {
							return;
						}
						PrefUtils.setString(UIUtils.getContext(), "user_ucode",
								bean.cont.ucode);
						PrefUtils.setString(UIUtils.getContext(),
								"et_user_name", bean.cont.uid);
						PrefUtils.setString(UIUtils.getContext(),
								"et_user_pwd", bean.cont.password);

						httpHelp.downLoad(bean.cont.face,
								GlobalConstant.HEAD_ICON_PATH,
								new LoadRequestCallBack() {

									@Override
									public void onLoading(long total,
											long current, boolean isUploading) {
									}

									@Override
									public void onSucceed(ResponseInfo t) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										// TODO Auto-generated method stub

									}

								});
						PrefUtils.setString(UIUtils.getContext(),
								GlobalConstant.HEAD_ICON,
								GlobalConstant.HEAD_ICON_PATH);

					}
				});

	}

	/**
	 * 设置倒计时启动装置
	 */
	private void countdown() {
		new TimeCount(6000, 1000).start();
	}

	class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			// 参数依次为总时长,和计时的时间间隔
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			tv_timer.setText(String.valueOf((millisUntilFinished / 1000)));
		}

		@Override
		public void onFinish() {

			goHomePage();

		}

	}

	/**
	 * 跳转首页
	 */
	private void goHomePage() {
		startActivity(new Intent(UIUtils.getContext(), MainActivity.class));
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_home_page:
			goHomePage();
			break;
		case R.id.tv_setting_page:

			startActivity(new Intent(UIUtils.getContext(), MainActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

}
