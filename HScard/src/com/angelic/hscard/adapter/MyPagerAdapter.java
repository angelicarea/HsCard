package com.angelic.hscard.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * @classname MyPagerAdapter
 * @description ViewPager适配器
 * @author By Chestnut(lishq)
 * @date Jan 3, 2014 2:40:02 PM
 */
public class MyPagerAdapter extends PagerAdapter {
	List<View> list = new ArrayList<View>();

	/**
	 * 构造方法，参数是我们的页卡，这样比较方便。
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param list
	 */
	public MyPagerAdapter(ArrayList<View> list) {
		this.list = list;
	}

	/**
	 * 返回页卡数量
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 判断一个对象,是否是一个页卡(View)
	 * 返回true/false
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	/**
	 * 删除页卡  
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		ViewPager pViewPager = ((ViewPager) container);
        pViewPager.removeView(list.get(position));
	}
	
	/**
	 * 实例化页卡
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ViewPager pViewPager = ((ViewPager) container);
        pViewPager.addView(list.get(position));
        return list.get(position);
	}
}
