package kr.or.iei.store.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.store.model.dao.StoreDao;

@Service
public class StoreService {
	@Autowired
	private StoreDao storeDao;

	public List selectAllSubway() {
		List list = storeDao.selectAllSubway();
		return list;
	}
}
