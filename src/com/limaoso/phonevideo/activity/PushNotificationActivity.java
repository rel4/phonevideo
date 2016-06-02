package com.limaoso.phonevideo.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.MySwitch;
import com.limaoso.phonevideo.view.MySwitch.OnCheckedChangeListener;

public class PushNotificationActivity extends BaseActivity implements
		OnClickListener {
	private TextView mTv_show_time; // 显示时间范围
	private MySwitch ms_push_message_notice;
	private MySwitch ms_push_system_notice;

	@Override
	protected void initView() {
		initTitle();
		init();
		setMySwitchListener();
		initCopoentData();
	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.push_notification);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void initCopoentData() {
		// TODO Auto-generated method stub
		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MS_PUSH_MESSAGE_NOTICE, null) != null) {
			ms_push_message_notice.setOpen(Integer.parseInt(PrefUtils
					.getString(UIUtils.getContext(),
							GlobalConstant.MS_PUSH_MESSAGE_NOTICE, null)));
		}

		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MS_PUSH_SYSTEM_NOTICE, null) != null) {
			ms_push_system_notice.setOpen(Integer.parseInt(PrefUtils.getString(
					UIUtils.getContext(), GlobalConstant.MS_PUSH_SYSTEM_NOTICE,
					null)));
		}

		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MTV_SHOW_TIME, null) != null) {
			mTv_show_time.setText(PrefUtils.getString(UIUtils.getContext(),
					GlobalConstant.MTV_SHOW_TIME, null));

		}

	}

	private void setMySwitchListener() {
		// TODO Auto-generated method stub
		ms_push_message_notice
				.setOnChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(MySwitch mySwitch,
							boolean isOpen) {
						// TODO Auto-generated method stub
						if (isOpen) {
							PrefUtils.setString(UIUtils.getContext(),
									GlobalConstant.MS_PUSH_MESSAGE_NOTICE, "1");
						} else {
							PrefUtils.setString(UIUtils.getContext(),
									GlobalConstant.MS_PUSH_MESSAGE_NOTICE, "0");
						}
					}
				});

		ms_push_system_notice
				.setOnChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(MySwitch mySwitch,
							boolean isOpen) {
						// TODO Auto-generated method stub
						if (isOpen) {
							PrefUtils.setString(UIUtils.getContext(),
									GlobalConstant.MS_PUSH_SYSTEM_NOTICE, "1");
						} else {
							PrefUtils.setString(UIUtils.getContext(),
									GlobalConstant.MS_PUSH_SYSTEM_NOTICE, "0");
						}
					}
				});
	}

	private void initTitle() {
		findViewById(R.id.rl_get_notice_time).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText("推送通知");
	}

	private void init() {
		mTv_show_time = (TextView) findViewById(R.id.tv_show_time);
		ms_push_message_notice = (MySwitch) findViewById(R.id.ms_push_message_notice);
		ms_push_system_notice = (MySwitch) findViewById(R.id.ms_push_system_notice);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_get_notice_time:
			startActivityForResult(
					new Intent(this, SelectTimeFrameWindow.class), 0);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 4) {

			mTv_show_time.setText(data
					.getStringExtra(SelectTimeFrameWindow.MYTIME));
			PrefUtils.setString(UIUtils.getContext(),
					GlobalConstant.MTV_SHOW_TIME,
					data.getStringExtra(SelectTimeFrameWindow.MYTIME).trim());
		}
	}

}
