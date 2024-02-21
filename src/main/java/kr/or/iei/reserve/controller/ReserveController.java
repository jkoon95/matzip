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
		//받아온 정보 : memberNo, storeNo 이정도?
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
		 * 만석인 날짜
		 * String query = "select reserve_time, count(*) from reserve_tbl where store_no = 2 group by reserve_time";
		 * 
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
