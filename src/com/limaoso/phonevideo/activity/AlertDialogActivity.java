package com.limaoso.phonevideo.activity;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.R.layout;
import com.limaoso.phonevideo.activity.menu.PlayCacheAcivity;
import com.limaoso.phonevideo.utils.CommonUtils;
import com.limaoso.phonevideo.utils.LogUtil;
import com.limaoso.phonevideo.utils.UIUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AlertDialogActivity extends BaseActivity implements OnClickListener {
	private String TAG=this.getClass().getName();

	@Override
	protected int setActivityAnimaMode() {
		return 5;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_agree:
			LogUtil.e(TAG, "topActivity: "+CommonUtils.getTopActivity(UIUtils.getContext()));
			LogUtil.e(TAG, "AlertDialogActivity : "+PlayCacheAcivity.class.getName());
			if (PlayCacheAcivity.instance==null) {
				startActivity(new Intent(this, PlayCacheAcivity.class));
			}
			finish();
			break;
		case R.id.tv_not_agree:
			finish();
			break;
		}

	}

	@Override
	protected void initView() {
		rootView.findViewById(R.id.tv_agree).setOnClickListener(this);
		rootView.findViewById(R.id.tv_not_agree).setOnClickListener(this);
	}

	@Override
	protected View getRootView() {
		// TODO Auto-generated method stub
		return UIUtils.inflate(R.layout.activity_alert_dialog);
	}

}
