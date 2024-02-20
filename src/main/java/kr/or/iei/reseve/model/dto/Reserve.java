package kr.or.iei.reseve.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reserve {

	private int reserveNo; //PK
	private String reserveDate; //ex) YYYY-MM-DD
	private String reserveTime; //ex) 12:00
	private int reservePeople;
	private String reserveRequest;
	private int reserveStatus; //확정 : 1, 취소 : 2, 이후 예약불가 : 3
	private int storeNo; //FK
	private int memberNo; //FK
	private int tableNo; //FK
}
