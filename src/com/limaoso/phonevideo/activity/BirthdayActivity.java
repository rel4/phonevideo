package com.limaoso.phonevideo.activity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.limaoso.phonevideo.widget.wheelview.NumericWheelAdapter;
import com.limaoso.phonevideo.widget.wheelview.OnWheelScrollListener;
import com.limaoso.phonevideo.widget.wheelview.WheelView;

/*
 * author 院彩华
 * 生日的选项页面
 * */

public class BirthdayActivity extends BaseActivity implements OnClickListener {
	private HttpHelp httpHelp;
	private WheelView year;// 年
	private WheelView month;// 月
	private WheelView day;// 日
	private int mYear = 1996;
	private int mMonth = 0;
	private int mDay = 1;
	private String birthday;// 生日
	private TextView tv_date_birthday;// 显示生日
	private Intent myData;
	private Button ibtn_left_home_save;
	private boolean quit = false;

	@Override
	protected void initView() {
		httpHelp = new HttpHelp();
		myData = getIntent();
		init();
		setListener();
		getDataPick();

	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.date_scroll);
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	private void setListener() {
		// TODO Auto-generated method stub
		ibtn_left_home_save.setOnClickListener(this);
	}

	@Override
	protected String getCurrentTitle() {
		// TODO Auto-generated method stub
		return "出生日期";
	}

	private void init() {
		// TODO Auto-generated method stub
		ibtn_left_home_save = (Button) findViewById(R.id.ibtn_left_home_save);
		ibtn_left_home_save.setVisibility(View.VISIBLE);
		tv_date_birthday = (TextView) findViewById(R.id.tv_date_birthday);
		if (CheckUtils.isYMD(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_BIRTHDAY, ""))) {
			tv_date_birthday.setText(PrefUtils.getString(UIUtils.getContext(),
					GlobalConstant.USER_BIRTHDAY, ""));
		} else {
			tv_date_birthday.setText("1980-10-11");
		}
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	/**
	 */
	private void initDay(int arg1, int arg2) {
		day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
	}

	/**
	 * 此方法获取到最终的年龄值
	 * 
	 * @param birthday
	 * @return
	 */
	public static final String calculateDatePoor(String birthday) {
		try {
			if (TextUtils.isEmpty(birthday))
				return "0";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date birthdayDate = sdf.parse(birthday);
			String currTimeStr = sdf.format(new Date());
			Date currDate = sdf.parse(currTimeStr);
			if (birthdayDate.getTime() > currDate.getTime()) {
				return "0";
			}
			long age = (currDate.getTime() - birthdayDate.getTime())
					/ (24 * 60 * 60 * 1000) + 1;
			String year = new DecimalFormat("0.00").format(age / 365f);
			if (TextUtils.isEmpty(year))
				return "0";
			return String.valueOf(new Double(year).intValue());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "0";
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;//
			int n_month = month.getCurrentItem() + 1;//
			initDay(n_year, n_month);
			birthday = new StringBuilder()
					.append((year.getCurrentItem() + 1950))
					.append("-")
					.append((month.getCurrentItem() + 1) < 10 ? "0"
							+ (month.getCurrentItem() + 1) : (month
							.getCurrentItem() + 1))
					.append("-")
					.append(((day.getCurrentItem() + 1) < 10) ? "0"
							+ (day.getCurrentItem() + 1) : (day
							.getCurrentItem() + 1)).toString();
			tv_date_birthday.setText(birthday);

		}
	};

	private void getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);

		int curYear = mYear;
		int curMonth = mMonth + 1;
		int curDate = mDay;

		year = (WheelView) findViewById(R.id.year);
		year.setAdapter(new NumericWheelAdapter(1950, norYear));
		year.setLabel("年");
		year.setCyclic(true);
		year.addScrollingListener(scrollListener);

		month = (WheelView) findViewById(R.id.month);
		month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
		month.setLabel("月");
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) findViewById(R.id.day);
		initDay(curYear, curMonth);
		day.setLabel("日");
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);
		if (CheckUtils.isYMD(tv_date_birthday.getText().toString().trim())) {
			String[] date = tv_date_birthday.getText().toString().trim()
					.split("-");
			year.setCurrentItem(Integer.parseInt(date[0]) - 1950);
			month.setCurrentItem(Integer.parseInt(date[1]) - 1);
			day.setCurrentItem(Integer.parseInt(date[2]) - 1);
		} else {
			year.setCurrentItem((norYear - 1950) / 2);
			month.setCurrentItem(6);
			day.setCurrentItem(15);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ibtn_left_home_save:
			save();
			break;
		}

	}

	@Override
	protected void onStop() {
		save();
		super.onStop();
	}

	private void save() {
		Intent i = new Intent();

		if (PrefUtils.getString(GlobalConstant.USER_BIRTHDAY, "").equals(
				tv_date_birthday.getText().toString().trim())) {
			finish();

		} else {
			httpHelp.sendGet(NetworkConfig.setBirth(tv_date_birthday.getText()
					.toString()), MyDataEditBean.class,
					new MyRequestCallBack<MyDataEditBean>() {

						@Override
						public void onSucceed(MyDataEditBean bean) {
							if (bean == null) {
								return;
							}
							UIUtils.showToast(UIUtils.getContext(), bean.msg);
							if (1 == Integer.parseInt(bean.status)) {
								PrefUtils.setString(UIUtils.getContext(),
										GlobalConstant.USER_BIRTHDAY,
										tv_date_birthday.getText().toString()
												.trim());
								Intent intent = new Intent();
								intent.setAction("myDataUpdate");
								BirthdayActivity.this.sendBroadcast(intent);
								finish();
							}
						}
					});
		}
	}

}
