package com.limaoso.phonevideo.activity;

import com.limaoso.phonevideo.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 author 王忠亮
 弹出通知时间选择菜单

 */
public class SelectTimeFrameWindow extends Activity implements OnClickListener {

	public static final String MYTIME = "myTime"; // 推送消息的时间范围tag
	private TextView btn_select_part_time;// 8点到22点
	private TextView btn_select_all_day;// 全天
	private TextView btn_select_time_cancel;// 取消

	private LinearLayout pop_select_time_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_time_scope_dialog);
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
		/*
		 * pop_select_time_layout.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { // TODO Auto-generated method stub
		 * Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
		 * Toast.LENGTH_SHORT).show(); } });
		 */
		// 添加按钮监听
		btn_select_part_time.setOnClickListener(this);
		btn_select_all_day.setOnClickListener(this);
		btn_select_time_cancel.setOnClickListener(this);
	}

	private void init() {
		btn_select_part_time = (TextView) this
				.findViewById(R.id.btn_select_part_time);
		btn_select_all_day = (TextView) this
				.findViewById(R.id.btn_select_all_day);

		btn_select_time_cancel = (TextView) this
				.findViewById(R.id.btn_select_time_cancel);

		pop_select_time_layout = (LinearLayout) findViewById(R.id.pop_select_time_layout);
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
		case R.id.btn_select_part_time:
			i.putExtra(MYTIME, "8：00-22：00");
			setResult(4, i);
			finish();
			break;
		case R.id.btn_select_all_day:
			i.putExtra(MYTIME, "全天");
			setResult(4, i);
			finish();
			break;

		case R.id.btn_select_time_cancel:
			finish();
			break;
		default:
			break;
		}

	}

}