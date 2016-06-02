package com.limaoso.phonevideo.base.impl.pager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.activity.APPDetailActivity;
import com.limaoso.phonevideo.activity.MainActivity;
import com.limaoso.phonevideo.base.BasePager;
import com.limaoso.phonevideo.bean.FindBean;
import com.limaoso.phonevideo.bean.Cont.AppCont;
import com.limaoso.phonevideo.db.findpage.FindDao;
import com.limaoso.phonevideo.db.findpage.FindInfo;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.global.MyApplication;
import com.limaoso.phonevideo.manager.DLAppManager;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.AppUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.RoundedImageView;

/**
 * 首页实现
 */
/**
 * 发现页实现
 * 
 * @author liang
 * 
 */
public class FindPager extends BasePager {
	private HttpHelp mHttpHelp;
	private FindBean findBean;
	private PullToRefreshListView lv;
	private int findPage = 2;
	private Adapter findAdapter;
	private View view;
	SimpleDateFormat simpleDF;

	List<FindInfo> listInfo;
	private int listPosition;// 查询listinfo中的数据，看看有没有想要的
	private FindDao findDao;
	private NotificationManager nm;
	private Notification notification;
	private Intent iDetail;

	public FindPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	public void initDB() {
		if (listInfo != null) {
			listInfo.clear();
		} else {
			listInfo = new ArrayList<FindInfo>();
		}
		listInfo.addAll(findDao.getAll());
		if (findAdapter != null) {
			findAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void initData() {
		simpleDF = new SimpleDateFormat("yyyyMMddHHmmss");
		super.initData();
		findDao = new FindDao();
		mHttpHelp = new HttpHelp();
		listInfo = new ArrayList<FindInfo>();
		iDetail = new Intent(mActivity, APPDetailActivity.class);
		DLAppManager.getInstance().setFindPager(this);
		initDB();
		inLoading();
		isOtherPager(1);
		setSlidingMenuEnable(true);
		initNet();
	}

	private void initNet() {
		mHttpHelp.sendGet(NetworkConfig.getFind(1), FindBean.class,
				new MyRequestCallBack<FindBean>() {

					@Override
					public void onSucceed(FindBean bean) {
						if (bean == null) {
							return;
						}
						findBean = bean;
						LoadingSuccess();
						initLV();// 初始化
						setListener();// 设置监听器
						// 去掉之前的View;
						flContent.removeAllViews();
						// 向FrameLayout中动态添加布局
						flContent.addView(lv);
					}
				});
	}

	@Override
	public String getCurrentTitle() {
		return "发现";
	}

	private void initLV() {
		view = View.inflate(UIUtils.getContext(), R.layout.findpager, null);
		// lv = new com.handmark.pulltorefresh.library.PullToRefreshListView(
		// MyApplication.getContext(), Mode.PULL_FROM_END);
		lv = (PullToRefreshListView) view.findViewById(R.id.ptr_findpager_show);
		findAdapter = new Adapter(MyApplication.getContext());
		lv.setAdapter(findAdapter);

	}

	private void setListener() {
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				iDetail.putExtra(GlobalConstant.DL_APP_ID,
						findBean.cont.get(position - 1).id);
				mActivity.startActivity(iDetail);
			}

		});
		lv.setMode(Mode.PULL_FROM_END);
		// 设置下拉刷新监听器
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
			// 下拉Pulling Down
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Void>() {

					// 先运行这个
					@Override
					protected Void doInBackground(Void... arg0) {
						// TODO Auto-generated method stub

						mHttpHelp.sendGet(NetworkConfig.getFind(findPage),
								FindBean.class,
								new MyRequestCallBack<FindBean>() {

									@Override
									public void onSucceed(FindBean bean) {

										if (bean == null) {
											UIUtils.showToast(
													UIUtils.getContext(),
													"没有更多数据");
											return;
										}
										if (bean.cont != null) {
											findPage++;
											findBean.cont.addAll(bean.cont);
											findAdapter.notifyDataSetChanged();
										} else {
											UIUtils.showToast(
													UIUtils.getContext(),
													"没有更多数据");
											return;
										}
									}
								});

						return null;
					}

					// 最后运行
					@Override
					protected void onPostExecute(Void result) {
						// TODO Auto-generated method stub\
						lv.onRefreshComplete();

					};
				}.execute();
			}
		});
	}

	class Adapter extends BaseAdapter {
		private Context context;
		private ViewHolder holder;

		// private List<DiscoverItem> data;

		public Context getContext() {
			return context;
		}

		/*
		 * public void addDiscoverItem(DiscoverItem item){ data.add(item);
		 * notifyDataSetChanged(); }
		 */
		public Adapter(Context context) {
			this.context = context;
			// data = new ArrayList<DiscoverItem>();
		}

		@Override
		public int getCount() {
			return findBean.cont.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			// ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getContext(), R.layout.discoverlist_item,
						null);
				holder = new ViewHolder();

				holder.iv_discoverlist_icon = (RoundedImageView) view
						.findViewById(R.id.iv_discoverlist_icon);
				holder.tv_discoverlist_name = (TextView) view
						.findViewById(R.id.tv_discoverlist_name);
				// holder.tv_discoverlist_introduce = (TextView) view
				// .findViewById(R.id.tv_discoverlist_introduce);
				holder.tv_discoverlist_number = (TextView) view
						.findViewById(R.id.tv_discoverlist_number);
				holder.tv_discoverlist_try = (TextView) view
						.findViewById(R.id.tv_discoverlist_try);
				view.setTag(holder);
			}

			holder.tv_discoverlist_name
					.setText(findBean.cont.get(position).name);
			// holder.tv_discoverlist_introduce.setText(findBean.cont
			// .get(position).mdesc);
			holder.tv_discoverlist_number
					.setText(findBean.cont.get(position).num);
			mHttpHelp.showImage(holder.iv_discoverlist_icon,
					findBean.cont.get(position).pic);

			listPosition = AppUtils.IsDownload(findBean.cont.get(position).id,
					listInfo);

			if (listPosition >= 0 && listInfo.get(listPosition).getState() == 2) {
				holder.tv_discoverlist_try.setClickable(true);
				holder.tv_discoverlist_try.setSelected(false);
				holder.tv_discoverlist_try.setText("打开");
				holder.tv_discoverlist_try.setOnClickListener(new MyOnclick(
						findBean.cont.get(position),
						holder.tv_discoverlist_try, 2, listInfo
								.get(listPosition)));

			} else if (AppUtils.IsDownload(findBean.cont.get(position).id,
					DLAppManager.getInstance().getFindInfo()) >= 0) {
				holder.tv_discoverlist_try.setClickable(false);
				holder.tv_discoverlist_try.setText("正在下载");
				holder.tv_discoverlist_try.setSelected(true);
			} else {
				holder.tv_discoverlist_try.setClickable(true);
				holder.tv_discoverlist_try.setSelected(false);
				holder.tv_discoverlist_try.setText("下载");
				holder.tv_discoverlist_try.setOnClickListener(new MyOnclick(
						findBean.cont.get(position),
						holder.tv_discoverlist_try, 0, null));
			}

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/*
		 * 显示在ListView中的控件
		 */

	}

	static class ViewHolder {
		RoundedImageView iv_discoverlist_icon;
		TextView tv_discoverlist_name;
		TextView tv_discoverlist_number;
		TextView tv_discoverlist_try;
		// TextView tv_discoverlist_introduce;
	}

	public void ShowDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage("确定下载该应用么?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	class MyOnclick implements OnClickListener {
		private int state;
		private String localUrl = "";
		private long name;
		private TextView textView;
		private FindInfo fInfo;
		private AppCont cont;

		public MyOnclick(AppCont cont, TextView textView, int state,
				FindInfo fInfo) {
			this.state = state;
			this.textView = textView;
			this.cont = cont;
			this.fInfo = fInfo;
		}

		@Override
		public void onClick(View v) {
			if (state == 2) {
				AppUtils.startAPP(fInfo);
			} else if (state == 1) {
			} else {
				DLAPP();
			}
		}

		private void DLAPP() {
			name = Long.parseLong((simpleDF.format(new Date()) + cont.id));
			localUrl = GlobalConstant.DL_APK_PATH + name + ".apk";
			fInfo = new FindInfo();
			fInfo.setAppid(cont.id);
			fInfo.setAppname(cont.name);
			fInfo.setApppackage(cont.pname);
			fInfo.setDownloadurl(cont.downloadurl);
			fInfo.setLocalurl(localUrl);
			fInfo.setState(1);
			listInfo.add(fInfo);
			state = 1;
			findDao.SaveDL(fInfo);
			DLAppManager.getInstance().addFindInfo(fInfo);
			if (!textView.isSelected()) {
				textView.setSelected(true);
				textView.setText("正在下载");
			}
		}

	}

	public void showNotification(int id) {
		nm.notify(id, notification);
	}

	private void showNotifi(String name) {
		nm = (NotificationManager) UIUtils.getContext().getSystemService(
				Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.ic_launcher, name + "正在下载",
				System.currentTimeMillis());
		notification.contentView = new RemoteViews(UIUtils.getContext()
				.getPackageName(), R.layout.up_notification);
		notification.flags = Notification.FLAG_ONGOING_EVENT
				| Notification.FLAG_AUTO_CANCEL;
		notification.icon = android.R.drawable.stat_sys_download;
		Intent notificationIntent = new Intent(UIUtils.getContext(),
				MainActivity.class);
	}
}
