package com.angelic.hscard.adapter;

import java.util.List;

import com.angelic.hscard.R;
import com.angelic.hscard.constants.Constants;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.AssetsFileService;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<HsCard> list;
	private Context context;

	public GridItemAdapter(List<HsCard> list, Context context) {
		// TODO Auto-generated constructor stub
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.card_tool_item, null);
			viewHolder = new ViewHolder();
			viewHolder.cardpng = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//从assets中读取图片
		AssetsFileService assetsFileService = new AssetsFileService(
				context);
		Bitmap bitmap = assetsFileService
				.getImageFromAssetsFile(Constants.CardPath + list.get(position).getEname() + Constants.SuffixPng);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, 168, 236);
		viewHolder.cardpng.setImageBitmap(bitmap);
		//测试用方法读取图片
//		if(list.get(position).getEname().equals("g1")){
//		viewHolder.cardpng.setImageResource(R.drawable.g2);
//		//Bitmap bp = ImageTools.createBitmapBySize(bitmap, 168, 236);
			
			
//		}else if(list.get(position).getEname().equals("g2")){
//			
//		}else if(list.get(position).getEname().equals("g3")){
//			viewHolder.cardpng.setImageResource(R.drawable.g3);
//		}else if(list.get(position).getEname().equals("g4")){
//			viewHolder.cardpng.setImageResource(R.drawable.g4);
//		}else if(list.get(position).getEname().equals("g5")){
//			viewHolder.cardpng.setImageResource(R.drawable.g5);
//		}else if(list.get(position).getEname().equals("g6")){
//			viewHolder.cardpng.setImageResource(R.drawable.g6);
//		}else if(list.get(position).getEname().equals("g7")){
//			viewHolder.cardpng.setImageResource(R.drawable.g7);
//		}else if(list.get(position).getEname().equals("g8")){
//			viewHolder.cardpng.setImageResource(R.drawable.g8);
//		}else if(list.get(position).getEname().equals("g9")){
//			viewHolder.cardpng.setImageResource(R.drawable.g9);
//		}else if(list.get(position).getEname().equals("g10")){
//			viewHolder.cardpng.setImageResource(R.drawable.g10);
//		}else if(list.get(position).getEname().equals("g11")){
//			viewHolder.cardpng.setImageResource(R.drawable.g11);
//		}else if(list.get(position).getEname().equals("g12")){
//			viewHolder.cardpng.setImageResource(R.drawable.g12);
//		}else if(list.get(position).getEname().equals("g13")){
//			viewHolder.cardpng.setImageResource(R.drawable.g13);
//		}else if(list.get(position).getEname().equals("g14")){
//			viewHolder.cardpng.setImageResource(R.drawable.g14);
//		}else if(list.get(position).getEname().equals("g15")){
//			viewHolder.cardpng.setImageResource(R.drawable.g15);
//		}
		return convertView;
	}

	private static class ViewHolder {
		public ImageView cardpng;
	}
	@SuppressWarnings("unused")
	private static String toNormalCardName(String ename){
		String g = ename.substring(0, 2);
		if(g.equals("g-")){
			return ename.substring(2);
		}else{
			return ename;
		}
	}
}
