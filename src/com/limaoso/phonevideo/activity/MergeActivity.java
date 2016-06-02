package com.limaoso.phonevideo.activity;

import com.limaoso.phonevideo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/*
 * author 院彩华
帐号合并页面
*/    
public class MergeActivity extends Activity{
	private TextView tv_title;//标题
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merge);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("合并帐号");
	}

}
