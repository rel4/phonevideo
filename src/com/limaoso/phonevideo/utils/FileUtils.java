package com.limaoso.phonevideo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
	public static long getNetTime() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 * 写log
	 * 
	 * @param log
	 * @param fileName
	 */
	public static synchronized void writeFileLog(String log, String fileName) {

		try {
			String rootLogPath = SDUtils.getRootFile(UIUtils.getContext()) + File.separator +"applog";
			File mf = new File(rootLogPath);
			if (!mf.exists()) {
				mf.mkdirs();
			}else if (!mf.isDirectory()) {
				mf.delete();
				mf.mkdirs();
			}
		String logPath =rootLogPath+  File.separator+fileName;
			File file = new File(logPath);
			if (!file.exists()) {
				
				file.createNewFile();
			}
			log = DateFormat(getNetTime()) + log + "\n";
			write(file, log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void write(File file, String str) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String DateFormat(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(time) + "-----------";
	}

}
