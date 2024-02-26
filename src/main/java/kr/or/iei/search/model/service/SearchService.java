package kr.or.iei.search.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.search.model.dao.SearchDao;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfo;
import kr.or.iei.store.model.dto.StoreReview;

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

	public StoreInfo getStoreInfoByStoreNo(int storeNo) {
		StoreInfo storeInfo = searchDao.getStoreInfoByStoreNo(storeNo);
		return storeInfo;
	}

	public List<Menu> selectAllMenu(int storeNo) {
		List<Menu> menuList = searchDao.selectAllMenu(storeNo);
		return menuList;
	}

	public List<ClosedDay> selectClosedDay(int storeNo) {
		List<ClosedDay> closedDay = searchDao.selectClosedDay(storeNo);
		return closedDay;
	}

	@Transactional
	public int updateInfo(StoreInfo i) {
		int result = searchDao.updateInfo(i);
		return result;
	}

	@Transactional
	public int insertInfo(StoreInfo i) {
		int result = searchDao.insertInfo(i);
		return result;
	}

	public List<StoreReview> selectStoreReview(int storeNo) {
		List<StoreReview> reviewList = searchDao.selectStoreReview(storeNo);
		return reviewList;
	}

	@Transactional
	public int insertReview(StoreReview sr) {
		int result = searchDao.insertReview(sr); 
		return result;
	}

	@Transactional
	public int updateReview(StoreReview sr) {
		int result = searchDao.updateReview(sr);
		return result;
	}

	@Transactional
	public int deleteReview(int reviewNo) {
		int result = searchDao.deleteReview(reviewNo);
		return result;
	}

}
