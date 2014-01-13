package com.angelic.hscard.activity;

import com.angelic.hscard.R;
import com.angelic.hscard.dao.HsCardDao;
import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.service.HsCardService;
import com.angelic.hscard.utils.GsonUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @classname ViewpagerInfo
 * @description 浏览卡牌信息
 * @author By Chestnut(lishq)
 * @date Dec 28, 2013 10:22:23 PM
 */
public class ViewpagerInfo extends Activity {

	private RelativeLayout titleLayout;
	private WebView webView;
	private TextView txtName;
	private Button btnBack;
	private String cardname,json;
	private HsCard card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.card_info);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 头部事件
		titleLayout = (RelativeLayout) findViewById(R.id.title);
		titleLayout.setOnClickListener(onClick);
		// 回退事件
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClick);
		// 本地HTML初始化
		webView = (WebView) findViewById(R.id.cardhtml);
		Intent intent = getIntent();
		// 接收意图Intent传值normalName普通卡牌和金色卡牌的英文名
		cardname = intent.getStringExtra("cardname");
		txtName = (TextView)findViewById(R.id.txt_name);
		
		card = serviceHsCard();
		txtName.setText(card.getName());
		json = GsonUtil.objectToJson(card);
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		// TODO Auto-generated method stub
		// 允许使用Javascript
		webView.getSettings().setJavaScriptEnabled(true);

		WebSettings settings = webView.getSettings();
		//Android中Webview自适应屏幕(HTML中,body的css样式设置为"margin: 0 auto;")
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity == 120) {
			settings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == 160) {
			settings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 240) {
			settings.setDefaultZoom(ZoomDensity.FAR);
		}
		// 背景透明2.X版本(无需设置硬件加速)
		webView.setBackgroundColor(0);
		
		webView.addJavascriptInterface(JavaScriptInterface(),"cardinfo");
		// Assets文件夹下本地的HTML
		webView.loadUrl("file:///android_asset/www/cards/card.html");
	}

	public Object JavaScriptInterface(){
		Object insertObj = new Object(){ 
			@SuppressWarnings("unused")
			public String getjson(){
				return json;
			}
		};
		return insertObj;
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
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	private HsCard serviceHsCard(){
		HsCardService service = new HsCardDao(this);
		return service.getCardByEname(cardname);
	}
}

