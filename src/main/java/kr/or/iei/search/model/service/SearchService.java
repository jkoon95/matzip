package kr.or.iei.search.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.search.model.dao.SearchDao;
import kr.or.iei.store.model.dto.Store;

@Service
public class SearchService {
	@Autowired
	private SearchDao searchDao;

	public int storeTotalCount(String stationName) {
		int totalCount = searchDao.searchTotalCount(stationName);
		return totalCount;
	}

	public List selectSearchList(int start, int amount,String stationName) {
		int end = start+amount-1;
		List searchList = searchDao.selectSearchList(start,end, stationName);
		return searchList;
	}

	public Store selectSearchOne(int storeNo) {
		Store store = searchDao.selectSearchOne(storeNo);
		return store;
	}
	

}
