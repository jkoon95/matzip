package kr.or.iei.store.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.store.model.dao.StoreDao;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;

@Service
public class StoreService {
	@Autowired
	private StoreDao storeDao;

	public List selectAllSubway() {
		List list = storeDao.selectAllSubway();
		return list;
	}

	public List selectTopStore(String stationName,int number) {
		List list = storeDao.selectTopStore(stationName,number);
		return list;
	}

	public int insertStore(Store store, List<EvidenceFile> evidenceFileList, String[] closedDays, List<Menu> menuList) {
		//매장등록(먼저 insert해서 등록된 번호 가져와야 나머지 insert 가능)
		
		//휴무일등록
		
		//증빙서류등록
		
		//메뉴등록
		
		return 0;
	}
}
