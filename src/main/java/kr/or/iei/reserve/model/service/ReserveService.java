package kr.or.iei.reserve.model.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.reserve.model.dao.ReserveDao;
import kr.or.iei.reseve.model.dto.TempClosedDay;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;

@Service
public class ReserveService {

	@Autowired
	private ReserveDao reserveDao;
	
	//임시
	public Store searchStore(int storeNo) {
		Store store = reserveDao.searchStore(storeNo);
		return store;
	}

	//임시로
	public List<Menu> searchMenu(int storeNo) {
		List<Menu> menu = reserveDao.searchMenu(storeNo);
		return menu;
	}
	
	public List<String> fullDays(Store store) {
		//1. dayTotalCount 구하기
		//한 테이블의, 한 날짜 기준 영업시간 내에서, 30분 단위로 예약 가능한 총 갯수
		int dayTotalCount = 0;
		//00:00부터 openingHour까지 30분 단위로 예약 가능한 횟수
		int openingCount = (Integer.parseInt(store.getOpeningHour().substring(0,2)) * 2) + (Integer.parseInt(store.getOpeningHour().substring(3,5)) / 30);
		//00:00부터 closingHour까지 30분 단위로 예약 가능한 횟수
		int closingCount = (Integer.parseInt(store.getClosingHour().substring(0,2)) * 2) + (Integer.parseInt(store.getClosingHour().substring(3,5)) / 30);
		//00:00부터 breakStart, breakEnd 까지 30분 단위로 예약 가능한 횟수(휴게시간 없다면 0으로 되도록 세팅)
		int breakStartCount = 0;
		int breakEndCount = 0;
		if(store.getBreakStart() != "-1") { //사장이 breakStart를 선택 안 하면 기본 value가 문자열 "-1"이 되도록 세팅해놨음. 즉 휴게시간이 없을떄 -1임.
			breakStartCount = (Integer.parseInt(store.getBreakStart().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakStart().substring(3,5)) / 30);
			breakEndCount = (Integer.parseInt(store.getBreakEnd().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakEnd().substring(3,5)) / 30);
		};
		//이하 대소비교시, openingCount가 openingHour를 대체하고(close도 동일), breakStartCount가 breakStart를 대체함(end도 동일)
		//경우의 수에 따른 dayTotalCount 찾기 시작
		/*
		if(openingCount < closingCount) {//당일 열고 당일 닫는 경우 또는 24시간 운영하는 경우
			//if(store.getBreakStart() != "-1") {//휴게시간이 있는 경우
				//dayTotalCount = (closingCount - breakEndCount) + (breakStartCount - openingCount);
			//}else {//휴게시간이 없는 경우
				//dayTotalCount = closingCount - openingCount;
			//}
			//계산해보니 휴게시간이 없으면 breakCount == 0 이어서, 더하나 빼나 상관 없네? 휴게시간 있는 경우와 없는 경우 모두 하나의 수식으로 합치자 ↓
			dayTotalCount = closingCount - breakEndCount + breakStartCount - openingCount;
		}else {//당일 열고 익일 닫는 경우
			if(store.getBreakStart() != "-1") {//휴게시간이 있는 경우
				if(breakStartCount < breakEndCount) { //휴게시간 시작과 종료가 모두 같은 날인 경우
					//if(openingCount < breakStartCount) { //휴게시간 시작과 종료가 opening날짜와 같은 경우(당일인 경우)
						//dayTotalCount = (48 - breakEndCount) + (breakStartCount - openingCount) + (closingCount - 0);
					//}else {//휴게시간 시작과 종료가 closing날짜와 같은 경우(익일인 경우)
						//dayTotalCount = (48 - openingCount) + (closingCount - breakEndCount) + (breakStartCount - 0);
					//}
					//계산해보니 위 두 경우의 수의 수식이 똑같아서, 하나의 수식으로 합치자 ↓
					dayTotalCount = 48 + closingCount - openingCount - breakEndCount + breakStartCount;
				}else { //휴게시간 시작은 당일이고 종료는 익일인 경우(breakStart > breakEnd)
					dayTotalCount = (breakStartCount - openingCount) + (closingCount - breakEndCount);
				}
			}else {//휴게시간이 없는 경우
				dayTotalCount = 48 + closingCount - openingCount;
			}
		};
		*/
		//dayTotalCount 구하기 최종 요약
		if ( (openingCount < closingCount) || (breakStartCount > breakEndCount) ) {
			dayTotalCount = closingCount - breakEndCount + breakStartCount - openingCount;
		}else {
			dayTotalCount = 48 + closingCount - openingCount - breakEndCount + breakStartCount;
		}
		
		//2. 만석인 날짜 구하기
		int tableAmount = reserveDao.tableAmount(store.getStoreNo());
		int allCount = tableAmount*dayTotalCount;
		List<String> fullDays = reserveDao.fullDays(store.getStoreNo(), allCount);
		
		return fullDays;
	}

	public HashMap<String, List<String>> fullTimes(Store store, List<String> fullDays) {
		//1. 예약 가능 날짜 구하기
		//오늘(=0일)부터 14일 후까지의 날짜 배열 리스트 만들기
		List<String> reserveAbleDays = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		for(int i=0; i<15; i++) {
			cal.setTime(new Date());//오늘날짜
			cal.add(Calendar.DAY_OF_YEAR, i);//오늘 날짜에서 i일 더하기
			String dateToStr = String.format("%1$tY-%1$tm-%1$td", cal);//cal를 문자열로 형변환 "yyyy-mm-dd"
			reserveAbleDays.add(dateToStr);
		}
		//최종 : 차집합으로 진짜 예약가능날짜만 구하기
		reserveAbleDays.removeAll(fullDays);
		
		//2. 예약 가능 날짜의 만석인 시각 구하기
		//key : reserveAbleDays[i], value : reserveFullTimes[]
		//reserve_tbl에선 예약횟수가 0인 시각의 time을 select할 수가 없어서, 예약가능시각이 아니라 불가피하게(지금 내 능력의 한계...) 만석인 시각을 먼저 구함 
		int tableAmount = reserveDao.tableAmount(store.getStoreNo());
		HashMap<String, List<String>> fullTimes = reserveDao.fullTime(store.getStoreNo(), reserveAbleDays, tableAmount);
		
		return fullTimes;
	}

	public List<Integer> closedDays(int storeNo) {
		List<ClosedDay> list = reserveDao.closedDays(storeNo);
		List<Integer> closedDays = new ArrayList<Integer>();
		for(ClosedDay item : list) {
			switch(item.getClosedDay()) {
			case "일" :
				closedDays.add(0);
				break;
			case "월" :
				closedDays.add(1);
				break;
			case "화" :
				closedDays.add(2);
				break;
			case "수" :
				closedDays.add(3);
				break;
			case "목" :
				closedDays.add(4);
				break;
			case "금" :
				closedDays.add(5);
				break;
			case "토" :
				closedDays.add(6);
				break;
			default:
				break;
			}
		}
		return closedDays;
	}

	public List<String> tempClosedDays(int storeNo) {
		List<TempClosedDay> list = reserveDao.tempClosedDays(storeNo);
		List<String> tempClosedDays = new ArrayList<String>();
		for(TempClosedDay item : list) {
			tempClosedDays.add(item.getTempClosedDay());
		}
		return tempClosedDays;
	}

	
}
