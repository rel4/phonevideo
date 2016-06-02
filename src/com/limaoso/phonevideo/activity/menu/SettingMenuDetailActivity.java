package com.limaoso.phonevideo.activity.menu;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.BaseActivity;
import com.limaoso.phonevideo.activity.MostHtmlActivity;
import com.limaoso.phonevideo.activity.PushNotificationActivity;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.MySwitch;
import com.limaoso.phonevideo.view.MySwitch.OnCheckedChangeListener;

/**
 * 菜单详情页-播放设置
 * 
 * @author Kevin
 * 
 */
public class SettingMenuDetailActivity extends BaseActivity implements
		OnClickListener {

	private MySwitch ms_setting_skip;
	private MySwitch ms_setting_continue;
	private MySwitch ms_setting_useweb;
	private MySwitch ms_setting_noticeweb;

	@Override
	protected View getRootView() {
		// TODO Auto-generated method stub
		return UIUtils.inflate(R.layout.mycenter_set);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void initMySwitch() {
		// 跳过头尾
		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MS_SETTING_SKIP, null) != null) {
			ms_setting_skip
					.setOpen(Integer.parseInt(PrefUtils.getString(
							UIUtils.getContext(),
							GlobalConstant.MS_SETTING_SKIP, null)));
		}
		// 连续播放
		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MS_SETTING_CONTINUE, null) != null) {
			ms_setting_continue.setOpen(Integer.parseInt(PrefUtils.getString(
					UIUtils.getContext(), GlobalConstant.MS_SETTING_CONTINUE,
					null)));
		}
		//
		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MS_SETTING_USEWEB, null) != null) {
			ms_setting_useweb.setOpen(Integer.parseInt(PrefUtils.getString(
					UIUtils.getContext(), GlobalConstant.MS_SETTING_USEWEB,
					null)));
		}

		if (PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.MS_SETTING_NOTICEWEB, null) != null) {
			ms_setting_noticeweb.setOpen(Integer.parseInt(PrefUtils.getString(
					UIUtils.getContext(), GlobalConstant.MS_SETTING_NOTICEWEB,
					null)));
		}
	}

	private void setListener() {

		ms_setting_skip.setOnChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(MySwitch mySwitch, boolean isOpen) {
				if (isOpen) {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_SKIP, "1");
				} else {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_SKIP, "0");
				}
			}
		});

		ms_setting_continue.setOnChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(MySwitch mySwitch, boolean isOpen) {
				if (isOpen) {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_CONTINUE, "1");
				} else {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_CONTINUE, "0");
				}
			}
		});

		ms_setting_useweb.setOnChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(MySwitch mySwitch, boolean isOpen) {
				if (isOpen) {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_USEWEB, "1");
				} else {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_USEWEB, "0");
				}
			}
		});

		ms_setting_noticeweb.setOnChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(MySwitch mySwitch, boolean isOpen) {
				if (isOpen) {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_NOTICEWEB, "1");
				} else {
					PrefUtils.setString(UIUtils.getContext(),
							GlobalConstant.MS_SETTING_NOTICEWEB, "0");
				}
			}
		});
	}

	protected void initView() {
		((TextView) findViewById(R.id.tv_title)).setText("设置");
		ms_setting_skip = (MySwitch) findViewById(R.id.ms_setting_skip);
		ms_setting_continue = (MySwitch) findViewById(R.id.ms_setting_continue);
		ms_setting_useweb = (MySwitch) findViewById(R.id.ms_setting_useweb);
		ms_setting_noticeweb = (MySwitch) findViewById(R.id.ms_setting_noticeweb);
		findViewById(R.id.myceterset_propelling_message).setOnClickListener(
				this);
		findViewById(R.id.myceterset_guideuser).setOnClickListener(this);
		findViewById(R.id.myceterset_aboutlimao).setOnClickListener(this);
		setListener();// 设置监听器
		initMySwitch();
	}

	@Override
	public void onClick(View view) {
		Intent settingIntent;
		switch (view.getId()) {

		case R.id.myceterset_propelling_message:
			settingIntent = new Intent(this, PushNotificationActivity.class);
			this.startActivity(settingIntent);
			break;
		case R.id.myceterset_guideuser:
			settingIntent = new Intent(this, MostHtmlActivity.class);
			settingIntent.putExtra("title", "新手指南");
			this.startActivity(settingIntent);
			break;
		case R.id.myceterset_aboutlimao:
			settingIntent = new Intent(this, MostHtmlActivity.class);
			settingIntent.putExtra("title", "关于狸猫");
			this.startActivity(settingIntent);
			break;

		}

	}

}
