package com.angelic.hscard.adapter;

import java.util.List;

import com.angelic.hscard.R;
import com.angelic.hscard.model.HsLeftMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class LeftMenuAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<HsLeftMenu> list;
	/** 选择position */
	public int selectedRadio = -1;

	public LeftMenuAdapter(List<HsLeftMenu> list, Context context) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
		
//		BitmapFactory.Options options = new BitmapFactory.Options();  
//       /** 
//        * 最关键在此，把options.inJustDecodeBounds = true; 
//        * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了 
//        */  
//       options.inJustDecodeBounds = true;  
//       Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/test.jpg", options); // 此时返回的bitmap为null  
//		Bitmap bitmap = BitmapFactory.decodeResource(getResource(),);
	}

	/**
	 * 设置选中的RadioButton的状态
	 * @Title setSelectRadioButton
	 * @Description 
	 * @param position
	 */
	public void setSelectRadioButton(int position) {
		selectedRadio = position;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (list == null) {
			return null;
		} else {
			return list.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (list == null || getCount() == 0) {
			return null;
		}
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.occupation_item, null);
			viewHolder = new ViewHolder();
			viewHolder.count = (TextView) convertView
					.findViewById(R.id.txt_count);
			viewHolder.btnOcc = (RadioButton) convertView
					.findViewById(R.id.btn_occupation);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).getName().equals("中立")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_neutrality);
			viewHolder.count.setText(list.get(position).getCount() + "");
		} else if (list.get(position).getName().equals("牧师")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_priest);
			viewHolder.count.setText(list.get(position).getCount() + "");
		} else if (list.get(position).getName().equals("法师")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_mage);
			viewHolder.count.setText(list.get(position).getCount() + "");
		} else if (list.get(position).getName().equals("萨满")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_shaman);
			viewHolder.count.setText(list.get(position).getCount() + "");
		} else if (list.get(position).getName().equals("德鲁伊")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_druid);
			viewHolder.count.setText(list.get(position).getCount() + "");
		} else if (list.get(position).getName().equals("猎人")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_hunter);
			viewHolder.count.setText(list.get(position).getCount() + "");
		}else if (list.get(position).getName().equals("圣骑士")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_paladin);
			viewHolder.count.setText(list.get(position).getCount() + "");
		}else if (list.get(position).getName().equals("潜行者")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_robber);
			viewHolder.count.setText(list.get(position).getCount() + "");
		}else if (list.get(position).getName().equals("术士")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_warlock);
			viewHolder.count.setText(list.get(position).getCount() + "");
		}else if (list.get(position).getName().equals("战士")) {
			viewHolder.btnOcc.setBackgroundResource(R.drawable.tab_warrior);
			viewHolder.count.setText(list.get(position).getCount() + "");
		}

		// viewHolder.btnOcc.setId(position);

		// viewHolder.btnOcc.setOnCheckedChangeListener(changeListener);
		if (selectedRadio == position) {
			viewHolder.btnOcc.setChecked(true);
//			LayoutParams params = new LayoutParams();
//			params.width = 
//			viewHolder.btnOcc.setLayoutParams(params);
		} else {
			viewHolder.btnOcc.setChecked(false);
		}

		return convertView;
	}

	private static class ViewHolder {
		public TextView count;
		public RadioButton btnOcc;
	}

//	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {
//
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView,
//				boolean isChecked) {
//			if (isChecked) {
//
//				selectedRadio = buttonView.getId();
//
//			}
//			notifyDataSetChanged();
//		}
//	};

}
