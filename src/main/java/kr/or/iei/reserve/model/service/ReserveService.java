package kr.or.iei.reserve.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.reserve.model.dao.ReserveDao;
import kr.or.iei.reseve.model.dto.ReserveFrm;
import kr.or.iei.reseve.model.dto.TableNoAndCapacity;
import kr.or.iei.reseve.model.dto.TempClosedDay;
import kr.or.iei.reseve.model.dto.TimeSet;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;

@Service
public class ReserveService {

	@Autowired
	private ReserveDao reserveDao;
	
	public ReserveFrm reserveFrm(int storeNo) {
		
		//<1> store 구하기
		Store store = reserveDao.searchStore(storeNo);
		
		//<2> menu 구하기
		List<Menu> menu = reserveDao.searchMenu(storeNo);
		
		//<3> fullDays 구하기(=만석인 날짜)
		//<3> - 1. dayTotalCount 구하기
		//한 테이블의, 한 날짜 기준 영업시간 내에서, 30분 단위로 예약 가능한 총 갯수
		int dayTotalCount = 0;
		//00:00부터 openingHour까지 30분 단위로 예약 가능한 횟수
		int openingCount = (Integer.parseInt(store.getOpeningHour().substring(0,2)) * 2) + (Integer.parseInt(store.getOpeningHour().substring(3,5)) / 30);
		//00:00부터 closingHour까지 30분 단위로 예약 가능한 횟수
		int closingCount = (Integer.parseInt(store.getClosingHour().substring(0,2)) * 2) + (Integer.parseInt(store.getClosingHour().substring(3,5)) / 30);
		//00:00부터 breakStart, breakEnd 까지 30분 단위로 예약 가능한 횟수(휴게시간 없다면 0으로 되도록 세팅)
		int breakStartCount = 0;
		int breakEndCount = 0;
		if(!store.getBreakStart().equals("-1")) { //사장이 breakStart를 선택 안 하면 기본 value가 문자열 "-1"이 되도록 세팅해놨음. 즉 휴게시간이 없을떄 -1임.
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
		//<3> - 2. 만석인 날짜 구하기
		int tableAmount = reserveDao.tableAmount(storeNo);
		int allCount = tableAmount*dayTotalCount;
		List<String> fullDays = reserveDao.fullDays(storeNo, allCount);
		
		
		ReserveFrm reserveFrm = new ReserveFrm(store, menu, fullDays);
		
		return reserveFrm;
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
	
	public TimeSet timeset(int storeNo, String selectedDay) {
		//<1> allTimes 전체 예약 시각 구하기
		Store store = reserveDao.searchStore(storeNo);
		//00:00부터 openingHour까지 30분 단위로 예약 가능한 횟수
		int openingCount = (Integer.parseInt(store.getOpeningHour().substring(0,2)) * 2) + (Integer.parseInt(store.getOpeningHour().substring(3,5)) / 30);
		//00:00부터 closingHour까지 30분 단위로 예약 가능한 횟수
		int closingCount = (Integer.parseInt(store.getClosingHour().substring(0,2)) * 2) + (Integer.parseInt(store.getClosingHour().substring(3,5)) / 30);
		//00:00부터 breakStart, breakEnd 까지 30분 단위로 예약 가능한 횟수(휴게시간 없다면 0으로 되도록 세팅)
		int breakStartCount = 0;
		int breakEndCount = 0;
		if(!store.getBreakStart().equals("-1")) { //사장이 breakStart를 선택 안 하면 기본 value가 문자열 "-1"이 되도록 세팅해놨음. 즉 휴게시간이 없을떄 -1임.
			breakStartCount = (Integer.parseInt(store.getBreakStart().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakStart().substring(3,5)) / 30);
			breakEndCount = (Integer.parseInt(store.getBreakEnd().substring(0,2)) * 2) + (Integer.parseInt(store.getBreakEnd().substring(3,5)) / 30);
		};
		int timeToEat = store.getTimeToEat(); //1=30분
		//allTimes 생성
		List<String> allTimes = new ArrayList<String>();
		//!!!! 어려우니까 일단 당일 열고 당일 닫는 경우만 가정하자 !!!!!
		if(openingCount < closingCount) {//당일 열고 당일 닫는 경우 또는 24시간 운영하는 경우
			if(!store.getBreakStart().equals("-1")) {//휴게시간이 있는 경우
				//open ~ breakStart
				for (int i=0; i<(breakStartCount-openingCount-timeToEat +1); i++) {
					int count = openingCount + i;
					//시각(tt:mm) 만드는 변수
					StringBuilder sb = new StringBuilder();
					//tt 구하기
					int tt = count/2;
					//시각에 tt 추가
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					//조건에 따라 시각에 mm 추가
					if(count%2 == 0){//정각이면
						sb.append("00");
					}else{//30분이면
						sb.append("30");
					};
					allTimes.add(sb.toString());
				};
				//breakEnd ~ close
				for (int i=0; i<(closingCount-breakEndCount-timeToEat +1); i++) {
					int count = breakEndCount + i;
					StringBuilder sb = new StringBuilder();
					int tt = count/2;
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					if(count%2 == 0){
						sb.append("00");
					}else{
						sb.append("30");
					};
					allTimes.add(sb.toString());
				};
			}else {//휴게시간이 없는 경우
				//open ~ close
				for (int i=0; i<(closingCount-openingCount-timeToEat +1); i++) {
					int count = openingCount + i;
					StringBuilder sb = new StringBuilder();
					int tt = count/2;
					if(tt<10) {
						sb.append("0"+tt+":");
					}else {
						sb.append(tt+":");
					}
					if(count%2 == 0){
						sb.append("00");
					}else{
						sb.append("30");
					};
					allTimes.add(sb.toString());
				};
			};
		};
		
		//<2> fullTimes 구하기 (=예약 가능한 날짜의 예약 가능한 시각)
		int tableAmount = reserveDao.tableAmount(storeNo);
		List<String> fullTimes = reserveDao.fullTime(storeNo, selectedDay, tableAmount);
		
		//<3> remainTimes 예약 가능한 날짜의 예약 가능한 시각 구하기
		//allTimes를 깊은복사
		List<String> remainTimes = new ArrayList<String>(allTimes);
		//removeAll=차집합
		remainTimes.removeAll(fullTimes);
		
		
		TimeSet timeSet = new TimeSet(allTimes, fullTimes, remainTimes); 
		
		return timeSet;
	}

	public List<TableNoAndCapacity> tableNoAndCapacity(int storeNo, String reserveDate, String reserveTime) {
		
		List<TableNoAndCapacity> tableNoAndCapacity = reserveDao.tableNoAndCapacity(storeNo, reserveDate, reserveTime);
		return tableNoAndCapacity;
	}
	
	
	
}
