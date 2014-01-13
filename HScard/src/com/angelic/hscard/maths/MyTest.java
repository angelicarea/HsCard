package com.angelic.hscard.maths;

import java.util.List;

import com.angelic.hscard.dao.HsCardDao;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.model.HsLeftMenu;
import com.angelic.hscard.service.HsCardService;

import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {

	private static String TAG = "MyTest";

	public MyTest() {

	}
	public void selectLeftMenu(){
		HsCard card = new HsCard();
		HsCardService service = new HsCardDao(getContext());
		List<HsCard> list = service.getListrCardByModelOR(card);
		List<HsLeftMenu> menu = MathsOccup.getTypeByHsList(list);
//		Log.i(TAG,"-->" + list.toString());
	}
}
