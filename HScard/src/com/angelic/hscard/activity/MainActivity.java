package com.angelic.hscard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.angelic.hscard.R;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
    	//全屏显示
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    			WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	new Handler().postDelayed(new Runnable(){

    		@Override
            public void run(){
                Intent intent = new Intent ();
                intent.setClass(MainActivity.this,Welcome.class);
                startActivity(intent);
                finish();//结束本Activity
            }
        }, 2000);
	}
}