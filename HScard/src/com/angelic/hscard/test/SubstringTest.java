package com.angelic.hscard.test;

import android.test.AndroidTestCase;

public class SubstringTest extends AndroidTestCase{

	public  String toNormalCardName(String ename){
		String g = ename.substring(0, 2);
		if(g.equals("g-")){
			return ename.substring(2);
		}else{
			return ename;
		}
	}
	
	public void test(){
		String name = toNormalCardName("g-abcdefg+dd");
		System.out.println(name);
	}
}
