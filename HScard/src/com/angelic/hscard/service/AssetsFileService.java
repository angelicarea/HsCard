package com.angelic.hscard.service;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
/**
 * 
 * @classname AssetsFileService
 * @description Assets文件读取
 * @author By Chestnut(lishq)
 * @date Dec 27, 2013 12:44:30 PM
 */
public class AssetsFileService {

	private final String TAG = "LocalFileService";

	private Context context;

	public AssetsFileService(Context context) {
		this.context = context;
	}

	public AssetsFileService() {

	}

	/**
	 * 
	 * @Title getImageFromAssetsFile
	 * @Description 通过文件名获取Assets目录下的图片
	 * @param filePath 文件路径，文件名带后缀
	 * @return
	 */
	public Bitmap getImageFromAssetsFile(String filePath) {
		Bitmap image = null;
		//AssetManager需要用context来获取
		AssetManager am = this.context.getResources().getAssets();
		try {
			InputStream is = am.open(filePath);
			image = BitmapFactory.decodeStream(is);
			is.close();
			Log.i(TAG, "-->" + image.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(TAG, "-->获取Asset图片失败！");
		}

		return image;

	}
}
