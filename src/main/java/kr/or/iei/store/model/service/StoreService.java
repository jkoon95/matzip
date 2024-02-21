package kr.or.iei.store.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.iei.store.model.dao.StoreDao;
import kr.or.iei.store.model.dto.ClosedDay;
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
	
	@Transactional
	public int insertStore(Store store, List<EvidenceFile> evidenceFileList, String[] closedDays, List<Menu> menuList) {
		//매장등록
		int result = storeDao.insertStore(store);
		if(result>0) {
			//등록매장번호 가져오기
			int storeNo = storeDao.selectStoreNo();
			//증빙서류등록
			for(EvidenceFile evidenceFile : evidenceFileList) {
				evidenceFile.setStoreNo(storeNo);
				result += storeDao.insertEvidenceFile(evidenceFile);				
			}
			//휴무일등록
			if(closedDays != null) {
				for(String closedDay : closedDays) {
					result += storeDao.insertClosedDay(storeNo,closedDay);					
				}
			}
			//메뉴등록		
			for(Menu menu : menuList) {
				menu.setStoreNo(storeNo);
				result += storeDao.insertMenu(menu);
			}
		}
		return result;
	}
}
