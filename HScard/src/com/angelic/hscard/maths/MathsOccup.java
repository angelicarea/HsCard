package com.angelic.hscard.maths;

import java.util.ArrayList;
import java.util.List;

import com.angelic.hscard.model.HsCard;
import com.angelic.hscard.model.HsLeftMenu;

public class MathsOccup {
	
	/**
	 * 计算Card职业和职业数量
	 * @Title getTypeByHsList
	 * @Description 
	 * @param list
	 * @return
	 */
	public static List<HsLeftMenu> getTypeByHsList(List<HsCard> list){
		List<HsLeftMenu> listMenu = new ArrayList<HsLeftMenu>();
		int tag = 0;
		for(HsCard card : list){
			tag = 0;
			card.getOccupation();
			for(HsLeftMenu menu :listMenu){
				if(menu.getName().equals(card.getOccupation())){
					menu.setCount(menu.getCount()+1);
					tag = 1;
				}
			}
			if(tag == 0){
				HsLeftMenu tMenu = new HsLeftMenu();
				tMenu.setName(card.getOccupation());
				tMenu.setCount(1);
				listMenu.add(tMenu);
			}
		}
		return listMenu;
	}
}
