package com.ktds.community.service;

import java.util.List;

import com.ktds.community.vo.CommunityVO;

public interface CommunityService {
	
	public List<CommunityVO> getAll();
	
	public CommunityVO getOne(int id);
	
	public int readfMyCommunitiesCount(int userId);
	
	public boolean incrementVC(int id);
	
	public boolean incrementRC(int id);
	
	public boolean createCommunity(CommunityVO communityVO);
	
	public boolean removePage(int id);
	
	public boolean updateCommunity(CommunityVO communityVO);
	
	public List<CommunityVO> readMyCommunities(int userId);
 
	public boolean deleteCommunities(List<Integer>ids, int userId);
	
}
