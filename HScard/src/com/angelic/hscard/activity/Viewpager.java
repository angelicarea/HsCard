package com.angelic.hscard.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.angelic.hscard.R;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.AssetsFileService;
import com.angelic.hscard.utils.ImageTools;
import com.angelic.hscard.view.MyScrollLayout;
import com.angelic.hscard.view.OnViewChangeListener;
import com.angelic.hscard.constants.Constants;

/**
 * 
 * @classname Viewpager
 * @description 浏览卡牌及其信息
 * @author By Chestnut(lishq)
 * @date Dec 25, 2013 1:40:57 PM
 */
public class Viewpager extends Activity implements OnViewChangeListener {

	private final static String TAG = "Viewpager";

	private MyScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private LinearLayout pointLLayout;
	private RelativeLayout titleLayout;
	private ImageView normalCard, goldenCard;
	private Button btnBack;
	private String normalName, goldenName;
	private HsCard card;
	private TextView txtName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.card_viewpager);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Intent intent = getIntent();
		// 接收意图Intent传值normalName普通卡牌和金色卡牌的英文名
		card = (HsCard)intent.getSerializableExtra("card");
		normalName = toNormalCardName(card.getEname());
		goldenName = "g-" + normalName;
		
		initView();
	}

	private void initView() {
		// 自定义的ViewGroup布局
		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
		// 圆点布局
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);

		// 头部事件
		titleLayout = (RelativeLayout) findViewById(R.id.title);
		titleLayout.setOnClickListener(onClick);
		// 回退事件
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClick);
		//卡牌中文名
		txtName = (TextView)findViewById(R.id.txt_name);
		txtName.setText(card.getName());
		// 普通卡牌的ImageView初始化
		normalCard = (ImageView) findViewById(R.id.normalcard);
		// 金色卡牌的ImageView初始化
		goldenCard = (ImageView) findViewById(R.id.goldencard);

		// 设置卡牌的图片
		setCardView(normalCard, normalName);
		setCardView(goldenCard, goldenName);
		// 获取com.angelic.hscard.view.MyScrollLayout包含的节点数(即布局数量)
		count = mScrollLayout.getChildCount();
		// 初始化圆点ImageView
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	/**
	 * 设置View
	 */
	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	/**
	 * 
	 * @Title setcurrentPoint
	 * @Description 设置当前的圆点
	 * @param position 改变后的View序号
	 */
	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
		Log.i(TAG, currentItem + "");
	}

	/**
	 * 
	 * @Title setCardView
	 * @Description 设置Card(ImageView)的Bitmap
	 * @param view
	 *            卡牌图片(ImageView)
	 * @param cardname
	 *            卡牌英文名
	 */
	private void setCardView(ImageView view, String cardname) {
		AssetsFileService assetsFileService = new AssetsFileService(
				Viewpager.this);
		Bitmap bitmap = assetsFileService
				.getImageFromAssetsFile(Constants.CardPath + cardname + Constants.SuffixPng);
		bitmap = ImageTools.createBitmapBySize(bitmap, 405, 539);
		view.setImageBitmap(bitmap);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.title:
				Intent intent = new Intent(Viewpager.this, ViewpagerInfo.class);
				//currentItem == 0  时为普通卡牌,currentItem == 1  时为金色卡牌
				if (currentItem == 0) {
					intent.putExtra("cardname", normalName);
				} else {
					intent.putExtra("cardname", goldenName);
				}
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	
	private static String toNormalCardName(String ename) {
		String g = ename.substring(0, 2);
		if (g.equals("g-")) {
			return ename.substring(2);
		} else {
			return ename;
		}
	}
}
