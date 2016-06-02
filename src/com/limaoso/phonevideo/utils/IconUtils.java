package com.limaoso.phonevideo.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class IconUtils {
	// 换图片

	public static boolean changeBitmap(String path, String name, String newICON) {
		if (CheckUtils.isIcon(newICON)) {
			Bitmap bm = BitmapFactory.decodeFile(newICON);
			IconCompress.saveBitmap(bm, path, name);
			File f = new File(newICON);
			f.delete();
			return true;
		}
		return false;
	}

}
