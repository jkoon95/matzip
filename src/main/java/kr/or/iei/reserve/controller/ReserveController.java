package kr.or.iei.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.iei.reserve.model.service.ReserveService;
import kr.or.iei.reseve.model.dto.ReserveFrm;
import kr.or.iei.reseve.model.dto.TimeSet;

@RequestMapping(value="/reserve")
@Controller
public class ReserveController {

	@Autowired
	private ReserveService reserveService;
	
	@RequestMapping(value="/reserveFrm")
	private String reserveFrm(Model model) {
		//매개변수 : @SessionAttribute(required = false) Member member, int storeNo, Menu menu, Model model
		//받아온 정보 : member, store, menu
		
		//원래 매개변수인데, 일단 임시로
		int storeNo = 2;;
		
		ReserveFrm reserveFrm = reserveService.reserveFrm(storeNo);
		
		model.addAttribute("store", reserveFrm.getStore());
		model.addAttribute("menu", reserveFrm.getMenu());
		model.addAttribute("fullDays", reserveFrm.getFullDays());
		
		return "reserve/reserveFrm";
	}
	
	@ResponseBody
	@PostMapping(value="/closedDays")
	private List<Integer> closedDays(int storeNo){
		//매개변수 : int storeNo
		//정기휴무일 구하기
		List<Integer> closedDays = reserveService.closedDays(storeNo);
		return closedDays;
	}
	
	@ResponseBody
	@PostMapping(value="/tempClosedDays")
	private List<String> tempClosedDays(int storeNo){
		//매개변수 : int storeNo
		//임시휴무일 구하기
		List<String> tempClosedDays = reserveService.tempClosedDays(storeNo);
		return tempClosedDays;
	}

	@ResponseBody
	@PostMapping(value="/timeSet")
	private TimeSet timeSet(int storeNo, String day){
		//매개변수 : int storeNo, String Day(datepicker에서 선택한 날짜)
		TimeSet timeSet = reserveService.timeset(storeNo, day);
		return timeSet;
	}
	
	@PostMapping(value="/reserveList")
	private String reserveList(String reserveDate, Model model) {
		//넘겨받은 정보 : storNo, memberNo, 
		//입력받은 정보 : reserveDate(yyyy-mm-dd), reserveTime, reservePeople, menuNo, servings, reserveRequest
		model.addAttribute("reserveDate", reserveDate);
		return "reserve/reserveList";
	}
	
}
