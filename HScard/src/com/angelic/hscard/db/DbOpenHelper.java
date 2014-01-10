package com.angelic.hscard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @classname DbOpenHelper
 * @description SQLite数据库操作 存储到存储介质/data/data/项目包名/datebases/${dbname}.db
 * @author By Chestnut(lishq)
 * @date Dec 29, 2013 10:26:15 AM
 */
public class DbOpenHelper extends SQLiteOpenHelper {

	private static String name = "hearthstone.db";// 表示数据库名称，如果为null则创建到内存当中
	private static int version = 1;// 数据库版本

	public DbOpenHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 创建数据库后,不会打开或创建一个数据库,直到调用getWritableDatabase(),或者getReadableDatabase()方法
	 * 
	 * @Title onCreate
	 * @Description 当数据库创建的时候，是第一次执行，完成数据库的表的创建,版本Version=1
	 * @param db
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 支持的数据类型，整形类型，字符串类型，日期类型，二进制的数据类型
		String sql = "create table hscard(cardid integer primary key autoincrement,name varchar(60),ename varchar(60),skill varchar(60),description varchar(120),occupation varchar(60),type varchar(60),race varchar(60),cost integer,attack integer,health integer,decompound integer,compound integer,level varchar(10),imgurl varchar(200),gimgurl varchar(200),rank varchar(10),artist varchar(60),cardps varchar(200),isgolden integer)";
		db.execSQL(sql);
	}

	/**
	 * 
	 * @Title onUpgrade
	 * @Description 当数据库版本更新,version比onCreate()的版本高,只执行onUpgrade()方法,版本Version=2
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		/**********
		 * 样例 String sql = "alter table person add sex varchar(8)";
		 * db.execSQL(sql);
		 **********/
	}

}
