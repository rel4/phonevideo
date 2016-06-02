package com.limaoso.phonevideo.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.InformNum;
import com.limaoso.phonevideo.bean.InitDataBean;
import com.limaoso.phonevideo.fragment.ContentFragment;
import com.limaoso.phonevideo.fragment.LeftMenuFragment;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.global.MyApplication;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.top.bean.ChannelBean;
import com.limaoso.phonevideo.top.bean.ChannelItem;
import com.limaoso.phonevideo.top.bean.ChannelManage;
import com.limaoso.phonevideo.top.bean.ChannelBean.ChannelCont.Channel;
import com.limaoso.phonevideo.top.db.SQLHelper;
import com.limaoso.phonevideo.update.UpdateManager;
import com.limaoso.phonevideo.utils.LogUtil;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

/**
 * 主页面
 * 
 * @author liangO
 * 
 */
public class MainActivity extends SlidingFragmentActivity {
	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";
	private HttpHelp httpHelp;
	private FragmentManager mFm;
	private FragmentTransaction mTransaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getInforNum();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyApplication.setMainActivity(this);
		setBehindContentView(R.layout.left_menu);// 设置侧边栏
		SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
		// int width = getWindowManager().getDefaultDisplay().getWidth();//
		// 获取屏幕宽度
		slidingMenu.setBehindOffset(150);// 设置预留屏幕的宽度
		slidingMenu.setFadeEnabled(false);
		slidingMenu.setBehindScrollScale(0.25f);
		slidingMenu.setFadeDegree(0.25f);
		// 配置背景图片
		slidingMenu.setBackgroundImage(R.drawable.zc_bj);
		// 设置专场动画效果
		slidingMenu
				.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
					@Override
					public void transformCanvas(Canvas canvas, float percentOpen) {
						float scale = (float) (percentOpen * 0.25 + 0.75);
						canvas.scale(scale, scale, -canvas.getWidth() / 2,
								canvas.getHeight() / 2);
					}
				});

		slidingMenu
				.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
					@Override
					public void transformCanvas(Canvas canvas, float percentOpen) {
						float scale = (float) (1 - percentOpen * 0.25);
						canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
					}
				});

		initFragment();
		// UmengUpdateAgent.setDefault();
		// // 请在调用update,forceUpdate函数之前设置推广id，silentUpdate不支持此功能
		// UmengUpdateAgent.setSlotId("54357");
		// UmengUpdateAgent.update(this);
		initPushAgent();// 友盟推送

		checkUpdate();
		initNetData();
	}

	private void initNetData() {
		getHttpHelp().sendGet(NetworkConfig.getUserIntrest(),
				InitDataBean.class, new MyRequestCallBack<InitDataBean>() {

					@Override
					public void onSucceed(InitDataBean bean) {
						if (bean == null || bean.cont == null) {
							return;
						}
						if ("1".equals(bean.status)) {
							if (!PrefUtils.getString(
									GlobalConstant.USER_TOP_DATA, "").equals(
									bean.cont.topTime)) {
								PrefUtils.setString(
										GlobalConstant.USER_TOP_DATA,
										bean.cont.topTime);
								getUserIntrest();
							}
						}
					}
				});
	}

	private HttpHelp getHttpHelp() {
		if (httpHelp == null) {
			httpHelp = new HttpHelp();
		}
		return httpHelp;
	}

	/**
	 * 获取栏目信息
	 */
	private void getUserIntrest() {
		getHttpHelp().sendGet(NetworkConfig.getUserIntrest(),
				ChannelBean.class, new MyRequestCallBack<ChannelBean>() {

					@Override
					public void onSucceed(ChannelBean bean) {
						if (bean == null || bean.cont == null) {
							return;
						}
						if ("1".equals(bean.status)) {
							ChannelManage manage = ChannelManage
									.getManage(new SQLHelper(
											getApplicationContext()));
							if (manage == null) {
								if (LogUtil.getDeBugState()) {
									throw new RuntimeException();
								} else {
									return;
								}
							}
							manage.deleteAllChannel();
							ChannelItem homechannelItem = new ChannelItem(-1,
									"精选", 0, 1);
							manage.addCache(homechannelItem);
							if (bean != null && bean.cont != null) {
								List<Channel> list = bean.cont.list;
								ArrayList<String> ids = new ArrayList<String>();
								for (Channel channel : list) {
									if (channel != null) {
										ids.add(channel.typeid + "");
									}
								}

								List<Channel> listall = bean.cont.listall;
								int UserCount = 1;
								int otherCount = 0;
								for (int i = 0; i < listall.size(); i++) {
									Channel channel = listall.get(i);
									if (ids.contains(channel.typeid + "")) {
										ChannelItem channelItem = new ChannelItem(
												channel.typeid,
												channel.typename, UserCount, 1);
										manage.addCache(channelItem);
										UserCount++;
									} else {
										ChannelItem channelItem = new ChannelItem(
												channel.typeid,
												channel.typename, otherCount, 0);
										manage.addCache(channelItem);
										otherCount++;
									}
								}

							}
						}
					}
				});
	}

	/**
	 * 初始化友盟推送
	 */
	private void initPushAgent() {
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		PushAgent.getInstance(this).onAppStart();// SDK的日志发送策略，不能保证一定可以统计到日活数据
		String device_token = UmengRegistrar.getRegistrationId(this);
		// Log.e("BBB", device_token);
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

	/**
	 * 检查版本
	 */
	public void checkUpdate() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				new UpdateManager(MainActivity.this).checkUpdate();
			}
		}, 1000);

	}

	/**
	 * 初始化fragment, 将fragment数据填充给布局文件
	 */
	private void initFragment() {
		mFm = getSupportFragmentManager();
		mTransaction = mFm.beginTransaction();
		mTransaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				FRAGMENT_LEFT_MENU);// 用fragment替换framelayout
		mTransaction.replace(R.id.fl_content, new ContentFragment(),
				FRAGMENT_CONTENT);
		mTransaction.commit();// 提交事务
		// Fragment leftMenuFragment = fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
	}

	/**
	 * 对外提供获取FragmentTransaction方法
	 * 
	 * @return
	 */
	public FragmentTransaction getTransaction() {
		return mTransaction;
	}

	// 获取侧边栏fragment
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm
				.findFragmentByTag(FRAGMENT_LEFT_MENU);

		return fragment;
	}

	// 获取主页面fragment
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(FRAGMENT_CONTENT);

		return fragment;
	}

	/**
	 * 设置监听手机物理返回键，实现连按两次退出应用
	 */
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			// 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
			if (System.currentTimeMillis() - exitTime > 2000) {
				UIUtils.showToast(UIUtils.getContext(), "再按一次退出程序");
				// 将系统当前的时间赋值给exitTime
				exitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.setData("");
	}

	public void showConflictDialog() {

	}

	/**
	 * 每次打开app的时时候获取消息通知的数量进行显示
	 */
	private void getInforNum() {
		new HttpHelp().sendGet(NetworkConfig.getInformNum(), InformNum.class,
				new MyRequestCallBack<InformNum>() {

					@Override
					public void onSucceed(InformNum bean) {
						if (bean == null) {
							return;
						}
						String value = bean.cont.reply_num + "##"
								+ bean.cont.system_num;
						PrefUtils.setString(MainActivity.this,
								GlobalConstant.INFORM_NUM, value);
					}
				});
	}

}
