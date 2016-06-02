package com.limaoso.phonevideo.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.MyDataEditBean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;

/*
 * author 院彩华
 绑定帐号
 */
public class AccountBindActivity extends BaseActivity implements
		OnClickListener {
	private HttpHelp httpHelp;
	private EditText ed_accountbind_bindphone;// 输入手机号
	private EditText ed_accountbind_bindmail;// 输入邮箱
	private TextView tv_accountbind_phonesend;// 发送手机验证码
	private TextView tv_accountbind_mailsend;// 发送邮箱验证吗
	private EditText et_accountbind_phonecheck;
	private EditText ed_accountbind_mailcheck;
	private TextView tv_accountbind_bindphone;
	private TextView tv_accountbind_bindmail;

	private TextView tv_bind_phone;
	private TextView tv_bind_mail;

	@Override
	protected void initView() {
		httpHelp = new HttpHelp();
		init();
		initData();
		setListener();

	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.accountbind);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void setListener() {
		tv_accountbind_phonesend.setOnClickListener(this);
		tv_accountbind_mailsend.setOnClickListener(this);
		et_accountbind_phonecheck.setOnClickListener(this);
		tv_bind_phone.setOnClickListener(this);
		tv_bind_mail.setOnClickListener(this);
	}

	@Override
	protected String getCurrentTitle() {
		return "帐号绑定";
	}

	private void init() {
		tv_accountbind_bindphone = (TextView) findViewById(R.id.tv_accountbind_bindphone);
		tv_accountbind_bindmail = (TextView) findViewById(R.id.tv_accountbind_bindmail);

		ed_accountbind_bindphone = (EditText) findViewById(R.id.ed_accountbind_bindphone);
		tv_accountbind_phonesend = (TextView) findViewById(R.id.tv_accountbind_phonesend);
		ed_accountbind_bindmail = (EditText) findViewById(R.id.ed_accountbind_bindmail);

		tv_accountbind_mailsend = (TextView) findViewById(R.id.tv_accountbind_mailsend);
		et_accountbind_phonecheck = (EditText) findViewById(R.id.et_accountbind_phonecheck);
		ed_accountbind_mailcheck = (EditText) findViewById(R.id.ed_accountbind_mailcheck);
		tv_bind_phone = (TextView) findViewById(R.id.tv_bind_phone);
		tv_bind_mail = (TextView) findViewById(R.id.tv_bind_mail);

	}

	private void initData() {
		if (!"".equals(PrefUtils.getString(GlobalConstant.USER_TEL_VERIFY, ""))
				&& Integer.parseInt(PrefUtils.getString(
						GlobalConstant.USER_TEL_VERIFY, "")) == 1) {
			if (CheckUtils.isMobile(PrefUtils.getString(
					GlobalConstant.USER_TEL, ""))) {
				ed_accountbind_bindphone.setText(PrefUtils.getString(
						GlobalConstant.USER_TEL, ""));
				tv_accountbind_bindphone.setText("已绑定手机号");
				tv_bind_phone.setText("修改绑定手机");
			} else {
				tv_bind_phone.setText("绑定手机");
				tv_accountbind_bindphone.setText("");
			}
		} else {
			tv_bind_phone.setText("绑定手机");
		}
		if (!"".equals(PrefUtils
				.getString(GlobalConstant.USER_EMAIL_VERIFY, ""))
				&& Integer.parseInt(PrefUtils.getString(
						GlobalConstant.USER_EMAIL_VERIFY, "")) == 1) {
			if (CheckUtils.isEmail(PrefUtils.getString(
					GlobalConstant.USER_EMAIL, ""))) {
				tv_accountbind_bindmail.setText("已绑定邮箱");
				ed_accountbind_bindmail.setText(PrefUtils.getString(
						GlobalConstant.USER_EMAIL, ""));
				tv_bind_mail.setText("修改绑定邮箱");
			} else {
				tv_bind_mail.setText("绑定邮箱");
				ed_accountbind_bindmail.setText("");
			}
		} else {
			tv_bind_mail.setText("绑定邮箱");
		}
	}

	@Override
	public void onClick(View view) {
		String email = ed_accountbind_bindmail.getText().toString();
		String phone = ed_accountbind_bindphone.getText().toString();
		switch (view.getId()) {
		case R.id.tv_accountbind_phonesend:
			if (phone.equals(PrefUtils.getString(GlobalConstant.USER_TEL, ""))) {
				UIUtils.showToast(AccountBindActivity.this, "该手机号码已被绑定");
				break;
			}
			if (CheckUtils.isMobile(phone)) {
				if (phoneTime == null) {
					phoneTime = new TimeCount(60000, 1000,
							tv_accountbind_phonesend);
				}
				phoneTime.start();
				httpHelp.sendGet(NetworkConfig.getPhoneCode(phone),
						MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {
							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (bean == null) {
									phoneTime.cancel();
									phoneTime.onFinish();
									return;
								}
								if (Integer.parseInt(bean.status) == 1) {
									UIUtils.showToast(AccountBindActivity.this,
											bean.msg);
								}

							}
						});

			} else {
				UIUtils.showToast(this, "请输入正确的手机号 ");
			}
			break;

		case R.id.tv_bind_phone:
			String phonecode = et_accountbind_phonecheck.getText().toString();
			if (CheckUtils.isMobile(phone)) {

				httpHelp.sendGet(NetworkConfig.getPhoneCheck(phone, phonecode),
						MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {

							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (bean == null) {
									UIUtils.showToast(AccountBindActivity.this,
											"网络请求失败，请再试一试");
									return;
								}
								UIUtils.showToast(AccountBindActivity.this,
										bean.msg);
								if ("1".equals(bean.status)) {
									phoneTime.cancel();
									phoneTime.onFinish();
									PrefUtils.setString(
											GlobalConstant.USER_TEL,
											ed_accountbind_bindphone.getText()
													.toString());
									PrefUtils
											.setString(
													GlobalConstant.USER_TEL_VERIFY,
													"1");
									et_accountbind_phonecheck.setText("");
									initData();
								}
							}
						});

			} else {
				UIUtils.showToast(this, "请输入正确的手机号 ");
			}
			break;
		case R.id.tv_accountbind_mailsend:
			if (CheckUtils.isEmail(email)) {
				if (email.equals(PrefUtils.getString(GlobalConstant.USER_EMAIL,
						""))) {
					UIUtils.showToast(AccountBindActivity.this, "该邮箱已被绑定");
					break;
				}
				if (emailTime == null) {
					emailTime = new TimeCount(60000, 1000,
							tv_accountbind_mailsend);
				}
				emailTime.start();
				httpHelp.sendGet(NetworkConfig.getMailCode(email),
						MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {

							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (bean == null) {
									emailTime.cancel();
									emailTime.onFinish();
									return;
								}
								if ("1".equals(bean.status)) {
									UIUtils.showToast(UIUtils.getContext(),
											bean.msg);
								}

							}
						});
			} else {
				UIUtils.showToast(this, "请输入邮箱号");
			}
			break;
		case R.id.tv_bind_mail:
			String mailcode = ed_accountbind_mailcheck.getText().toString()
					.trim();
			if (CheckUtils.isEmail(email)) {

				httpHelp.sendGet(NetworkConfig.getMailCheck(email, mailcode),
						MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {

							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (bean == null) {
									return;
								}
								UIUtils.showToast(AccountBindActivity.this,
										bean.msg);
								if ("1".equals(bean.status)) {
									tv_bind_mail.setText("修改绑定邮箱");
									emailTime.cancel();
									emailTime.onFinish();
									PrefUtils.setString(
											GlobalConstant.USER_EMAIL,
											ed_accountbind_bindmail.getText()
													.toString());
									PrefUtils.setString(
											GlobalConstant.USER_EMAIL_VERIFY,
											"1");
									ed_accountbind_mailcheck.setText("");
									initData();
								}

							}
						});
			} else {
				UIUtils.showToast(this, "请输入邮箱号");
			}
			break;
		default:
			break;
		}
	}

	private TimeCount phoneTime;
	private TimeCount emailTime;

	// /**
	// * 设置倒计时启动装置
	// */
	// private void countdown(TextView textView) {
	// new TimeCount(60000, 1000, textView, this).start();
	// }

	class TimeCount extends CountDownTimer {
		private TextView textView;

		public TimeCount(long millisInFuture, long countDownInterval,
				TextView textView) {
			// 参数依次为总时长,和计时的时间间隔
			super(millisInFuture, countDownInterval);
			this.textView = textView;
			this.textView.setFocusable(false);
			this.textView.setClickable(false);
		}

		@Override
		public void onTick(long millisUntilFinished) {

			textView.setText(String.valueOf((millisUntilFinished / 1000))
					+ "秒后重新发送");
		}

		@Override
		public void onFinish() {
			textView.setText("重新发送");
			textView.setFocusable(true);
			this.textView.setClickable(true);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
