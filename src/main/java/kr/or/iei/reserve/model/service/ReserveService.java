package kr.or.iei.reserve.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.reserve.model.dao.ReserveDao;
import kr.or.iei.store.model.dto.Store;

@Service
public class ReserveService {

	@Autowired
	private ReserveDao reserveDao;

	public List reserveFrm(Member member, Store store) {
		List list = new ArrayList();
		int tableAmount = reserveDao.tableAmount(store.getStoreNo());
		
		String openingHour = store.getOpeningHour().substring(0, 2);
		String closingHour = store.getClosingHour().substring(3, 5);
		String breakStart = store.getBreakStart().substring(0, 2);
		String breakEnd = store.getBreakEnd().substring(3, 5);
		int timeToEat = store.getTimeToEat();
		
		List<String> fullDays = reserveDao.fullDays(store, tableAmount);
		
		return list;
	}
	
	
}
