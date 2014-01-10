package com.angelic.hscard.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.util.Log;

public class AssetTest extends AndroidTestCase {
	private final String TAG = "AssetTest";

	/**
	 * 
	 * @Title readFile
	 * @Description 测试读取Asset文件带路径
	 */
	public void readFile() {
		Context context = getContext();
		AssetsFileService fileService = new AssetsFileService(context);
		Bitmap bitmap = fileService.getImageFromAssetsFile("card/hsbg.jpg");
		Log.i(TAG, bitmap.toString());
	}
}
