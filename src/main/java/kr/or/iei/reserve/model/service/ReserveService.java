package kr.or.iei.reserve.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.reserve.model.dao.ReserveDao;

@Service
public class ReserveService {

	@Autowired
	private ReserveDao reserveDao;
	
	
}
