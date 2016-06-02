package com.limaoso.phonevideo.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.limaoso.phonevideo.R;
import com.limaoso.phonevideo.adapter.AppBaseAdapter;
import com.limaoso.phonevideo.bean.BaseBaen;
import com.limaoso.phonevideo.bean.SearchRseBean.Cont.Cfilm_area;
import com.limaoso.phonevideo.global.GlobalConstant;
import com.limaoso.phonevideo.network.config.NetworkConfig;
import com.limaoso.phonevideo.network.httphelp.HttpHelp;
import com.limaoso.phonevideo.network.httphelp.HttpInterface.MyRequestCallBack;
import com.limaoso.phonevideo.utils.StringNumUtils;
import com.limaoso.phonevideo.utils.UIUtils;

public class SelectPlayNumActivity extends BaseActivity implements
		OnItemClickListener {

	private int tv_all_num;
	private int k_id;// 库ID
	private GridView gv_tv_num;
	private int tvNum;// 剧集
	private HttpHelp httpHelp;
	private Cfilm_area cfilm_area;

	@Override
	protected View getRootView() {
		return getInitView();
	}

	@Override
	protected int setActivityAnimaMode() {
		return 4;
	}

	// @Override
	protected View getInitView() {
		cfilm_area = (Cfilm_area) getIntent().getSerializableExtra(
				GlobalConstant.TV_ID_AND_TVNUM);

		if (cfilm_area != null) {

			tv_all_num = (int) StringNumUtils.string2Num(cfilm_area.jujinum);
			k_id = (int) StringNumUtils.string2Num(cfilm_area.id);
		}
		return UIUtils.inflate(R.layout.activity_select_play_num);
	}

	// @Override
	protected void initView() {
		gv_tv_num = (GridView) findViewById(R.id.gv_tv_num);
		((TextView) findViewById(R.id.tv_title)).setText("选集");
		ItemAdapter itemAdapter = new ItemAdapter(tv_all_num);
		gv_tv_num.setAdapter(itemAdapter);
		gv_tv_num.setOnItemClickListener(this);
	}

	public void setItemAdapter(GridView view, int position) {

		// 选集条目点击事件
		// view.setOnItemClickListener(this);
	}

	private class ItemAdapter extends AppBaseAdapter {
		private int pagePosition;

		public ItemAdapter(int position) {
			super(position);
			this.pagePosition = position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderCross holder;
			if (convertView == null) {
				holder = new ViewHolderCross();
				convertView = View.inflate(SelectPlayNumActivity.this,
						R.layout.item_scroll_view, null);
				holder.setView(convertView);
			} else {
				holder = (ViewHolderCross) convertView.getTag();
			}
			holder.setPopuData(pagePosition, position);
			return convertView;
		}
	}

	private class ViewHolderCross {
		TextView tv_scroll;

		public void setView(View convertView) {
			tv_scroll = (TextView) convertView.findViewById(R.id.tv_scroll);
			convertView.setTag(this);
		}

		public void setPopuData(final int pagePosition, final int position) {
			tv_scroll.setText(position + 1 + "");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		if (httpHelp == null) {
			httpHelp = new HttpHelp();
		}

		httpHelp.sendGet(NetworkConfig.getReqPlay(k_id + "", (position + 1)),
				BaseBaen.class, new MyRequestCallBack<BaseBaen>() {

					@Override
					public void onSucceed(BaseBaen bean) {
						if (bean == null || "0".equals(bean.status)) {
							UIUtils.showToast(UIUtils.getContext(),
									UIUtils.getContext().getResources().getString(R.string.state_not_resource));
							return;
						}
						Intent intent = new Intent(SelectPlayNumActivity.this,
								PlaySourceActivity.class);
						intent.putExtra(GlobalConstant.TV_ID, cfilm_area.id
								+ GlobalConstant.FLAG_APP_SPLIT
								+ (position + 1));
						startActivity(intent);
					}
				});

	}

}
