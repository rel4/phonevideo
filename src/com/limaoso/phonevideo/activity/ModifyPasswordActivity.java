package com.limaoso.phonevideo.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 *    修改密码
 */
public class ModifyPasswordActivity extends BaseActivity implements
		OnClickListener {
	private HttpHelp httpHelp;
	private TextView tv_title;// 标题
	private EditText et_modifypassword_new1;
	private EditText et_modifypassword_new2;
	private EditText et_modifypassword_old;
	private TextView tv_modifypassword_submit;
	private TextView tv_modifypassword_reset;
	private static final String MYDATE = "mydata";
	private Intent mydata;
	private RelativeLayout rl_modifypassword_oldpassword;
	private View v_modifypassword_line;

	@Override
	protected void initView() {
		httpHelp = new HttpHelp();
		mydata = getIntent();
		init();// 初始化控件
		initdata();
		setListener();// 设置监听器
	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.modifypassword);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void initdata() {
		if (!"".equals(PrefUtils.getString(GlobalConstant.USER_ISEDITPWD, ""))
				&& Integer.parseInt(PrefUtils.getString(
						GlobalConstant.USER_ISEDITPWD, "")) == 0) {
			rl_modifypassword_oldpassword.setVisibility(View.GONE);
			v_modifypassword_line.setVisibility(View.GONE);
			et_modifypassword_old.setText("123456");
		} else {
			rl_modifypassword_oldpassword.setVisibility(View.VISIBLE);
			v_modifypassword_line.setVisibility(View.VISIBLE);
			et_modifypassword_old.setText("");
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		tv_modifypassword_submit.setOnClickListener(this);
		tv_modifypassword_reset.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("修改密码");

		et_modifypassword_old = (EditText) findViewById(R.id.et_modifypassword_old);
		et_modifypassword_new1 = (EditText) findViewById(R.id.et_modifypassword_new1);
		et_modifypassword_new2 = (EditText) findViewById(R.id.et_modifypassword_new2);

		tv_modifypassword_submit = (TextView) findViewById(R.id.tv_modifypassword_submit);
		tv_modifypassword_reset = (TextView) findViewById(R.id.tv_modifypassword_reset);

		rl_modifypassword_oldpassword = (RelativeLayout) findViewById(R.id.rl_modifypassword_oldpassword);
		v_modifypassword_line = findViewById(R.id.v_modifypassword_line);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_modifypassword_submit:
			String oldpwd = et_modifypassword_old.getText().toString().trim();
			String newpwd1 = et_modifypassword_new1.getText().toString().trim();
			String newpwd2 = et_modifypassword_new2.getText().toString().trim();
			if (!newpwd1.equals(newpwd2)) {
				UIUtils.showToast(ModifyPasswordActivity.this, "新密码不一致");
			} else {
				if (!CheckUtils.isPassWord(oldpwd)) {
					UIUtils.showToast(UIUtils.getContext(), "请输入6-16位数字、字母的旧密码");
				} else if (!CheckUtils.isPassWord(newpwd1)
						|| !CheckUtils.isPassWord(newpwd2)) {
					UIUtils.showToast(UIUtils.getContext(), "请输入6-16位数字、字母的新密码");
				} else {
					httpHelp.sendGet(
							NetworkConfig.setPassword(oldpwd, newpwd1),
							MyDataEditBean.class,
							new MyRequestCallBack<MyDataEditBean>() {
								@Override
								public void onSucceed(MyDataEditBean bean) {
									if (bean == null || "0".equals(bean.status)) {
										return;
									}
									UIUtils.showToast(UIUtils.getContext(),
											bean.msg);
									if ("1".equals(bean.status)) {
										PrefUtils.setString(
												GlobalConstant.USER_ISEDITPWD,
												"1");
										initdata();
									}
								}
							});

				}
			}
			break;
		case R.id.tv_modifypassword_reset:
			et_modifypassword_old.setText("");
			et_modifypassword_new1.setText("");
			et_modifypassword_new2.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent();
		intent.setAction("myDataUpdate");
		ModifyPasswordActivity.this.sendBroadcast(intent);
	}

}
