package kr.or.iei.reserve.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.reserve.model.service.ReserveService;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;

@RequestMapping(value="/reserve")
@Controller
public class ReserveController {

	@Autowired
	private ReserveService reserveService;
	
	@RequestMapping(value="/reserveFrm")
	private String reserveFrm(Model model) {
		//매개변수 : @SessionAttribute(required = false) Member member, Store store, Menu menu, Model model
		//받아온 정보 : member, store, menu
		
		//원래 매개변수인데, 일단 임시로
		//int memberNo = 25;
		int storeNo = 2;
		Store store = reserveService.searchStore(storeNo);
		List<Menu> menu = reserveService.searchMenu(storeNo);
		
		
		//만석인 날짜
		List<String> fullDays = reserveService.fullDays(store);
		
		//예약 가능한 날짜의 만석인 시각 구하기
		HashMap<String, List<String>> fullTimes = reserveService.fullTimes(store, fullDays);
		
		//정기휴무일 구하기
		List<Integer> closedDays = reserveService.closedDays(store.getStoreNo());
		
		//임시휴무일 구하기
		List<String> tempClosedDays = reserveService.tempClosedDays(store.getStoreNo());
		
		model.addAttribute("store", store);
		model.addAttribute("menu", menu);
		model.addAttribute("fullDays", fullDays);
		model.addAttribute("fullTimes", fullTimes);
		model.addAttribute("closedDays", closedDays);
		model.addAttribute("tempClosedDays", tempClosedDays);
		
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
