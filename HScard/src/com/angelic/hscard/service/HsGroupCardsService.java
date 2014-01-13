package com.angelic.hscard.service;

import java.util.List;

import com.angelic.hscard.model.HsGroupCards;

public interface HsGroupCardsService {
	public List<HsGroupCards> getCardsByGroupId(String groupid);
	public int addHsGroupCards(HsGroupCards info);
	public int editHsGroupCards(HsGroupCards info);
	public int delHsGroupCards(String id);
}
