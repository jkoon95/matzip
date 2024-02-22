package kr.or.iei.reserve.controller;

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
	private String reserveFrm(@SessionAttribute(required = false) Member member, Store store, Menu menu, Model model) {
		//매개변수 : @SessionAttribute(required = false) Member member, Store store, Menu menu, Model model
		//받아온 정보 : member, store, menu
		int memberNo = 25;
		int storeNo = 2;
		/*
		 * 보내줄 정보
		 * storeName, openingHour, closingHour, breakStart, breakEnd
		 * 예약불가일 : 정기휴무일, 임시휴무일, 만석인 날짜(아래)
		 * 만석 아닌 날의 날짜별 남은 table_no
		 * 
		 */
		/*
		 * 만석인 날짜와 만석인 시각 동시 조회
		 * 시각별 예약횟수 쿼리 : select reserve_time, count(*) reserve_time_count from reserve_tbl where store_no = 2 and reserve_date = 'yyyy-mm-dd' and reserve_status != 3 group by reserve_time order by reserve_time;
		 * 식탁수 쿼리 : select count(*) table_count from table_tbl store_no = ? group by store_no; 
		 * 시각별 예약횟수와 식탁수 비교 (같으면 그 시각 만석)
		 */
		/*
		 * 만석 아닌 시각의 잔여 식탁번호 조회
		 * select table_no from table_tbl where store_no = 2
		 * minus
		 * select table_no from reserve_tbl where store_no = 2 and reserve_date = '2024-02-29' and reserve_time = '12:00' and reserve_status != 3;
		 */
		
		//만석인 날짜 조회
		List list= reserveService.reserveFrm(member, store);
		
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
