package com.angelic.hscard.service;

import java.util.List;

import com.angelic.hscard.model.HsGroupInfo;

public interface HsGroupInfoService {

	public List<HsGroupInfo> getAllGroup();
	public int addHsGroup(HsGroupInfo info);
	public int editHsGroup(HsGroupInfo info);
	public int delHsGroup(String id);
}
