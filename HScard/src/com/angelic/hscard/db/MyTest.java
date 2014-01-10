package com.angelic.hscard.db;

import java.util.List;
import java.util.Map;

import com.angelic.hscard.dao.HsCardDao;
import com.angelic.hscard.db.DbOpenHelper;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.HsCardService;

import android.test.AndroidTestCase;
import android.util.Log;

public class MyTest extends AndroidTestCase {

	private static String TAG = "MyTest";

	public MyTest() {

	}

	public void createDb() {
		DbOpenHelper helper = new DbOpenHelper(getContext());
		// 创建数据库后,不会打开或创建一个数据库,直到调用getWritableDatabase(),或者getReadableDatabase()方法
		helper.getWritableDatabase();
		Log.i(TAG,"-->");
	}
	
	public void rawDB(){
		MyDbTest dbTest = new MyDbTest(getContext());
//		Log.i(TAG,"-->");
		
	}
	public void selectListByModel(){
		HsCard card = new HsCard();
		card.setIsgolden("1");
		card.setSkill("冲锋,战吼");
		HsCardService service = new HsCardDao(getContext());
		List<HsCard> list = service.getListrCardByModelOR(card);
//		Log.i(TAG,"-->" + list.toString());
	}
}
