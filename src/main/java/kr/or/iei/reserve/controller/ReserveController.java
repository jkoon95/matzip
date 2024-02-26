package kr.or.iei.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.or.iei.reserve.model.service.ReserveService;
import kr.or.iei.reseve.model.dto.Reserve;
import kr.or.iei.reseve.model.dto.ReserveFrm;
import kr.or.iei.reseve.model.dto.TableNoAndCapacity;
import kr.or.iei.reseve.model.dto.TimeSet;

@RequestMapping(value="/reserve")
@Controller
public class ReserveController {

	@Autowired
	private ReserveService reserveService;
	
	@RequestMapping(value="/reserveFrm")
	private String reserveFrm(Model model) {
		//매개변수 : @SessionAttribute(required = false) Member member, int storeNo, Menu menu, Model model
		
		//원래 매개변수인데, 일단 임시로
		int storeNo = 1;
		
		ReserveFrm reserveFrm = reserveService.reserveFrm(storeNo);
		
		model.addAttribute("store", reserveFrm.getStore());
		model.addAttribute("menus", reserveFrm.getMenus());
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
	private TimeSet timeSet(int storeNo, String selectedDay){
		//매개변수 : int storeNo, String Day(datepicker에서 선택한 날짜)
		TimeSet timeSet = reserveService.timeset(storeNo, selectedDay);
		return timeSet;
	}
	
	@ResponseBody
	@PostMapping(value="/tableNoAndCapacity")
	private List<TableNoAndCapacity> tableNoAndCapacity(int storeNo, String reserveDate, String reserveTime) {
		//식탁 수용가능 인원수가 적은 것 부터 index 0 번에 배치됨
		List<TableNoAndCapacity> tableNoAndCapacity = reserveService.tableNoAndCapacity(storeNo, reserveDate, reserveTime);
		return tableNoAndCapacity;
	}
	
	@PostMapping(value="/reserve")
	private String reserve(Reserve reserve, int[] menuNo, int[] servings, Model model) {
		/* 받아온 정보
		 * @SessionAttribute(required = false) Member member
		 * int[] menuNo
		 * int[] servings
		 * <이하 Reserve객체로 받아온 것들>
		 * int storeNo
		 * String reserveDate
		 * String reserveTime
		 * int reservePeople
		 * int tableNo
		 * String reserveRequest
		 */
		reserve.setMemberNo(3); //원래는 reserve.setMemberNo(Member.getMemberNo()) 이렇게 세션에 있는 정보를 넣어야... 일단 임시로 넣었음.
		reserve.setReserveStatus(1);
		int insertResult = reserveService.insertReserve(reserve, menuNo, servings);
		return "reserve/reserveList";
	}
	
	
}
