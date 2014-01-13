package com.angelic.hscard.activity;

import com.angelic.hscard.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class CardGroup extends Activity {
	private GridView gridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_tool);
		
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setVerticalScrollBarEnabled(false);
	}
}
