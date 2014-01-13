package com.angelic.hscard.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.angelic.hscard.db.DbOpenHelper;
import com.angelic.hscard.model.HsGroupInfo;
import com.angelic.hscard.service.HsGroupInfoService;
import com.angelic.hscard.utils.GsonUtil;

public class HsGroupInfoDao implements HsGroupInfoService {
	
	private final static String TAG = "HsGroupInfoDao";

	private DbOpenHelper helper = null;

	public HsGroupInfoDao(Context context) {
		helper = new DbOpenHelper(context);
	}
	
	@Override
	public List<HsGroupInfo> getAllGroup() {
		List<HsGroupInfo> list = new ArrayList<HsGroupInfo>();
		String sql = "select * from group_info";
		Log.i(TAG, sql);
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				HsGroupInfo hsGroupInfo = new HsGroupInfo();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				hsGroupInfo = (HsGroupInfo) GsonUtil.mapToObject(map, hsGroupInfo);
				list.add(hsGroupInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	@Override
	public int addHsGroup(HsGroupInfo info) {
		int strid = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("");
		sql.append("insert into group_info (");
		int state = 0;
		if(info.getName() !=null && !info.getName().equals("")){
			sql.append("name");
			state = 1;
		}
		if(info.getOccupation() !=null && !info.getOccupation().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append("occupation");
			state = 1;
		}
		if(info.getDescription() !=null && !info.getDescription().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append("description");
			state = 1;
		}
		sql.append(") values(");
		state = 0;
		if(info.getName() !=null && !info.getName().equals("")){
			sql.append(info.getName());
			state = 1;
		}
		if(info.getOccupation() !=null && !info.getOccupation().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(info.getOccupation());
			state = 1;
		}
		if(info.getDescription() !=null && !info.getDescription().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(info.getDescription());
			state = 1;
		}
		sql.append(")");
		Log.i(TAG, sql.toString());
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			database.execSQL(sql.toString(), null);
			Cursor cursor = database.rawQuery("select last_insert_rowid() from group_info", null);
			if(cursor.moveToFirst()){
				strid = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return strid;
	}

	@Override
	public int editHsGroup(HsGroupInfo info) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("");
		sql.append("update  group_info set");
		int state = 0;
		if(info.getName() !=null && !info.getName().equals("")){
			sql.append(" name =" + info.getName());
			state =1;
		}
		if(info.getOccupation() !=null && !info.getOccupation().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(" occupation =" + info.getOccupation());
			state =1;
		}
		if(info.getDescription() !=null && !info.getDescription().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(" description = '" + info.getDescription() + "'");
			state =1;
		}
		sql.append(" where id = " + info.getId());
		Log.i(TAG, sql.toString());
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			database.execSQL(sql.toString(),null);
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return 1;
	}

	@Override
	public int delHsGroup(String id) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = null;
		String sql = "delete from group_info where id =" + id;
		try {
			database = helper.getWritableDatabase();
			database.execSQL(sql,null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}finally{
			if (database != null) {
				database.close();
			}
		}
		return 1;
	}

}
