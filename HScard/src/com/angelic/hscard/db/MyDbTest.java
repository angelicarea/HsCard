package com.angelic.hscard.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.angelic.hscard.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDbTest {

	private static final String DATABASE_PATH = "/data/data/com.angelic.hscard/databases";

	//当前数据库版本号 (int类型)
	private static final int DATABASE_VERSION = 0;

	private static final String DATABASE_NAME = "hearthstone.db";

	private static String outFileName = DATABASE_PATH + "/" + DATABASE_NAME;

	//传入上下文
	private Context context;

	private SQLiteDatabase database;

	/**
	 * 
	 * <p>Title: </p>
	 * <p>Description: 检查数据库是否存在,并校验版本号.如果版本号更新,则删除原数据库,并复制/res/raw/hearthstone.db到APK目录下</p>
	 * @param context 构造函数传入上下文参数
	 */
	public MyDbTest(Context context) {
		this.context = context;

		//获得APK下的数据库文件
		File file = new File(outFileName);
		//判断数据库文件是否存在
		if (file.exists()) {
			//获得APK中的数据库
			database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
			//判断APK中的数据库版本,如何不等于当前数据库版本,则删除.
			if (database.getVersion() != DATABASE_VERSION) {
				database.close();
				file.delete();
			}
		}
		//创建数据库
		try {
			buildDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title buildDatabase
	 * @Description 构建数据库
	 * @throws Exception
	 */
	private void buildDatabase() throws Exception {
		//将/res/raw/下的当前数据库转化为输入流
		InputStream myInput = context.getResources().openRawResource(
				R.raw.hearthstone);
		//获取APK下数据库文件
		File file = new File(outFileName);
		//获取APK下数据库路径
		File dir = new File(DATABASE_PATH);
		//如果APK下数据库路径不存在,则创建数据库路径
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new Exception("创建失败");
			}
		}

		//如果APK下数据库文件不存在,则将/res/raw/下当前版本的数据库文件写入到APK数据库文件夹下
		if (!file.exists()) {
			try {
				OutputStream myOutput = new FileOutputStream(outFileName);

				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
				}
				myOutput.close();
				myInput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 查找
	 * 
	 * @return
	 */
	public Cursor select() {
		database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
		String sql = "select * from hscard";

		Cursor cursor = database.rawQuery(sql, null);
		return cursor;
	}

	/**
	 * 插入
	 * 
	 * @param word
	 * @param note
	 * @return
	 */
	public long insert(String word, String note) {
		database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
		ContentValues cv = new ContentValues();
		cv.put("word", word);
		cv.put("note", note);

		long result = database.insert("hscard", null, cv);
		return result;
	}

	/**
	 * 更新
	 * 
	 * @param word
	 * @param note
	 * @return
	 */
	private int update(String word, String note) {
		database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);

		ContentValues cv = new ContentValues();
		cv.put("note", note);

		int result = database.update("hscard", cv, "word=?",
				new String[] { word });

		return result;
	}

	/**
	 * 删除
	 * 
	 * @param word
	 */
	public int deleteNote(String word) {
		database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
		int result = database.delete("hscard", "word=?",
				new String[] { word });
		return result;
	}

	public void close() {
		database.close();
	}
}