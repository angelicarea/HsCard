package com.angelic.hscard.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.angelic.hscard.R;
import com.angelic.hscard.adapter.GridItemAdapter;
import com.angelic.hscard.dao.HsCardDao;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.HsCardService;

public class CardTool extends Activity{
	private GridView gridView;
	private List<HsCard> list = new ArrayList<HsCard>();
	private GridItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_tool);
		HsCard card = new HsCard();
		card.setOccupation("中立");
		HsCardService service = new HsCardDao(this);
		list = service.getListrCardByModelOR(card);
//		HsCard card1 = new HsCard();
//		HsCard card2 = new HsCard();
//		HsCard card3 = new HsCard();
//		HsCard card4 = new HsCard();
//		HsCard card5 = new HsCard();
//		card1.setEname("g1");
//		card2.setEname("g2");
//		card3.setEname("g3");
//		card4.setEname("g4");
//		card5.setEname("g5");
//		list.add(card1);
//		list.add(card2);
//		list.add(card3);
//		list.add(card4);
//		list.add(card5);
//		HsCard card6 = new HsCard();
//		HsCard card7 = new HsCard();
//		HsCard card8 = new HsCard();
//		HsCard card9 = new HsCard();
//		HsCard card10 = new HsCard();
//		card6.setEname("g6");
//		card7.setEname("g7");
//		card8.setEname("g8");
//		card9.setEname("g9");
//		card10.setEname("g10");
//		list.add(card6);
//		list.add(card7);
//		list.add(card8);
//		list.add(card9);
//		list.add(card10);
//		HsCard card11 = new HsCard();
//		HsCard card12 = new HsCard();
//		HsCard card13 = new HsCard();
//		HsCard card14 = new HsCard();
//		HsCard card15 = new HsCard();
//		card11.setEname("g11");
//		card12.setEname("g12");
//		card13.setEname("g13");
//		card14.setEname("g14");
//		card15.setEname("g15");
//		list.add(card11);
//		list.add(card12);
//		list.add(card13);
//		list.add(card14);
//		list.add(card15);
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setVerticalScrollBarEnabled(false);
		setOnPageList(list);
	}
	public void setOnPageList(List<HsCard> list){
		adapter = new GridItemAdapter(list, this,gridView);
		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(itemOnClick);
	}
//	private OnItemClickListener itemOnClick = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//			// TODO Auto-generated method stub
//			Toast.makeText(CardTool.this, "item" + (position+1), Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent(CardTool.this, Viewpager.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("card", list.get(position));
//			intent.putExtras(bundle);
//			startActivity(intent);
//		}
//	};
}
