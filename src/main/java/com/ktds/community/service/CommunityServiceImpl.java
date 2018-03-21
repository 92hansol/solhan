package com.ktds.community.service;

import java.util.ArrayList;
import java.util.List;

import com.ktds.community.dao.CommunityDao;
import com.ktds.community.vo.CommunityVO;

public class CommunityServiceImpl implements CommunityService {
	
	private CommunityDao communityDao;
	
	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	@Override
	public List<CommunityVO> getAll(){
		return communityDao.selectAll();
	}

	@Override
	public boolean createCommunity(CommunityVO communityVO) {
		//html 띄어쓰기 바꾸기 \n-> br/
		String body = communityVO.getBody();
		
		body = body.replace("\n", "<br/>");
		//body다시 넣어주기
		communityVO.setBody(body);
		
//		if( filter(body) || filter(communityVO.getTitle())) {
//			
//			return false;
//			
//		}
		
		int insertCount = communityDao.insertCommunity(communityVO);
		return insertCount > 0;
				
	}

	@Override
	public CommunityVO getOne(int id) {
//		if( communityDao.incrementViewCount(id) > 0 ) {
				//업데이트를 잘 수행했다면. select One을 수행해라.
			return communityDao.selectOne(id);
//		}
//		return null;
	}

	@Override
	public boolean incrementVC(int id) {
		if ( communityDao.incrementViewCount(id) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean incrementRC(int id) {
		if (communityDao.incrementRecommendCount(id) >0 ) {
			return true;
		}
		return false;
	}

	public boolean filter(String str) {
		List<String> blackList = new ArrayList<String>();
		blackList.add("욕");
		blackList.add("씨");
		blackList.add("개");
		blackList.add("2식");
		blackList.add("1식");
		
		//str ==> 남편은 2식이에요
		String[] splitString = str.split(" ");
		
		for( String word : splitString ) {
			
			for(String blackString : blackList) {
				if( word.contains(blackString)) {
					return true;
				}
			}
		}
			return true;
	}

	@Override
	public boolean removePage(int id) {
		if (communityDao.deletePage(id) > 0 ) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCommunity(CommunityVO communityVO) {
		if (communityDao.updateCommunity(communityVO) > 0) {
			return true;	
		}
		return false;
	}

	@Override
	public int readfMyCommunitiesCount(int userId) {
		return communityDao.selectMyCommunitiesCount(userId) ;
	}

	@Override
	public List<CommunityVO> readMyCommunities(int userId) {
		return communityDao.selectMyCommunities(userId);
	}

	@Override
	public boolean deleteCommunities(List<Integer>ids, int userId) {
		return communityDao.deleteCommunities(ids, userId) >0;
	}
	
}
