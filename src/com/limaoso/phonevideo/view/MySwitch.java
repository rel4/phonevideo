package com.limaoso.phonevideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.limaoso.phonevideo.R;

/**
 * 自定义滑动开关
 * 
 * @author jiang
 * 
 */
public class MySwitch extends View   {
	private Bitmap bg_close, switch_open_bg;
	private Bitmap slide_close, slide_open_bg;// 滑块对应的图片
	private int LEFT;
	private boolean open = false;
	int l = 0; // 滑块当前的位置
	// 定义了一个成员变量
	private OnCheckedChangeListener changeListener;

	// 回调方法
	public void setOnChangeListener(OnCheckedChangeListener changeListener) {
		this.changeListener = changeListener;
	}

	// 步骤1 声明接口
	public interface OnCheckedChangeListener {
		void onCheckedChanged(MySwitch mySwitch, boolean isOpen);
	}


	// 在代码中使用自定义控件
	public MySwitch(Context context) {
		super(context);
		init();
	}

	private void init() {
		bg_close = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_close_bg);
		switch_open_bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.switch_open_bg);
		slide_close = BitmapFactory.decodeResource(getResources(),
				R.drawable.slide_close);
		slide_open_bg = BitmapFactory.decodeResource(getResources(),
				R.drawable.slide_open_bg);
		LEFT = bg_close.getWidth() - slide_close.getWidth();
//		this.setOnClickListener(this);
	}

	int startX;

	// int startY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 按下的时候记录开始的坐标
			startX = (int) event.getX();
			// if (Math.abs(l - 0) < Math.abs(l - LEFT)) {
			// l = 0;
			// open = false;
			// } else {
			// l = LEFT;
			// open = true;
			// }
			break;
		case MotionEvent.ACTION_MOVE:// 移动的时候
			int newX = (int) event.getX();
			int dX = newX - startX;
			l += dX;
			if (l < 0) {
				l = 0;
			} else if (l > LEFT) {
				l = LEFT;
			}

			startX = newX;
			invalidate();

			// if (Math.abs(l - 0) < Math.abs(l - LEFT)) {
			// l = 0;
			// open = false;
			// } else {
			// l = LEFT;
			// open = true;
			// }
			break;
		case MotionEvent.ACTION_UP:
			if (l <= LEFT / 2) {
				l = 0;
				open = false;
			} else {
				l = LEFT;
				open = true;
			}
			invalidate();

			// if (Math.abs(l - 0) < Math.abs(l - LEFT)) {
			// l = 0;
			// open = false;
			// } else {
			// l = LEFT;
			// open = true;
			// }
			if (changeListener != null) {
				changeListener.onCheckedChanged(this, open);
			}
			break;
		}

		return true;
	}

	public void setOpen(int open) {
		if (open == 1) {
			this.open = true;
			l = LEFT;
		} else {
			this.open = false;
			l = 0;
		}
		if (changeListener != null) {
			changeListener.onCheckedChanged(this, this.open);
		}
	}

	//
	public MySwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();

	}

	// 在布局文件中声明自定义控件
	public MySwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	// 设置测量的规则
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 设置控件的宽和高
		setMeasuredDimension(bg_close.getWidth(), bg_close.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Paint paint=new Paint();
		// paint.setColor(Color.RED);
		// canvas.drawRect(0, 0, 200, 200, paint);
		// 参数1 bitMap 图片 参数2 和参数3 分别代表 x坐标和y坐标
		canvas.drawBitmap(open ? bg_close : switch_open_bg, 0, 0, null); // 把背景画到控件上
		// 画滑块
		canvas.drawBitmap(open ? slide_close : slide_open_bg, open?LEFT:0, 0, null);
	}

//	@Override
//	public void onClick(View v) {
//		open = !open;
//		if ( changeListener!= null) {
//			changeListener.onCheckedChanged(this, open);
//		}
//		invalidate();
//	}
}
