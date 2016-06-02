package com.limaoso.phonevideo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.http.RequestParams;
import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.bean.CommentBean;
import com.limaoso.phonevideo.bean.CommentSonBean;
import com.limaoso.phonevideo.bean.Son;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.PrefUtils;
import com.limaoso.phonevideo.utils.UIUtils;
import com.limaoso.phonevideo.view.CircleImageView;

/**
 * 评论界面
 * 
 * @author liang
 * 
 */
/**
 * @author jjm
 * 
 */
public class CommentActivity extends BaseActivity implements OnClickListener {
	private CommentBean commentBean;
	private HttpHelp httpHelp;
	private PullToRefreshListView ptr_comment_reponse;
	private CommentAdapter commentAdapter;
	private CircleImageView iv_comment_icon;
	private TextView tv_comment_name;
	private TextView tv_comment_time;
	private TextView tv_comment_reply;
	private TextView tv_comment_content;
	private TextView tv_title;
	private EditText et_comment_etComment;
	private Button btn_comment_SendComment;
	private static Integer commentPosition;
	private static Integer state = 0;// 判断是回复主评论还是子评论 0子评论1主评论
	private ImageView iv_comment_dialogue;
	private String mCommentId; // 评论影片的id

	@Override
	protected void initView() {
		httpHelp = new HttpHelp();
		getFilmId();
		init();
		netWork();
		setViewListener();
		setLVListener();

	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	@Override
	protected View getRootView() {
		// TODO Auto-generated method stub
		return UIUtils.inflate(R.layout.activity_comment);
	}

	/**
	 * 获取消息通知页面的点击条目后的评论id
	 */
	private void getFilmId() {
		Intent intent = getIntent();
		mCommentId = intent.getStringExtra(GlobalConstant.FILM_COMMENT_ID);
	}

	private void setViewListener() {
		btn_comment_SendComment.setOnClickListener(this);
		iv_comment_dialogue.setOnClickListener(this);
	}

	private void setLVListener() {
		ptr_comment_reponse
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String url = "";
						if (commentBean == null || commentBean.cont == null
								|| commentBean.cont.son == null
								|| commentBean.cont.son.size() <= 0) {
							url = commentBean.url.up + "&currentid=0";
						} else {
							url = commentBean.url.up + "&currentid="
									+ commentBean.cont.son.get(0).id;
						}
						httpHelp.sendGet(url, CommentSonBean.class,
								new MyRequestCallBack<CommentSonBean>() {

									@Override
									public void onSucceed(CommentSonBean bean) {
										if (bean == null || bean.cont == null
												|| bean.cont.size() <= 0) {
											UIUtils.showToast(
													UIUtils.getContext(),
													"没有更多了");
											ptr_comment_reponse
													.onRefreshComplete();
											return;
										}
										if (commentBean == null
												|| commentBean.cont == null
												|| commentBean.cont.son == null
												|| commentBean.cont.son.size() <= 0) {
										} else {
											commentBean.cont.son.addAll(0,
													bean.cont);
											commentAdapter
													.notifyDataSetChanged();
										}
										ptr_comment_reponse.onRefreshComplete();

									}
								});
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String url = "";
						if (commentBean.cont.son.size() <= 0) {
							url = commentBean.url.down + "&currentid=0";
						} else {
							url = commentBean.url.down
									+ "&currentid="
									+ commentBean.cont.son
											.get(commentBean.cont.son.size() - 1).id;
						}
						httpHelp.sendGet(url, CommentSonBean.class,
								new MyRequestCallBack<CommentSonBean>() {

									@Override
									public void onSucceed(CommentSonBean bean) {
										if (bean == null || bean.cont == null
												|| bean.cont.size() <= 0) {
											UIUtils.showToast(
													UIUtils.getContext(),
													"没有更多了");
											ptr_comment_reponse
													.onRefreshComplete();
											return;
										}
										commentBean.cont.son.addAll(bean.cont);
										commentAdapter.notifyDataSetChanged();
										ptr_comment_reponse.onRefreshComplete();
									}
								});

					}
				});
	}

	private void netWork() {
		httpHelp.sendGet(NetworkConfig.getComment(mCommentId),
				CommentBean.class, new MyRequestCallBack<CommentBean>() {

					@Override
					public void onSucceed(CommentBean bean) {
						if (bean == null)
							return;
						tv_comment_name.setText(bean.cont.parent.nickname);
						tv_comment_time.setText(bean.cont.parent.time);
						tv_comment_reply.setText(": "
								+ bean.cont.parent.replynum + " )");
						tv_comment_content.setText(bean.cont.parent.msg);
						tv_title.setText(bean.cont.title);
						commentBean = bean;
						httpHelp.showImage(iv_comment_icon,
								bean.cont.parent.face + "##");
						setLVAdapter();
					}

				});
	}

	private void setLVAdapter() {
		commentAdapter = new CommentAdapter();
		ptr_comment_reponse.setMode(Mode.BOTH);
		ptr_comment_reponse.setAdapter(commentAdapter);
		ptr_comment_reponse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				commentPosition = position;
				state = 0;
				// showView(v);
			}
		});
	}

	/**
	 * 初始视图
	 */
	private void init() {
		ptr_comment_reponse = (PullToRefreshListView) findViewById(R.id.ptr_comment_reponse);
		ptr_comment_reponse.setMode(Mode.BOTH);
		iv_comment_icon = (CircleImageView) findViewById(R.id.iv_comment_icon);
		tv_comment_name = (TextView) findViewById(R.id.tv_comment_name);
		tv_comment_time = (TextView) findViewById(R.id.tv_comment_time);
		tv_comment_reply = (TextView) findViewById(R.id.tv_comment_reply);
		tv_comment_content = (TextView) findViewById(R.id.tv_comment_content);
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_comment_etComment = (EditText) findViewById(R.id.et_comment_etComment);
		btn_comment_SendComment = (Button) findViewById(R.id.btn_comment_SendComment);
		iv_comment_dialogue = (ImageView) findViewById(R.id.iv_comment_dialogue);

	}

	class CommentAdapter extends BaseAdapter {
		private Holder holder;

		@Override
		public int getCount() {
			if (commentBean == null || commentBean.cont == null
					|| commentBean.cont.son == null) {
				return 0;
			}
			return commentBean.cont.son.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView != null) {
				holder = (Holder) convertView.getTag();
			} else {
				convertView = UIUtils.inflate(R.layout.item_reply);
				holder = new Holder();
				holder.tv_item_content = (TextView) convertView
						.findViewById(R.id.tv_item_content);
				holder.tv_item_reply_time = (TextView) convertView
						.findViewById(R.id.tv_item_reply_time);
				holder.tv_item_reply_floor = (TextView) convertView
						.findViewById(R.id.tv_item_reply_floor);

				convertView.setTag(holder);

			}
			String name = commentBean.cont.son.get(position).nickname + " 回复  "
					+ commentBean.cont.parent.nickname + ":";
			String content = commentBean.cont.son.get(position).msg;
			SpannableString spanname1 = new SpannableString(name + content);

			spanname1.setSpan(new ForegroundColorSpan(R.color.bg_text_gray), 0,
					name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //
			spanname1.setSpan(
					new ForegroundColorSpan(Color.parseColor("#464545")),
					name.length(), name.length() + content.length(),
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.tv_item_content.setText(spanname1);
			holder.tv_item_reply_time.setText(commentBean.cont.son
					.get(position).time);
			holder.tv_item_reply_floor.setText(commentBean.cont.son
					.get(position).floor + "楼");
			return convertView;
		}
	}

	private void showView(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		et_comment_etComment.setVisibility(View.VISIBLE);
		btn_comment_SendComment.setVisibility(View.VISIBLE);
		et_comment_etComment.setFocusable(true);
		et_comment_etComment.requestFocus();

	}

	class Holder {
		public TextView tv_item_content;
		public TextView tv_item_reply_time;
		public TextView tv_item_reply_floor;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_comment_SendComment:
			String s = et_comment_etComment.getText().toString().trim();
			if ("".equals(s)) {
				UIUtils.showToast(UIUtils.getContext(), "请输入要发送的内容");
			} else {

				// Son son = new Son();
				// son.msg = s;
				// son.time = "刚刚";
				// son.id = commentBean.cont.parent.id;
				// son.nickname = "我";
				// if (state == 0) {
				// son.nickname2 =
				// commentBean.cont.son.get(commentPosition).nickname;
				// } else {
				// son.nickname2 = commentBean.cont.parent.nickname;
				// }
				//
				// commentBean.cont.son.add(0, son);
				// commentAdapter.notifyDataSetChanged();
				if (state == 0) {
					sendNet(commentBean.cont.son.get(commentPosition - 1).id);
				} else {
					sendNet(commentBean.cont.parent.id);
				}
			}
			break;
		case R.id.iv_comment_dialogue:
			state = 1;
			showView(v);
			break;
		default:
			break;
		}
	}

	private Son son;

	private void sendNet(String pid) {
		String Url = NetworkConfig.getReplyComment();
		if (Url == null) {
			return;
		}
		son = new Son();
		son.msg = et_comment_etComment.getText().toString().trim();
		son.nickname = PrefUtils.getString(GlobalConstant.USER_NICKNAME, "");
		// son.nickname2 = commentBean.cont.parent.nickname;
		RequestParams param = new RequestParams();
		param.addBodyParameter("topid", commentBean.cont.parent.id);
		param.addBodyParameter("pid", pid);
		param.addBodyParameter("ckuid", commentBean.cont.parent.aid);
		param.addBodyParameter("msg", et_comment_etComment.getText().toString()
				.trim());
		httpHelp.sendPost(Url, param, CommentBean.class,

		new MyRequestCallBack<CommentBean>() {

			@Override
			public void onSucceed(CommentBean bean) {
				if (bean == null) {
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						et_comment_etComment.getWindowToken(), 0);
				et_comment_etComment.setVisibility(View.GONE);
				et_comment_etComment.setText("");
				btn_comment_SendComment.setVisibility(View.GONE);
				if (Integer.parseInt(bean.status) == 1) {
					UIUtils.showToast(UIUtils.getContext(), "发送成功");
					commentBean.cont.son.add(0, son);
					commentAdapter.notifyDataSetChanged();

				} else {
					UIUtils.showToast(UIUtils.getContext(), "网络请求失败，请再发送一次");
				}
			}
		});

	}

}
