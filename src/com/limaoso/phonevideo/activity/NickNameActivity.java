package com.limaoso.phonevideo.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
 修改昵称
 */
public class NickNameActivity extends BaseActivity implements OnClickListener {
	private HttpHelp httpHelp;
	private ImageButton ib_nickname_delete;// 重置文字
	private EditText et_nickname_name;// 昵称
	private Button ibtn_left_home_save;

	@Override
	protected void initView() {
		httpHelp = new HttpHelp();
		init();
		setListener();
	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.mydata_nickname);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void setListener() {
		ib_nickname_delete.setOnClickListener(this);
		ibtn_left_home_save.setOnClickListener(this);
	}

	@Override
	protected String getCurrentTitle() {
		// TODO Auto-generated method stub
		return "昵称";
	}

	private void init() {
		// TODO Auto-generated method stub
		ib_nickname_delete = (ImageButton) findViewById(R.id.ib_nickname_delete);
		et_nickname_name = (EditText) findViewById(R.id.et_nickname_name);
		et_nickname_name.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_NICKNAME, ""));
		et_nickname_name.setSelection(et_nickname_name.getText().toString()
				.length());
		ibtn_left_home_save = (Button) findViewById(R.id.ibtn_left_home_save);
		ibtn_left_home_save.setVisibility(View.VISIBLE);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.ib_nickname_delete:
			et_nickname_name.setText("");
			et_nickname_name.setHint("");
			break;
		case R.id.ibtn_left_home_save:
			save();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		save();
	}

	private void save() {
		Intent i = new Intent();

		String name = et_nickname_name.getText().toString();
		if (!CheckUtils.isNickName(name)) {
			UIUtils.showToast(UIUtils.getContext(), "名字应为1~12个字母、数字、中文以内");
			return;
		}
		if ("".equals(et_nickname_name.getText().toString().trim())
				|| PrefUtils.getString(GlobalConstant.USER_NICKNAME, "")
						.equals(et_nickname_name.getText().toString().trim())) {
			finish();
		} else {
			httpHelp.sendGet(NetworkConfig.setNickName(et_nickname_name
					.getText().toString()), MyDataEditBean.class,
					new MyRequestCallBack<MyDataEditBean>() {

						@Override
						public void onSucceed(MyDataEditBean bean) {
							// TODO Auto-generated method stub
							if (bean == null) {
								return;
							}
							UIUtils.showToast(UIUtils.getContext(), bean.msg);
							if ("1".equals(bean.status)) {
								PrefUtils.setString(UIUtils.getContext(),
										GlobalConstant.USER_NICKNAME,
										et_nickname_name.getText().toString());
								Intent intent = new Intent();
								intent.setAction("myDataUpdate");
								NickNameActivity.this.sendBroadcast(intent);
								finish();
							}
						}
					});
		}

	}

}
