package com.limaoso.phonevideo.activity;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.UserBean;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.global.MyApplication;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.LoadRequestCallBack;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.CheckUtils;
import com.limaoso.phonevideo.utils.IconUtils;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.SystemUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.CircleImageView;

/*
 author 院彩华
 我的资料
 */
public class MyDataActivity extends BaseActivity implements OnClickListener {
	private HttpHelp httpHelp;
	private UserBean myDataBean;

	private RelativeLayout rl_mydata_head;// 修改头像按钮
	private ImageButton ib_mydata_nickname;// 修改昵称
	private ImageButton ib_mydata_date;// 修改生日
	private ImageButton ib_mydata_bind;// 帐号绑定
	private ImageButton ib_mydata_password;// 修改密码
	private TextView tv_mydata_quit;// 退出登录

	private CircleImageView ib_mydata_headicon;// 头像图片

	private TextView tv_mydata_birthday;// 显示生日
	private TextView tv_mydata_nickname;// 显示昵称
	private TextView tv_mydata_loginname;

	private static final int HEADICON = 1;
	private static final int NICKNAME = 2;
	private static final int GENDER = 3;
	private static final int DATE = 4;
	private static final int ACCOUNTBIND = 5;
	private static final int MODIFYPASSWORD = 6;
	// private static String finalpath = "";
	private boolean bynet = false;
	private boolean headIcon = false;

	@Override
	protected void initView() {
		update();
		init();// 初始化控件
	}

	@Override
	protected View getRootView() {
		return UIUtils.inflate(R.layout.mydata);
	}

	private static final String MYDATE = "mydata";
	File headiconfile;// 存放压缩头像的文件夹

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected String getCurrentTitle() {
		return "我的资料";
	}

	private void initdata() {
		if (CheckUtils.isIcon(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.HEAD_ICON, null))) {
			httpHelp.showImage(
					ib_mydata_headicon,
					PrefUtils.getString(UIUtils.getContext(),
							GlobalConstant.HEAD_ICON, null) + "##");
			headIcon = false;
		} else {
			headIcon = true;
		}
		if (!"".equals(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_NICKNAME, ""))) {
			tv_mydata_nickname.setText(PrefUtils.getString(
					UIUtils.getContext(), GlobalConstant.USER_NICKNAME, ""));
		} else {
			bynet = true;
		}
		if (!"".equals(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_BIRTHDAY, ""))) {
			tv_mydata_birthday.setText(PrefUtils.getString(
					UIUtils.getContext(), GlobalConstant.USER_BIRTHDAY, ""));
		} else {
			bynet = true;
		}
	}

	private void initMyNet() {
		httpHelp.sendGet(NetworkConfig.getMyData("mydataActivity"),
				UserBean.class, new MyRequestCallBack<UserBean>() {

					@Override
					public void onSucceed(UserBean bean) {
						if (bean == null) {
							return;
						}
						if ("1".equals(bean.status)) {
							if (headIcon) {
								httpHelp.showImage(ib_mydata_headicon,
										bean.cont.face + "##");
							}
							saveUserData(bean);
							initdata();
						}
					}
				});
	}

	protected void saveUserData(UserBean bean) {
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_UCODE,
				bean.cont.ucode);
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_NICKNAME,
				bean.cont.nickname);
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_ID,
				bean.cont.id);
		PrefUtils.setString(UIUtils.getContext(),
				GlobalConstant.USER_ISEDITPWD, bean.cont.iseditpwd);
		if (bean.cont.birth != null && CheckUtils.isYMD(bean.cont.birth)) {
			PrefUtils.setString(UIUtils.getContext(),
					GlobalConstant.USER_BIRTHDAY, bean.cont.birth);
		}
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.USER_SCORE,
				bean.cont.score);
		PrefUtils.setString(UIUtils.getContext(), GlobalConstant.HEAD_ICON,
				GlobalConstant.HEAD_ICON_PATH);
		if (headIcon) {
			httpHelp.downLoad(bean.cont.face, GlobalConstant.HEAD_ICON_PATH,
					new LoadRequestCallBack() {

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
						}

						@Override
						public void onSucceed(ResponseInfo t) {
							PrefUtils.setString(GlobalConstant.HEAD_ICON,
									GlobalConstant.HEAD_ICON_PATH);
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

					});
		}
	}

	private void setListener() {
		rl_mydata_head.setOnClickListener(this);
		ib_mydata_nickname.setOnClickListener(this);
		ib_mydata_date.setOnClickListener(this);
		ib_mydata_bind.setOnClickListener(this);
		ib_mydata_password.setOnClickListener(this);
		tv_mydata_quit.setOnClickListener(this);
	}

	private void init() {
		httpHelp = new HttpHelp();

		rl_mydata_head = (RelativeLayout) findViewById(R.id.rl_mydata_head);
		ib_mydata_nickname = (ImageButton) findViewById(R.id.ib_mydata_nickname);
		ib_mydata_date = (ImageButton) findViewById(R.id.ib_mydata_date);
		ib_mydata_bind = (ImageButton) findViewById(R.id.ib_mydata_bind);
		ib_mydata_password = (ImageButton) findViewById(R.id.ib_mydata_password);

		ib_mydata_headicon = (CircleImageView) findViewById(R.id.ib_mydata_headicon);

		tv_mydata_birthday = (TextView) findViewById(R.id.tv_mydata_birthday);
		tv_mydata_nickname = (TextView) findViewById(R.id.tv_mydata_nickname);
		tv_mydata_loginname = (TextView) findViewById(R.id.tv_mydata_loginname);
		tv_mydata_quit = (TextView) findViewById(R.id.tv_mydata_quit);

		if (SystemUtils.getSystemVersion() >= SystemUtils.V4_0) {
			ib_mydata_headicon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		tv_mydata_loginname.setText(PrefUtils.getString(UIUtils.getContext(),
				GlobalConstant.USER_NICKNAME, "100000888"));
		initdata();
		setListener();// 设置控件的监听器
		if (bynet || headIcon) {
			initMyNet();
		}
		// initMyNet();

	}

	@Override
	public void onClick(View view) {
		Intent myDataIntent;
		switch (view.getId()) {
		case R.id.rl_mydata_head:
			// 弹出头像的菜单
			myDataIntent = new Intent(MyDataActivity.this,
					SelectPicPopupWindow.class);
			startActivityForResult(myDataIntent, HEADICON);

			break;
		case R.id.ib_mydata_nickname:
			// 跳转到修改昵称页面
			myDataIntent = new Intent(MyDataActivity.this,
					NickNameActivity.class);
			startActivityForResult(myDataIntent, NICKNAME);
			break;

		case R.id.ib_mydata_bind:
			// 跳转到合并帐号页面
			myDataIntent = new Intent(MyDataActivity.this,
					AccountBindActivity.class);
			startActivityForResult(myDataIntent, ACCOUNTBIND);
			break;
		case R.id.ib_mydata_date:
			// 跳转到生日选择页面
			myDataIntent = new Intent(MyDataActivity.this,
					BirthdayActivity.class);
			startActivityForResult(myDataIntent, DATE);
			break;
		case R.id.ib_mydata_password:
			myDataIntent = new Intent(MyDataActivity.this,
					ModifyPasswordActivity.class);
			startActivityForResult(myDataIntent, MODIFYPASSWORD);
			break;
		case R.id.tv_mydata_quit:
			PrefUtils.clearSP(UIUtils.getContext());
			MyApplication.getMainActivity().finish();
			myDataIntent = new Intent(MyDataActivity.this,
					QuitLoginActivity.class);
			startActivity(myDataIntent);
			finish();
			break;
		default:
			break;
		}

	}

	private String iconPath;

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (HEADICON == resultCode) {
			if (intent.getStringExtra("mydata") != null) {
				iconPath = intent.getStringExtra("mydata");
			} else {
				iconPath = GlobalConstant.HEAD_ICON_PATH;
			}
			httpHelp.showImage(ib_mydata_headicon, iconPath + "##");

			RequestParams params = new RequestParams();
			// headiconfile = new File(GlobalConstant.HEAD_ICON_PATH);
			headiconfile = new File(iconPath);
			params.addBodyParameter("uploadedfile", headiconfile);
			httpHelp.sendPost(NetworkConfig.setHeadIcon(), params,
					UserBean.class, new MyRequestCallBack<UserBean>() {

						@Override
						public void onSucceed(UserBean bean) {
							if (bean == null) {
								return;
							}
							if ("1".equals(bean.status)) {
								if (IconUtils.changeBitmap(
										GlobalConstant.HEAD_ICON_SAVEPATH,
										GlobalConstant.ROOT_DIR_NAME + ".jpg",
										iconPath)) {
									UIUtils.showToast(MyDataActivity.this,
											"上传成功");
								} else {
									UIUtils.showToast(MyDataActivity.this,
											"请检查您的网络");
								}
								// PrefUtils.setString(UIUtils.getContext(),
								// GlobalConstant.HEAD_ICON, iconPath);
							} else {
								UIUtils.showToast(MyDataActivity.this,
										"请检查您的网络");
							}
						}
					});
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		content.unregisterReceiver(receiver);
		Intent intent = new Intent();
		intent.setAction("myspaceUpdate");
		MyDataActivity.this.sendBroadcast(intent);
	}

	private Context content;
	private Receiver receiver;

	private void update() {
		if (content == null) {
			content = UIUtils.getContext();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction("myDataUpdate");
		receiver = new Receiver();
		content.registerReceiver(receiver, filter);
	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 看本地有没有存有头像，
			initdata();
		}

	}

}
