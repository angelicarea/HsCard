package com.angelic.hscard.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.angelic.hscard.db.DbOpenHelper;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.HsCardService;
import com.angelic.hscard.utils.GsonUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class HsCardDao implements HsCardService {

	private final static String TAG = "HsCardDao";

	private DbOpenHelper helper = null;

	public HsCardDao(Context context) {
		helper = new DbOpenHelper(context);
	}

	@Override
	public List<HsCard> getListrCardByModelOR(HsCard card) {
		// TODO Auto-generated method stub
		List<HsCard> list = new ArrayList<HsCard>();
		String sql = getORSqlByModel(card);
		Log.i(TAG, sql);
		SQLiteDatabase database = null;
		try {
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			int colums = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				HsCard hsCard = new HsCard();
				for (int i = 0; i < colums; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_value = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_value == null) {
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
				hsCard = (HsCard) GsonUtil.mapToObject(map, hsCard);
				list.add(hsCard);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (database != null) {
				database.close();
			}
		}
		Log.i(TAG, list.toString());
		return list;
	}

	/**
	 * 
	 * @Title getORSqlByModel
	 * @Description 精确筛选
	 * @param card
	 *            HsCard
	 * @return String
	 */
	private String getORSqlByModel(HsCard card) {
		String sql = getSkillSql(card);
		return sql;
	}

	/**
	 * 
	 * @Title getSkillSql
	 * @Description 第4层查询：获得技能的结果集合的SQL语句
	 * @param card
	 *            HsCard
	 * @return String
	 */
	private String getSkillSql(HsCard card) {
		String raceSql = getRaceSql(card);
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (" + raceSql + ")r where 1=1");
		if (card.getSkill() != null) {
			String[] strs = splitStrings(card.getSkill());
			if (strs.length > 0) {
				int i = 0;
				for (String str : strs) {
					if (i == 0) {
						buffer.append(" and skill like '%" + str + "%'");
					} else {
						buffer.append(" or skill like '%" + str + "%'");
					}
					i = i + 1;
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @Title getRaceSql
	 * @Description 第3层子查询：获得种族的结果集合的SQL语句
	 * @param card
	 *            HsCard
	 * @return String
	 */
	private String getRaceSql(HsCard card) {
		String typeSql = getTypeSql(card);
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (" + typeSql + ")t where 1=1");
		if (card.getRace() != null) {
			String[] strs = splitStrings(card.getRace());
			if (strs.length > 0) {
				int i = 0;
				for (String str : strs) {
					if (i == 0) {
						buffer.append(" and race = '" + str + "'");
					} else {
						buffer.append(" or race ='" + str + "'");
					}
					i = i + 1;
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @Title getTypeSql
	 * @Description 第2层子查询：获得卡牌类型的结果集合的SQL语句
	 * @param card
	 *            HsCard
	 * @return String
	 */
	private String getTypeSql(HsCard card) {
		String baseSql = getIsGoldenSql(card);
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from (" + baseSql + ")b where 1=1");
		if (card.getType() != null) {
			String[] strs = splitStrings(card.getType());
			if (strs.length > 0) {
				int i = 0;
				for (String str : strs) {
					if (i == 0) {
						buffer.append(" and type ='" + str + "'");
					} else {
						buffer.append(" or type ='" + str + "'");
					}
					i = i + 1;
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @Title getIsGoldenSql
	 * @Description 第1层子查询：获得是否是金卡的结果集合的SQL语句
	 * @param card
	 *            HsCard
	 * @return String
	 */
	private String getIsGoldenSql(HsCard card) {
		String sql = "select * from hscard where isgolden ="
				+ card.getIsgolden();
		return sql;
	}

	@Override
	public List<HsCard> getListrCardByModelAND(HsCard card) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getANDSqlByModel(HsCard card) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from hscard where 1=1");
		if (card != null) {
			if (card.getAttack() != null) {
				buffer.append(" and attack = ?");
			}
			if (card.getCost() != null) {
				buffer.append(" and cost ?");
			}
			if (card.getEname() != null) {
				buffer.append(" and ename = ?");
			}
			if (card.getHealth() != null) {
				buffer.append(" and health = ?");
			}
			if (card.getLevel() != null) {
				buffer.append(" and level = ?");
			}
			if (card.getOccupation() != null) {
				buffer.append(" and occupation = ?");
			}
			if (card.getRace() != null) {
				buffer.append(" and race = ?");
			}
			if (card.getRank() != null) {
				buffer.append(" and rank = ?");
			}
			if (card.getType() != null) {
				buffer.append(" and type = ?");
			}
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @Title splitStrings
	 * @Description 分割字符串
	 * @param strs
	 *            字符串用','相连
	 * @return String[]
	 */
	private String[] splitStrings(String strs) {
		String[] rs = null;
		rs = strs.split(",");
		return rs;
	}
}
