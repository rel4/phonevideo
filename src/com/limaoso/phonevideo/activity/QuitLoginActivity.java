package com.limaoso.phonevideo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

public class QuitLoginActivity extends BaseActivity implements OnClickListener {
	private HttpHelp httpHelp;
	private UserBean usrBean;

	private EditText et_quitlogin_account;// 帐号
	private EditText et_quitlogin_password;// 密码

	private LinearLayout ll_quitlogin_password;
	private LinearLayout ll_quitlogin_account;

	private TextView tv_quitlogin_login;
	private TextView tv_quitlogin_retrieve;
	private TextView tv_quit_login_gohome;

	private TextView tv_quit_app;
	private TextView tv_quit_movie;
	private TextView tv_quit_recommerd;

	@Override
	protected void initView() {
		PrefUtils.setBoolean(UIUtils.getContext(), GlobalConstant.QUIT, true);
		httpHelp = new HttpHelp();
		init();
		setListener();
	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.quitlogin);
	}

	private void setListener() {
		et_quitlogin_account
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							ll_quitlogin_account.setVisibility(View.GONE);
						} else {
							if ("".equals(et_quitlogin_account.getText()
									.toString().trim())) {
								ll_quitlogin_account
										.setVisibility(View.VISIBLE);
							} else {
								ll_quitlogin_account.setVisibility(View.GONE);
							}
						}

					}
				});

		et_quitlogin_password
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {

						if (hasFocus) {
							ll_quitlogin_password.setVisibility(View.GONE);
						} else {
							if ("".equals(et_quitlogin_password.getText()
									.toString().trim())) {
								ll_quitlogin_password
										.setVisibility(View.VISIBLE);
							} else {
								ll_quitlogin_password.setVisibility(View.GONE);
							}
						}

					}
				});

		tv_quitlogin_login.setOnClickListener(this);
		tv_quitlogin_retrieve.setOnClickListener(this);
		tv_quit_login_gohome.setOnClickListener(this);

		tv_quit_app.setOnClickListener(this);
		tv_quit_movie.setOnClickListener(this);
		tv_quit_recommerd.setOnClickListener(this);
	}

	private void init() {
		et_quitlogin_account = (EditText) findViewById(R.id.et_quitlogin_account);
		et_quitlogin_password = (EditText) findViewById(R.id.et_quitlogin_password);

		ll_quitlogin_account = (LinearLayout) findViewById(R.id.ll_quitlogin_account);
		ll_quitlogin_password = (LinearLayout) findViewById(R.id.ll_quitlogin_password);

		tv_quitlogin_login = (TextView) findViewById(R.id.tv_quitlogin_login);
		tv_quitlogin_retrieve = (TextView) findViewById(R.id.tv_quitlogin_retrieve);
		tv_quit_login_gohome = (TextView) findViewById(R.id.tv_quit_login_gohome);

		tv_quit_app = (TextView) findViewById(R.id.tv_quit_app);
		tv_quit_movie = (TextView) findViewById(R.id.tv_quit_movie);
		tv_quit_recommerd = (TextView) findViewById(R.id.tv_quit_recommerd);

		if (et_quitlogin_account.hasFocus()) {
			ll_quitlogin_account.setVisibility(View.GONE);
		}
		if (et_quitlogin_password.hasFocus()) {
			ll_quitlogin_password.setVisibility(View.GONE);
		}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_quitlogin_login:
			String password = et_quitlogin_password.getText().toString().trim();
			String userid = et_quitlogin_account.getText().toString().trim();
			if ("".equals(password)) {
				UIUtils.showToast(this, "密码不能为空");
			} else if (!CheckUtils.isPassWord(password)) {
				UIUtils.showToast(this, "请输入6-16位数字、字母");
			} else {

				httpHelp.sendGet(NetworkConfig.getLogin(userid, password),
						UserBean.class, new MyRequestCallBack<UserBean>() {

							@Override
							public void onSucceed(UserBean bean) {
								if (bean == null) {
									UIUtils.showToast(UIUtils.getContext(),
											"登录失败");
									return;
								}
								UIUtils.showToast(UIUtils.getContext(),
										bean.msg);
								if ("1".equals(bean.status)) {
									saveUserData(bean);
									QuitLoginActivity.this
											.startActivity(new Intent(
													QuitLoginActivity.this,
													MainActivity.class));
									PrefUtils.setBoolean(UIUtils.getContext(),
											GlobalConstant.IS_LOGINED, true);
									PrefUtils.setBoolean(UIUtils.getContext(),
											GlobalConstant.QUIT, false);
									QuitLoginActivity.this
											.startActivity(new Intent(
													QuitLoginActivity.this,
													MainActivity.class));
									finish();
								}

							}

						});
			}

			break;
		case R.id.tv_quitlogin_retrieve:
			startActivity(new Intent(QuitLoginActivity.this,
					RetrievePasswordActivity.class));
			break;
		case R.id.tv_quit_login_gohome: // 进入首页
			startActivity(new Intent(QuitLoginActivity.this, MainActivity.class));
			finish();
			break;
		case R.id.tv_quit_movie: // 进入观影社区页面
			startActivity(new Intent(QuitLoginActivity.this, MainActivity.class));
			finish();
			break;
		case R.id.tv_quit_recommerd: // 进入必看推荐页面
			startActivity(new Intent(QuitLoginActivity.this, MainActivity.class));
			finish();
			break;
		case R.id.tv_quit_app: // 进入应用发现页面
			startActivity(new Intent(QuitLoginActivity.this, MainActivity.class));
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Intent intent = new Intent();
		intent.setAction("myspaceUpdate");
		QuitLoginActivity.this.sendBroadcast(intent);
	}

	private void initMainData() {

		httpHelp.sendGet(NetworkConfig.getUser(), UserBean.class,
				new MyRequestCallBack<UserBean>() {

					public void onSucceed(UserBean bean) {
						if (bean == null) {
							return;
						}
						if ("1".equals(bean.status)) {
							PrefUtils.setString(UIUtils.getContext(),
									"user_ucode", bean.cont.ucode);
							PrefUtils.setString(UIUtils.getContext(),
									"et_user_name", bean.cont.uid);
							PrefUtils.setString(UIUtils.getContext(),
									"et_user_pwd", bean.cont.password);

							httpHelp.downLoad(bean.cont.face,
									GlobalConstant.HEAD_ICON_PATH,
									new LoadRequestCallBack() {

										@Override
										public void onLoading(long total,
												long current,
												boolean isUploading) {

										}

										@Override
										public void onSucceed(ResponseInfo t) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(
												HttpException arg0, String arg1) {
											// TODO Auto-generated method stub

										}

									});
							PrefUtils.setString(UIUtils.getContext(),
									GlobalConstant.HEAD_ICON,
									GlobalConstant.HEAD_ICON_PATH);

						}
					}
				});
	}

	// 点击EditText以外的任何区域隐藏键盘
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				if (hideInputMethod(this, v)) {
					return true; // 隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	public static boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// v.getLocationInWindow(1);
			int left = leftTop[0], top = leftTop[1], bottom = top
					+ v.getHeight(), right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public static Boolean hideInputMethod(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
		return false;
	}

}
