package com.angelic.hscard.adapter;

import java.util.HashSet;
import java.util.List;

import com.angelic.hscard.R;
import com.angelic.hscard.activity.CardTool;
import com.angelic.hscard.activity.Viewpager;
import com.angelic.hscard.constants.Constants;
import com.angelic.hscard.handlers.SyncThumbnailExtractor;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.AssetsFileService;
import com.angelic.hscard.utils.FileUtils;
import com.angelic.hscard.utils.ImageTools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GridItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<HsCard> list;
	private Context context;
	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private GridView mGridView;
//	private LruCache<String, Bitmap> mMemoryCache;
//	// GridView中可见的第一张图片的下标
//	private int mFirstVisibleItem;
//	// GridView中可见的图片的数量
//	private int mVisibleItemCount;
//	// 记录是否是第一次进入该界面
//	private boolean isFirstEnterThisActivity = true;
//	// 记录读取本地图片的任务
//	private HashSet<AssetsloadBitmapAsyncTask> mAssetsloadBitmapAsyncTaskHashSet;

	private SyncThumbnailExtractor syncThumbnailExtractor;

	/**
	 * 卡牌浏览Adapter
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param list
	 * @param context
	 * @param gridView
	 */
	public GridItemAdapter(List<HsCard> list, Context context, GridView gridView) {
		// TODO Auto-generated constructor stub
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mGridView = gridView;
		mGridView.setOnScrollListener(new ScrollListenerImpl());
		mGridView.setOnItemClickListener(itemOnClick);
//		mAssetsloadBitmapAsyncTaskHashSet = new HashSet<AssetsloadBitmapAsyncTask>();
		syncThumbnailExtractor = new SyncThumbnailExtractor(context);
//		// 获取应用程序最大可用内存
//		int maxMemory = (int) Runtime.getRuntime().maxMemory();
//		int cacheSize = maxMemory / 8;
//		// 设置图片缓存大小为程序最大可用内存的1/8
//		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//			@Override
//			protected int sizeOf(String key, Bitmap bitmap) {
//				return bitmap.getRowBytes() * bitmap.getHeight();
//			}
//		};
	}
	/**
	 * 卡组模拟器的Adpater,长点击事件为浏览卡牌,点击事件为卡组添加卡牌
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param list
	 * @param context
	 * @param gridView
	 * @param group
	 */
	public GridItemAdapter(List<HsCard> list, Context context, GridView gridView,String group) {
		// TODO Auto-generated constructor stub
		super();
		this.list = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mGridView = gridView;
		mGridView.setOnScrollListener(new ScrollListenerImpl());
		mGridView.setOnItemClickListener(itemOnClickGroup);
		mGridView.setOnItemLongClickListener(itemOnLongClickGroup);
		syncThumbnailExtractor = new SyncThumbnailExtractor(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (list == null) {
			return null;
		} else {
			return list.get(position);
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
		ViewHolder viewHolder;
		String imgUrl = Constants.DATA_PATH + list.get(position).getEname()
				+ Constants.SuffixPng;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.card_tool_item, null);
			viewHolder = new ViewHolder();
			 viewHolder.loadgif = (ProgressBar) convertView
			 .findViewById(R.id.pb_loadcard);
			 viewHolder.loadgif.setTag("gif" + imgUrl);
			viewHolder.cardpng = (ImageView) convertView
					.findViewById(R.id.image);
			viewHolder.cardpng.setTag(imgUrl);
			viewHolder.cardpng.setImageBitmap(null);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.cardpng.setImageBitmap(null);
		syncThumbnailExtractor.decodeThumbnail(viewHolder.cardpng, imgUrl);

		// if (getBitmapFromMemoryCache(imgUrl) != null) {
		// viewHolder.cardpng.setImageBitmap(getBitmapFromMemoryCache(imgUrl));
		// // viewHolder.loadgif.setVisibility(View.GONE);
		// } else {
		// // //从assets中读取图片
		// // AssetsFileService assetsFileService = new AssetsFileService(
		// // context);
		// // Bitmap bitmap = assetsFileService
		// // .getImageFromAssetsFile(Constants.CardPath +
		// // list.get(position).getEname() + Constants.SuffixPng);
		// // bitmap = ThumbnailUtils.extractThumbnail(bitmap, 168, 236);
		// // viewHolder.cardpng.setImageBitmap(bitmap);
		// // addBitmapToMemoryCache(position+"", bitmap);
		// viewHolder.cardpng.setImageBitmap(null);
		// // viewHolder.loadgif.setVisibility(View.VISIBLE);
		// }

		// 测试用方法读取图片
		// if(list.get(position).getEname().equals("g1")){
		// viewHolder.cardpng.setImageResource(R.drawable.g2);
		// //Bitmap bp = ImageTools.createBitmapBySize(bitmap, 168, 236);

		// }else if(list.get(position).getEname().equals("g2")){
		//
		// }else if(list.get(position).getEname().equals("g3")){
		// viewHolder.cardpng.setImageResource(R.drawable.g3);
		// }else if(list.get(position).getEname().equals("g4")){
		// viewHolder.cardpng.setImageResource(R.drawable.g4);
		// }else if(list.get(position).getEname().equals("g5")){
		// viewHolder.cardpng.setImageResource(R.drawable.g5);
		// }else if(list.get(position).getEname().equals("g6")){
		// viewHolder.cardpng.setImageResource(R.drawable.g6);
		// }else if(list.get(position).getEname().equals("g7")){
		// viewHolder.cardpng.setImageResource(R.drawable.g7);
		// }else if(list.get(position).getEname().equals("g8")){
		// viewHolder.cardpng.setImageResource(R.drawable.g8);
		// }else if(list.get(position).getEname().equals("g9")){
		// viewHolder.cardpng.setImageResource(R.drawable.g9);
		// }else if(list.get(position).getEname().equals("g10")){
		// viewHolder.cardpng.setImageResource(R.drawable.g10);
		// }else if(list.get(position).getEname().equals("g11")){
		// viewHolder.cardpng.setImageResource(R.drawable.g11);
		// }else if(list.get(position).getEname().equals("g12")){
		// viewHolder.cardpng.setImageResource(R.drawable.g12);
		// }else if(list.get(position).getEname().equals("g13")){
		// viewHolder.cardpng.setImageResource(R.drawable.g13);
		// }else if(list.get(position).getEname().equals("g14")){
		// viewHolder.cardpng.setImageResource(R.drawable.g14);
		// }else if(list.get(position).getEname().equals("g15")){
		// viewHolder.cardpng.setImageResource(R.drawable.g15);
		// }
		return convertView;
	}

	private static class ViewHolder {
		public ImageView cardpng;
		public ProgressBar loadgif;
	}

	

//	/**
//	 * 将一张图片存储到LruCache中。
//	 * 
//	 * @param key
//	 *            LruCache的键，这里传入序号。
//	 * @param bitmap
//	 *            LruCache的键，这里传入缩略图Bitmap对象。
//	 */
//	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//		if (getBitmapFromMemoryCache(key) == null) {
//			mMemoryCache.put(key, bitmap);
//		}
//	}
//
//	/**
//	 * 从LruCache中获取一张图片，如果不存在就返回null。
//	 * 
//	 * @param key
//	 *            LruCache的键，这里传入序号。
//	 * @return 对应传入键的Bitmap对象，或者null。
//	 */
//	public Bitmap getBitmapFromMemoryCache(String key) {
//		return mMemoryCache.get(key);
//	}

//	/**
//	 * 取消所有任务
//	 */
//	public void cancelAllTasks() {
//		if (mAssetsloadBitmapAsyncTaskHashSet != null) {
//			for (AssetsloadBitmapAsyncTask task : mAssetsloadBitmapAsyncTaskHashSet) {
//				task.cancel(false);
//			}
//		}
//	}

	private class ScrollListenerImpl implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
//			mFirstVisibleItem = firstVisibleItem;
//			mVisibleItemCount = visibleItemCount;
//			if (isFirstEnterThisActivity && visibleItemCount > 0) {
//				loadBitmaps(firstVisibleItem, visibleItemCount);
//				isFirstEnterThisActivity = false;
//			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				pause();
			} else {
				resume();
			}
		}

	}
	private OnItemClickListener itemOnClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
//			Toast.makeText(context, "item" + (position+1), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(context, Viewpager.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("card", list.get(position));
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	};
	private OnItemClickListener itemOnClickGroup = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
//			Toast.makeText(context, "item" + (position+1), Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent(context, Viewpager.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("card", list.get(position));
//			intent.putExtras(bundle);
//			context.startActivity(intent);
		}
	};
	private OnItemLongClickListener itemOnLongClickGroup = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
//			Toast.makeText(context, "item" + (position+1), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(context, Viewpager.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("card", list.get(position));
			intent.putExtras(bundle);
			context.startActivity(intent);
			return false;
		}
	};

//	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
//		try {
//			for (int i = firstVisibleItem; i < firstVisibleItem
//					+ visibleItemCount; i++) {
//				Bitmap bitmap = getBitmapFromMemoryCache(Constants.CardPath
//						+ list.get(i).getEname() + Constants.SuffixPng);
//				if (bitmap == null) {
//					AssetsloadBitmapAsyncTask assetsloadBitmapAsyncTask = new AssetsloadBitmapAsyncTask();
//					mAssetsloadBitmapAsyncTaskHashSet
//							.add(assetsloadBitmapAsyncTask);
//					assetsloadBitmapAsyncTask.execute(Constants.CardPath
//							+ list.get(i).getEname() + Constants.SuffixPng,
//							list.get(i).getEname());
//				}
//				// else {
//				// //依据Tag找到对应的ImageView显示图片
//				// ImageView imageView = (ImageView)
//				// mGridView.findViewWithTag(Constants.CardPath +
//				// list.get(i).getEname() + Constants.SuffixPng);
//				// if (imageView != null && bitmap != null) {
//				// imageView.setImageBitmap(bitmap);
//				// notifyDataSetChanged();
//				// }
//				// }
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	private Bitmap readBitmaps(String imgUrl) {
//		AssetsFileService assetsFileService = new AssetsFileService(context);
//		Bitmap bitmap = assetsFileService.getImageFromAssetsFile(imgUrl);
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, 168, 236);
//		addBitmapToMemoryCache(imgUrl, bitmap);
//		return bitmap;
//	}

//	class AssetsloadBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
//		private String imgUrl;
//		private String imgName;
//
//		@Override
//		protected Bitmap doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			imgUrl = params[0];
//			imgName = params[1];
//			// Bitmap bitmap = readBitmaps(imgUrl);
//
//			if (!FileUtils.sdCardIsExist()) {
//
//				AssetsFileService assetsFileService = new AssetsFileService(
//						context);
//				Bitmap bitmap = assetsFileService
//						.getImageFromAssetsFile(imgUrl);
//				bitmap = ThumbnailUtils.extractThumbnail(bitmap, 168, 236);
//				addBitmapToMemoryCache(imgUrl, bitmap);
//				return bitmap;
//			} else {
//				Bitmap bitmap = ImageTools.getImageThumbnail(
//						Constants.DATA_PATH + imgName + Constants.SuffixPng,
//						168, 236);
//				addBitmapToMemoryCache(imgUrl, bitmap);
//				return bitmap;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap bitmap) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(bitmap);
//			// ProgressBar progressBar = (ProgressBar) mGridView
//			// .findViewWithTag("gif" + imgUrl);
//			ImageView imageView = (ImageView) mGridView.findViewWithTag(imgUrl);
//			if (imageView != null && bitmap != null) {
//				imageView.setImageBitmap(bitmap);
//				// progressBar.setVisibility(View.GONE);
//			}
//			notifyDataSetChanged();
//			mAssetsloadBitmapAsyncTaskHashSet.remove(this);
//		}
//
//	}

	public void clear() {
		syncThumbnailExtractor.clear();
	}

	public void pause() {
		syncThumbnailExtractor.pause();
	}

	public void stop() {
		syncThumbnailExtractor.stop();
	}

	public void resume() {
		syncThumbnailExtractor.resume();
	}

}
