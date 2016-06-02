package com.limaoso.phonevideo.top;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.top.fragment.HomeTVListFragment;
import com.limaoso.phonevideo.utils.LogUtil;

public class TopActivity extends FragmentActivity implements OnClickListener {
	private HttpHelp httpHelp;
	private FragmentManager fm;
	private HomeTVListFragment htv;
	private static final String TAG = "TopActivity";
	private FragmentManager cfm; // 子类的fragment管理器

	private TextView tv_title_title;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		findViewById(R.id.iv_title_backicon).setOnClickListener(this);
		tv_title_title = (TextView) findViewById(R.id.tv_title_title);
		tv_title_title.setText("小视频");
		// cfm = getChildFragmentManager();
		fm = getSupportFragmentManager();
		setView();
	};

	private void setView() {
		FragmentTransaction transaction = fm.beginTransaction();
		// if (htv != null) {
		// transaction.hide(htv);
		// }
		if (htv == null) {
			htv = new HomeTVListFragment(this);
		}
		transaction.replace(R.id.fragment_top, htv);
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 更新界面
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e(this, "onActivityResult执行了。。。。。。。。。。");
		switch (requestCode) {
		case GlobalConstant.ZERO:
			if (resultCode == GlobalConstant.ONE) {
				htv.setChangelView();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		FragmentManager fm = getSupportFragmentManager();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0
					|| index >= fm.getFragments().size()) {
				LogUtil.e(TAG,
						"Activity result fragment index out of range: 0x"
								+ Integer.toHexString(requestCode));
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
				LogUtil.e(TAG,
						"Activity result no fragment exists for index: 0x"
								+ Integer.toHexString(requestCode));
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
			return;
		}
	}

	/**
	 * 递归调用，对所有子Fragement生效
	 * 
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private void handleResult(Fragment frag, int requestCode, int resultCode,
			Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_title_backicon:
			finish();

			break;

		default:
			break;
		}
	}

}
