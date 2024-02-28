package kr.or.iei.reserve.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.reserve.model.service.ReserveService;
import kr.or.iei.reseve.model.dto.MenuServings;
import kr.or.iei.reseve.model.dto.Reserve;
import kr.or.iei.reseve.model.dto.ReserveFrm;
import kr.or.iei.reseve.model.dto.ReserveViewMember;
import kr.or.iei.reseve.model.dto.TableNoAndCapacity;
import kr.or.iei.reseve.model.dto.TimeSet;

@RequestMapping(value="/reserve")
@Controller
public class ReserveController {

	@Autowired
	private ReserveService reserveService;
	
	@PostMapping(value="/reserveFrm")
	public String reserveFrm(@SessionAttribute(required = false) Member member, int storeNo, Model model) {
		
		ReserveFrm reserveFrm = reserveService.reserveFrm(storeNo);
		
		model.addAttribute("store", reserveFrm.getStore());
		model.addAttribute("menus", reserveFrm.getMenus());
		model.addAttribute("fullDays", reserveFrm.getFullDays());
		
		return "reserve/reserveFrm";
	}
	
	@ResponseBody
	@PostMapping(value="/closedDays")
	public List<Integer> closedDays(int storeNo){
		//정기휴무일 구하기
		List<Integer> closedDays = reserveService.closedDays(storeNo);
		return closedDays;
	}
	
	@ResponseBody
	@PostMapping(value="/tempClosedDays")
	public List<String> tempClosedDays(int storeNo){
		//임시휴무일 구하기
		List<String> tempClosedDays = reserveService.tempClosedDays(storeNo);
		return tempClosedDays;
	}

	@ResponseBody
	@PostMapping(value="/timeSet")
	public TimeSet timeSet(int storeNo, String selectedDay){
		//String Day = datepicker에서 선택한 날짜
		TimeSet timeSet = reserveService.timeset(storeNo, selectedDay);
		return timeSet;
	}
	
	@ResponseBody
	@PostMapping(value="/tableNoAndCapacity")
	public List<TableNoAndCapacity> tableNoAndCapacity(int storeNo, String reserveDate, String reserveTime) {
		//식탁 수용가능 인원수가 적은 것 부터 index 0 번에 배치됨
		List<TableNoAndCapacity> tableNoAndCapacity = reserveService.tableNoAndCapacity(storeNo, reserveDate, reserveTime);
		return tableNoAndCapacity;
	}
	
	@PostMapping(value="/reserveInsert")
	public String reserve(@SessionAttribute(required = false) Member member, Reserve reserve, int[] menuNo, int[] servings, Model model) {
		/* 받아온 정보
		 * @SessionAttribute(required = false) Member member
		 * Reserve reserve
		 * int[] menuNo
		 * int[] servings
		 * <이하 Reserve객체 내에서 받아온 것들>
		 * int storeNo
		 * String reserveDate
		 * String reserveTime
		 * int reservePeople
		 * int tableNo
		 * String reserveRequest
		 */
		//int memberNo = 3;//임시로
		reserve.setMemberNo(member.getMemberNo());
		reserve.setReserveStatus(1);
		int insertResult = reserveService.insertReserve(reserve, menuNo, servings);
		return "redirect:/reserve/reserveList";
	}
	
	//나중에 post로 바꿔
	@PostMapping(value="/reserveList")
	public String reserveList(@SessionAttribute(required = false) Member member, Model model) {
		//int memberNo = 3;//임시로
		
		HashMap<String, List> rvmList = reserveService.reserveViewMemberList(member.getMemberNo());
		
		
		List<ReserveViewMember> afterRvmList = rvmList.get("after");
		List<ReserveViewMember> beforeRvmList = rvmList.get("before");
		List<MenuServings> menuServings = rvmList.get("menuServings");
		model.addAttribute("afterRvmList", afterRvmList);
		model.addAttribute("beforeRvmList", beforeRvmList);
		model.addAttribute("menuServings", menuServings);
		
		return "reserve/reserveList";
	}
	
	@ResponseBody
	@PostMapping(value="/cancelReserve")
	public int deleteReserve(Integer reserveNo) {
		int result = reserveService.cancelReserve(reserveNo);
		return result;
	}
	
}
