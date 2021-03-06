package com.angelic.hscard.utils;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class Utils {
	public static final boolean DBG_H = false;
	private static final String TAG = "Utils";

	public static void logh(String tag, String msg) {
		if (DBG_H)
			Log.d(tag, msg);
	}

	public static boolean isFirstRun(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"share", context.MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			// Log.d("debug", "第一次运行");
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return true;
		} else {
			// Log.d("debug", "不是第一次运行");
			return false;
		}
	}

	/**
	 * Refresh view status
	 */
	public static void setVisibleGone(View view, View... views) {
		if (null != view && view.getVisibility() != View.VISIBLE)
			view.setVisibility(View.VISIBLE);
		setGone(views);
	}

	public static void setGone(View... views) {
		if (views != null && views.length > 0) {
			for (View view : views) {
				if (null != view && view.getVisibility() != View.GONE)
					view.setVisibility(View.GONE);
			}
		}
	}

	public static void setVisible(View... views) {
		if (views != null && views.length > 0) {
			for (View view : views) {
				if (null != view && view.getVisibility() != View.VISIBLE)
					view.setVisibility(View.VISIBLE);
			}
		}
	}

	public static void setEnable(View... views) {
		if (views != null && views.length > 0) {
			for (View view : views) {
				if (view != null && !view.isEnabled()) {
					view.setEnabled(true);
				}
			}
		}
	}

	public static void setDisable(View... views) {
		if (views != null && views.length > 0) {
			for (View view : views) {
				if (view != null && view.isEnabled()) {
					view.setEnabled(false);
				}
			}
		}
	}

	public static void setInvisible(View... views) {
		if (views != null && views.length > 0) {
			for (View view : views) {
				if (null != view && view.getVisibility() != View.INVISIBLE)
					view.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * File Operations
	 * 
	 * @param args
	 *            file names
	 * @return
	 */
	public static String getFilePathName(String... args) {
		String[] param = args;
		String Str = param[0];
		for (int i = 1, len = param.length; i < len; i++) {
			if (null == Str)
				return null;
			if (Str.endsWith(File.separator)
					&& param[i].startsWith(File.separator))
				Str = Str.substring(0, Str.length() - 1) + param[i];
			else if (Str.endsWith(File.separator)
					|| param[i].startsWith(File.separator))
				Str += param[i];
			else
				Str += File.separator + param[i];
		}
		return Str;
	}

	public static boolean mkdirs(String fileDir) {
		logh(TAG, "mkdirs: " + fileDir);
		File file = new File(fileDir);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return true;
	}

	public final static String FILE_BASE_NAME = "ProMonkey";
	public final static String FILE_ASSETS_PATH = "source";

	public static String mkTmpDirs() {
		String tmpPath = getFilePathName(Environment
				.getExternalStorageDirectory().getAbsolutePath(),
				FILE_BASE_NAME);
		mkdirs(tmpPath);
		return tmpPath;
	}

}
