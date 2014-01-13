package com.angelic.hscard.adapter;

import java.util.List;

import com.angelic.hscard.R;
import com.angelic.hscard.activity.Viewpager;
import com.angelic.hscard.dao.HsGroupCardsDao;
import com.angelic.hscard.dao.HsGroupInfoDao;
import com.angelic.hscard.model.HsGroupCards;
import com.angelic.hscard.model.HsGroupInfo;
import com.angelic.hscard.service.HsGroupCardsService;
import com.angelic.hscard.service.HsGroupInfoService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class RightMenuAdapter extends BaseAdapter {
	public static int STATE_GROUP = 0;
	public static int STATE_CARD = 1;
	public int state = 0;
	private LayoutInflater inflater;
	private List<HsGroupInfo> list;
	private List<HsGroupCards> cList;
	private ListView listViewG, listViewC;
	private HsGroupInfo groupInfo;
	private Context context;

	public RightMenuAdapter(Context context, ListView listViewG) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listViewG = listViewG;
		list = getAllHsGroupInfo();
		state = STATE_GROUP;

		listViewG.setOnItemClickListener(groupItemClick);
		
		Button buttonFooter = new Button(context);  
	    buttonFooter.setText("新建卡组");  
	    buttonFooter.setBackgroundResource(R.drawable.gridbg);
        buttonFooter.setTag("occSetting");
		listViewG.addFooterView(buttonFooter);
	}

	public RightMenuAdapter(Context context, ListView listViewC,
			HsGroupInfo info) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.listViewC = listViewC;
		this.groupInfo = info;
		cList = getAllHsGroupCards(info.getId());
		state = STATE_CARD;

		listViewC.setOnItemClickListener(cardItemClick);
		listViewC.setOnItemLongClickListener(cardItemLongClick);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (state == STATE_GROUP) {
			if (list == null) {
				return 0;
			} else {
				return list.size();
			}
		} else {
			if (cList == null) {
				return 0;
			} else {
				return cList.size();
			}
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (state == STATE_GROUP) {
			if (list == null) {
				return null;
			} else {
				return list.get(position);
			}
		} else {
			if (cList == null) {
				return null;
			} else {
				return cList.get(position);
			}
		}

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (state == STATE_GROUP) {
			if (list == null || getCount() == 0) {
				return null;
			}
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.card_group_item_g, null);
				viewHolder = new ViewHolder();
				viewHolder.groupBg = (RelativeLayout) convertView
						.findViewById(R.id.ll_cardgroup_bg);
				viewHolder.groupName = (TextView) convertView
						.findViewById(R.id.txt_cardgroup_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			
			if (list.get(position).getOccupation().equals("牧师")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.anduin_card_bg);
			} else if (list.get(position).getOccupation().equals("法师")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.portrait_card_bg);
			} else if (list.get(position).getOccupation().equals("萨满")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.thrall_card_bg);
			} else if (list.get(position).getOccupation().equals("德鲁伊")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.mafurion_card_bg);
			} else if (list.get(position).getOccupation().equals("猎人")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.rexxar_card_bg);
			}else if (list.get(position).getOccupation().equals("圣骑士")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.uther_card_bg);
			}else if (list.get(position).getOccupation().equals("潜行者")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.valeera_card_bg);
			}else if (list.get(position).getOccupation().equals("术士")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.guldan_card_bg);
			}else if (list.get(position).getOccupation().equals("战士")) {
				viewHolder.groupBg.setBackgroundResource(R.drawable.garrosh_card_bg);
			}
			viewHolder.groupName.setText(list.get(position).getName());
			return convertView;
		} else {
			if (cList == null || getCount() == 0) {
				return null;
			}
			ViewHolderC viewHolderC;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.card_group_item_c, null);
				viewHolderC = new ViewHolderC();
				viewHolderC.cardBg = (ImageView) convertView
						.findViewById(R.id.img_cardbg);
				viewHolderC.cardCount = (ImageView) convertView
						.findViewById(R.id.count);
				viewHolderC.cardName = (TextView) convertView
						.findViewById(R.id.cardname);
				viewHolderC.cardCost = (TextView) convertView
						.findViewById(R.id.cost);
				viewHolderC.cardBorder = (LinearLayout) convertView
						.findViewById(R.id.ll_cardborder);
				convertView.setTag(viewHolderC);
			} else {
				viewHolderC = (ViewHolderC) convertView.getTag();
			}

			
			return convertView;
		}

	}

	private static class ViewHolder {
		RelativeLayout groupBg;
		TextView groupName;
	}

	private static class ViewHolderC {
		ImageView cardBg;
		ImageView cardCount;
		TextView cardName;
		TextView cardCost;
		LinearLayout cardBorder;
	}

	private OnItemClickListener groupItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			
		}

	};
	private OnItemClickListener cardItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub
			
		}

	};
	private OnItemLongClickListener cardItemLongClick = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	/**
	 * 获取所有卡组信息
	 * 
	 * @Title getAllHsGroupInfo
	 * @Description TODO
	 * @return
	 */
	private List<HsGroupInfo> getAllHsGroupInfo() {
		HsGroupInfoService service = new HsGroupInfoDao(context);
		return service.getAllGroup();
	}

	private List<HsGroupCards> getAllHsGroupCards(String groupid) {
		HsGroupCardsService service = new HsGroupCardsDao(context);
		return service.getCardsByGroupId(groupid);
	}
}
