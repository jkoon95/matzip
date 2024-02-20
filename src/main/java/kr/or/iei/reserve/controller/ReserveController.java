package kr.or.iei.reserve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.iei.reserve.model.service.ReserveService;

@RequestMapping(value="/reserve")
@Controller
public class ReserveController {

	@Autowired
	private ReserveService reserveService;
	
	@RequestMapping(value="/reserveFrm")
	private String reserveFrm() {
		//넘겨받은 정보 : memberNo, storeNo 이정도?
		/*
		 * 화면 구성에 필요한 정보
		 * store_tbl : storeName, openingHour, closingHour, breakStart, breakEnd, 
		 * closed_
		 */
		return "reserve/reserveFrm";
	}
	
	@PostMapping(value="/reserveList")
	private String reserveList(String reserveDate, Model model) {
		//넘겨받은 정보 : storNo, memberNo, 
		//입력받은 정보 : reserveDate(yyyy-mm-dd), reserveTime, reservePeople, menuNo, servings, reserveRequest
		model.addAttribute("reserveDate", reserveDate);
		return "reserve/reserveList";
	}
	
}
