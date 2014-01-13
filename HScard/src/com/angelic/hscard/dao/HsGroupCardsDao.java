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
import com.angelic.hscard.model.HsGroupCards;
import com.angelic.hscard.model.HsGroupInfo;
import com.angelic.hscard.service.HsGroupCardsService;
import com.angelic.hscard.utils.GsonUtil;

public class HsGroupCardsDao implements HsGroupCardsService {

	private final static String TAG = "HsGroupCardsDao";

	private DbOpenHelper helper = null;

	public HsGroupCardsDao(Context context) {
		helper = new DbOpenHelper(context);
	}
	
	@Override
	public List<HsGroupCards> getCardsByGroupId(String groupid) {
		List<HsGroupCards> list = new ArrayList<HsGroupCards>();
		String sql = "select * from group_cards where groupid = '" + groupid + "'";
		Log.i(TAG, sql);
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				HsGroupCards hsGroupCards = new HsGroupCards();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				hsGroupCards = (HsGroupCards) GsonUtil.mapToObject(map, hsGroupCards);
				list.add(hsGroupCards);
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
	public int addHsGroupCards(HsGroupCards info) {
		// TODO Auto-generated method stub
		int strid = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("");
		sql.append("insert into group_cards (");
		int state = 0;
		if(info.getGroupid() !=null && !info.getGroupid().equals("")){
			sql.append("groupid");
			state = 1;
		}
		if(info.getCardid() !=null && !info.getCardid().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append("cardid");
			state = 1;
		}
		if(info.getName() !=null && !info.getName().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append("name");
			state = 1;
		}
		if(info.getCount() !=null && !info.getCount().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append("count");
			state = 1;
		}
		if(info.getIsgolden() !=null && !info.getIsgolden().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append("isgolden");
			state = 1;
		}
		sql.append(") values(");
		state = 0;
		if(info.getGroupid() !=null && !info.getGroupid().equals("")){
			sql.append(info.getGroupid());
			state = 1;
		}
		if(info.getCardid() !=null && !info.getCardid().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(info.getCardid());
			state = 1;
		}
		if(info.getName() !=null && !info.getName().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(info.getName());
			state = 1;
		}
		if(info.getCount() !=null && !info.getCount().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(info.getCount());
			state = 1;
		}
		if(info.getIsgolden() !=null && !info.getIsgolden().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(info.getIsgolden());
			state = 1;
		}
		sql.append(")");
		Log.i(TAG, sql.toString());
		SQLiteDatabase database = null;
		try {
			database = helper.getWritableDatabase();
			database.execSQL(sql.toString(), null);
			Cursor cursor = database.rawQuery("select last_insert_rowid() from group_cards", null);
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
	public int editHsGroupCards(HsGroupCards info) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("");
		sql.append("update  group_cards set");
		int state = 0;
		if(info.getGroupid() !=null && !info.getGroupid().equals("")){
			sql.append(" groupid =" + info.getGroupid());
			state =1;
		}
		if(info.getCardid() !=null && !info.getCardid().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(" cardid =" + info.getCardid());
			state =1;
		}
		if(info.getName() !=null && !info.getName().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(" name = '" + info.getName() + "'");
			state =1;
		}
		if(info.getCount() !=null && !info.getCount().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(" count =" + info.getCount());
			state =1;
		}
		if(info.getIsgolden() !=null && !info.getIsgolden().equals("")){
			if(state != 0){
				sql.append(",");
			}
			sql.append(" isgolden =" + info.getIsgolden());
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
	public int delHsGroupCards(String id) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = null;
		String sql = "delete from group_cards where id =" + id;
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
