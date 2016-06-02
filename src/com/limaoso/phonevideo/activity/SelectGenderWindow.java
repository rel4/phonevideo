package com.limaoso.phonevideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.UiThreadTest;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.MyDataEditBean;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/*
 * 
 author 院彩华
 弹出性别选择菜单

 */
public class SelectGenderWindow extends Activity implements OnClickListener {

	private HttpHelp httpHelp;

	private TextView btn_selectgender_male;// 男
	private TextView btn_selectgender_female;// 女
	private TextView btn_selectgender_secrecy;// 保密
	private TextView btn_selectgender_cancel;// 取消

	private LinearLayout pop_selectgender_layout;
	private Intent myData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectgender_dialog);
		myData = getIntent();
		httpHelp = new HttpHelp();
		init();// 初始化控件
		setListener();// 设置监听器
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

	private void setListener() {
		pop_selectgender_layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
						Toast.LENGTH_SHORT).show();
			}
		});
		// 添加按钮监听
		btn_selectgender_male.setOnClickListener(this);
		btn_selectgender_female.setOnClickListener(this);
		btn_selectgender_cancel.setOnClickListener(this);
		btn_selectgender_secrecy.setOnClickListener(this);
	}

	private void init() {
		btn_selectgender_male = (TextView) this
				.findViewById(R.id.btn_selectgender_male);
		btn_selectgender_female = (TextView) this
				.findViewById(R.id.btn_selectgender_female);

		btn_selectgender_cancel = (TextView) this
				.findViewById(R.id.btn_selectgender_cancel);

		btn_selectgender_secrecy = (TextView) findViewById(R.id.btn_selectgender_secrecy);
		pop_selectgender_layout = (LinearLayout) findViewById(R.id.pop_selectgender_layout);
		// 添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
	}

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.btn_selectgender_male:
			if (!"男".equals(myData.getExtras().get("mydata"))) {
				httpHelp.sendGet(NetworkConfig.setSex(1), MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {

							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (1 == Integer.parseInt(bean.status)) {
									UIUtils.showToast(SelectGenderWindow.this,
											"修改成功 ");
								} else {
									UIUtils.showToast(SelectGenderWindow.this,
											"修改失败");
								}
							}
						});
				i.putExtra("mydata", "男");
				setResult(3, i);
			}
			finish();
			break;
		case R.id.btn_selectgender_female:
			if (!"女".equals(myData.getExtras().get("mydata"))) {
				httpHelp.sendGet(NetworkConfig.setSex(0), MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {

							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (1 == Integer.parseInt(bean.status)) {
									UIUtils.showToast(SelectGenderWindow.this,
											"修改成功 ");
								} else {
									UIUtils.showToast(SelectGenderWindow.this,
											"修改失败");
								}
							}
						});
				i.putExtra("mydata", "女");
				setResult(3, i);
			}
			finish();
			break;
		case R.id.btn_selectgender_secrecy:
			if (!"保密".equals(myData.getExtras().get("mydata"))) {
				httpHelp.sendGet(NetworkConfig.setSex(2), MyDataEditBean.class,
						new MyRequestCallBack<MyDataEditBean>() {

							@Override
							public void onSucceed(MyDataEditBean bean) {
								if (1 == Integer.parseInt(bean.status)) {
									UIUtils.showToast(SelectGenderWindow.this,
											"修改成功 ");
								} else {
									UIUtils.showToast(SelectGenderWindow.this,
											"修改失败");
								}
							}
						});
				i.putExtra("mydata", "保密");
				setResult(3, i);
			}
			finish();
			break;
		case R.id.btn_selectgender_cancel:
			finish();
			break;
		default:
			break;
		}

	}
}