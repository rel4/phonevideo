package com.limaoso.phonevideo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.limaoso.phonevideo.R;

/**
 * 发现
 * @author 院彩华
 *
 */
public class Adapter extends BaseAdapter {
	private Context context;
	private ViewHolder holder;
	//private List<DiscoverItem> data;

	public Context getContext() {
		return context;
	}

/*	public void addDiscoverItem(DiscoverItem item){
		data.add(item);
		notifyDataSetChanged();
	}*/
	public Adapter(Context context) {
		this.context = context;
		//data = new ArrayList<DiscoverItem>();
	}

	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		//ViewHolder holder;
		if(convertView  != null){
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}else{
			view = View.inflate(getContext(), R.layout.discoverlist_item, null);
			holder = new ViewHolder();	
			
			holder.iv_discoverlist_icon = (ImageView) view.findViewById(R.id.iv_discoverlist_icon);
			holder.tv_discoverlist_name = (TextView) view.findViewById(R.id.tv_discoverlist_name);
//			holder.tv_discoverlist_introduce = (TextView) view.findViewById(R.id.tv_discoverlist_introduce);
			holder.tv_discoverlist_number = (TextView) view.findViewById(R.id.tv_discoverlist_number);
			holder.tv_discoverlist_try = (TextView) view.findViewById(R.id.tv_discoverlist_try);
		
			view.setTag(holder);
		}
		
	
		
		/*holder.tv_discoverlist_name.setText(getItem(position).getName());
		holder.tv_discoverlist_introduce.setText(getItem(position).getIntroduce());
		holder.tv_discoverlist_number.setText(getItem(position).getNumber());
		holder.tv_discoverlist_try.setText(getItem(position).getTrytry());*/
		
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
	 * */
	static class ViewHolder{
    	ImageView iv_discoverlist_icon;
    	TextView tv_discoverlist_name;
    	TextView tv_discoverlist_number;
    	TextView tv_discoverlist_try;
    	TextView tv_discoverlist_introduce;
    }

}
