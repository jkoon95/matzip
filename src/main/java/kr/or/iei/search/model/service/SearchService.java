package kr.or.iei.search.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.search.model.dao.SearchDao;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfo;

@Service
public class SearchService {
	@Autowired
	private SearchDao searchDao;

	public int storeTotalCount(String stationName) {
		int totalCount = searchDao.searchTotalCount(stationName);
		return totalCount;
	}
	
	public List selectTopStore(String stationName,int number,int memberNo) {
		List list = searchDao.selectTopStore(stationName,number,memberNo);
		return list;
	}

	public List selectSearchList(int start, int amount,String stationName) {
		int end = start+amount-1;
		List searchList = searchDao.selectSearchList(start,end, stationName);
//		int isLike = searchDao.selectSearchStoreIsLike
		return searchList;
	}

	public Store selectSearchOne(int storeNo) {
		Store store = searchDao.selectSearchOne(storeNo);
		return store;
	}

	public StoreInfo getInfoByStoreNo(int storeNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public Menu getMenuByStoreNo(int storeNo) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
