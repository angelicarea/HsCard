package com.angelic.hscard.service;

import java.util.List;

import com.angelic.hscard.model.HsCard;

public interface HsCardService {

	/**
	 * 
	 * @Title getListrCardByModelOR
	 * @Description 通过卡牌类别,种族,技能类型多条件查询结果
	 * @param card
	 *            传入实体类HsCard,这是查询条件,单属性多条件用','分割
	 * @return 查询结果集合List<HsCard>
	 */
	public List<HsCard> getListrCardByModelOR(HsCard card);

	public List<HsCard> getListrCardByModelAND(HsCard card);
}
