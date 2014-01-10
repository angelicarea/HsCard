package com.angelic.hscard.view;

import com.angelic.hscard.R;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MyCardListLayout extends RelativeLayout {
	
	private Activity act;
	private Button test1,test2;
	private RelativeLayout relativeLayout;

	public MyCardListLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public void init(Context context) {
		// TODO Auto-generated method stub
		test1 = (Button)findViewById(R.id.btn_test1);
		test2= (Button)findViewById(R.id.btn_test2);
		
	}
	public void setActivity(Activity act){
		this.act = act;
	}
	

}
