package kr.or.iei.reserve.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.reserve.model.dao.ReserveDao;
import kr.or.iei.store.model.dto.Store;

@Service
public class ReserveService {

	@Autowired
	private ReserveDao reserveDao;

	public List reserveFrm(Member member, Store store) {
		List list = new ArrayList();
		
		int tableAmount = reserveDao.tableAmount(store.getStoreNo());
		
		//한 날짜 기준 영업시간 내에서 30분 단위로 예약 가능한 총 갯수
		int totalCount = 0;
		
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
		//timeToEatCount 1당 30분임.
		int timeToEatCount = store.getTimeToEat();

		//아래 if문 조건식 돌리기 위해 선언
		int openingHour = Integer.parseInt(store.getOpeningHour().substring(0,2)+store.getOpeningHour().substring(3,5));
		int closingHour = Integer.parseInt(store.getClosingHour().substring(0,2)+store.getClosingHour().substring(3,5));
		int breakStart = Integer.parseInt(store.getBreakStart().substring(0,2)+store.getBreakStart().substring(3,5));
		int breakEnd = Integer.parseInt(store.getBreakEnd().substring(0,2)+store.getBreakEnd().substring(3,5));
		
		//경우의 수에 따른 totalCount 찾기
		if(openingHour < closingHour) {//당일 열고 당일 닫는 경우 또는 24시간 운영하는 경우
			/*
			 * if(store.getBreakStart() != "-1") {//휴게시간이 있는 경우
					totalCount = (closingCount - breakEndCount) + (breakStartCount - openingCount);
				}else {//휴게시간이 없는 경우
					totalCount = closingCount - openingCount;
				}
			*/
			//계산해보니 휴게시간이 없으면 breakCount == 0 이어서, 더하나 빼나 상관 없네? 휴게시간 있는 경우와 없는 경우 모두 하나의 수식으로 합치자 ↓
			totalCount = closingCount - breakEndCount + breakStartCount - openingCount;
		}else {//당일 열고 익일 닫는 경우
			if(store.getBreakStart() != "-1") {//휴게시간이 있는 경우
				if(breakStart < breakEnd) { //휴게시간 시작과 종료가 모두 같은 날인 경우
					if(breakStart < openingHour < ) { //휴게시간 시작과 종료가 opening날짜와 같은 경우(당일인 경우)
						/*
						24시 : 48
						breakEndCount : 40
						breakStartCount : 30
						openingCount : 20
						closingCount : 10
						0시 : 0
						*/
					}else {//휴게시간 시작과 종료가 closing날짜와 같은 경우(익일인 경우)
						/*
						24시 : 48
						openingCount : 40
						closingCount : 30
						breakEndCount : 20
						breakStartCount : 10
						0시 : 0
						*/
					}
				}else { //휴게시간 시작은 당일이고 종료는 익일인 경우(breakStart > breakEnd)
					/*
					24시 : 48
					breakStartCount : 40
					openingCount : 30
					closingCount : 20
					breakEndCount : 10
					0시 : 0
					*/
				}
				
			}else {//휴게시간이 없는 경우
				totalCount = 48 - openingCount + closingCount;
			}
		}
		
		
		List<String> fullDays = reserveDao.fullDays(store, tableAmount);
		
		return list;
	}
	
	
}
