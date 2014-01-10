package com.angelic.hscard.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * 
 * @classname ImageTools
 * @description 处理图片的工具类.
 * @author By Chestnut(lishq)
 * @date Dec 30, 2013 10:04:28 PM
 */
public class ImageTools {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	/**
	 * 
	 * @Title toGrayscale
	 * @Description 图片去色,返回灰度图片
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 
	 * @Title toGrayscale
	 * @Description 去色同时加圆角
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 
	 * @Title toRoundCorner
	 * @Description 把图片变成圆角
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 
	 * @Title toRoundCorner
	 * @Description 使圆角功能支持BitampDrawable
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * 
	 * @Title saveBefore
	 * @Description 读取路径中的图片，然后将其转化为缩放后的bitmap
	 * @param path
	 */
	public static void saveBefore(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = 2; // 图片长宽各缩小二分之一
		// 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
		bitmap = BitmapFactory.decodeFile(path, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		System.out.println(w + " " + h);
		// savePNG_After(bitmap,path);
		saveJPGE_After(bitmap, path);
	}

	/**
	 * 
	 * @Title savePNG_After
	 * @Description 保存图片为PNG
	 * @param bitmap
	 * @param name
	 */
	public static void savePNG_After(Bitmap bitmap, String name) {
		File file = new File(name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title saveJPGE_After
	 * @Description 保存图片为JPEG
	 * @param bitmap
	 * @param path
	 */
	public static void saveJPGE_After(Bitmap bitmap, String path) {
		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title createBitmapForWatermark
	 * @Description 水印
	 * @param src
	 * @param watermark
	 * @return
	 */
	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 
	 * @Title potoMix
	 * @Description 图片合成
	 * @param direction
	 * @param bitmaps
	 * @return
	 */
	public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
			int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}

	/**
	 * 
	 * @Title createBitmapBySize
	 * @Description 将Bitmap转换成指定大小
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * 
	 * @Title drawableToBitmapByBD
	 * @Description Drawable 转 Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmapByBD(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * 
	 * @Title bitmapToDrawableByBD
	 * @Description Bitmap 转 Drawable
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * 
	 * @Title bytesToBimap
	 * @Description byte[] 转 bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Title bitmapToBytes
	 * @Description bitmap 转 byte[]
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 
	 * @Title blurImage
	 * @Description 模糊效果,bitmap不能超过32bits
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImage(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int newColor = 0;

		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++) {
			for (int k = 1, len = height - 1; k < len; k++) {
				for (int m = 0; m < 9; m++) {
					int s = 0;
					int p = 0;
					switch (m) {
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}

				for (int m = 0; m < 9; m++) {
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}

				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		return bitmap;
	}

	/**
	 * 
	 * @Title blurImageAmeliorate
	 * @Description 柔化效果(高斯模糊)(优化后比上面快三倍),bitmap不能超过32bits
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 
	 * @Title setAlpha
	 * @Description 设置图片透明度
	 * @param sourceImg
	 *            要传入的图片
	 * @param number
	 *            设置的透明值 1~100之间的
	 * @return 返回设置完成的图片
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);

		return sourceImg;
	}

	/**
	 * 缩放图片
	 * 
	 * @Title zoomImg
	 * @Description TODO
	 * @param img
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(String img, int newWidth, int newHeight) {
		// 图片源
		Bitmap bm = BitmapFactory.decodeFile(img);
		if (null != bm) {
			return zoomImg(bm, newWidth, newHeight);
		}
		return null;
	}

	/**
	 * 缩放图片,Assets资源
	 * 
	 * @Title zoomImg
	 * @Description TODO
	 * @param context
	 * @param img
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Context context, String img, int newWidth,
			int newHeight) {
		// 图片源
		try {
			Bitmap bm = BitmapFactory.decodeStream(context.getAssets()
					.open(img));
			if (null != bm) {
				return zoomImg(bm, newWidth, newHeight);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 缩放图片,bitmap
	 * 
	 * @Title zoomImg
	 * @Description TODO
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 根据宽度从本地图片路径获取该图片的缩略图
	 * 
	 * @param localImagePath
	 *            本地图片的路径
	 * @param width
	 *            缩略图的宽
	 * @param addedScaling
	 *            额外可以加的缩放比例
	 * @return bitmap 指定宽高的缩略图
	 */
	public static Bitmap getBitmapByWidth(String localImagePath, int width,
			int addedScaling) {
		if (TextUtils.isEmpty(localImagePath)) {
			return null;
		}

		Bitmap temBitmap = null;

		try {
			BitmapFactory.Options outOptions = new BitmapFactory.Options();

			// 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
			outOptions.inJustDecodeBounds = true;

			// 加载获取图片的宽高
			BitmapFactory.decodeFile(localImagePath, outOptions);

			int height = outOptions.outHeight;

			if (outOptions.outWidth > width) {
				// 根据宽设置缩放比例
				outOptions.inSampleSize = outOptions.outWidth / width + 1
						+ addedScaling;
				outOptions.outWidth = width;

				// 计算缩放后的高度
				height = outOptions.outHeight / outOptions.inSampleSize;
				outOptions.outHeight = height;
			}

			// 重新设置该属性为false，加载图片返回
			outOptions.inJustDecodeBounds = false;
			outOptions.inPurgeable = true;
			outOptions.inInputShareable = true;
			temBitmap = BitmapFactory.decodeFile(localImagePath, outOptions);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return temBitmap;
	}
}
