package kr.or.iei.store.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreRowMapper;
import kr.or.iei.subway.model.dto.subwayRowMapper;

@Repository
public class StoreDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private subwayRowMapper subwayRowMapper;
	
	public List selectAllSubway() {
		String query = "select * from subway_tbl order by 1";
		List list = jdbc.query(query, subwayRowMapper);
		return list;
	}

	public List selectTopStore(String stationName,int number) {
		String query = "SELECT * FROM (\r\n" + 
				"    SELECT\r\n" + 
				"        s.STORE_NO,\r\n" + 
				"        s.MEMBER_NO,\r\n" + 
				"        s.BUSINESS_NO,\r\n" + 
				"        s.STORE_NAME,\r\n" + 
				"        s.STORE_ADDR,\r\n" + 
				"        s.STORE_PHONE,\r\n" + 
				"        s.HOMEPAGE,\r\n" + 
				"        s.STORE_SNS,\r\n" + 
				"        s.STORE_DESCRIPTION,\r\n" + 
				"        s.FOOD_TYPE,\r\n" + 
				"        s.STORE_IMG,\r\n" + 
				"        s.OPENING_HOUR,\r\n" + 
				"        s.CLOSING_HOUR,\r\n" + 
				"        s.BREAK_START,\r\n" + 
				"        s.BREAK_END,\r\n" + 
				"        s.STORE_LEVEL,\r\n" + 
				"        s.SUBWAY_NAME,\r\n" + 
				"        s.STORE_STATUS,\r\n" + 
				"        s.TIME_TO_EAT,\r\n" + 
				"        COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" + 
				"        COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" + 
				"        AVG(r.REVIEW_STAR) AS REVIEW_SCORE,\r\n" + 
				"        CASE\r\n" + 
				"           WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"                \r\n" + 
				"           WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" + 
				"           and  to_char(SYSDATE,'hh24:mi') BETWEEN s.break_start AND s.break_end THEN 'break time'     \r\n" + 
				"                \r\n" + 
				"           WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR THEN '영업중'\r\n" + 
				"                \r\n" + 
				"           ELSE '마감'\r\n" + 
				"       END \r\n" + 
				"       AS OPERATION_STATUS\r\n" + 
				"    FROM\r\n" + 
				"        STORE_TBL s\r\n" + 
				"        LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"        LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" + 
				"    WHERE\r\n" + 
				"        s.SUBWAY_NAME = ? \r\n" + 
				"    GROUP BY\r\n" + 
				"s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, \r\n" + 
				"s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" + 
				"s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" + 
				"s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT, \r\n" + 
				"CASE WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) \r\n" + 
				"OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) \r\n" + 
				"THEN '휴무' WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR and to_char(SYSDATE,'hh24:mi') \r\n" + 
				"BETWEEN s.break_start AND s.break_end THEN 'break time' WHEN to_char(SYSDATE,'hh24:mi') \r\n" + 
				"BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR THEN '영업중' ELSE '마감' END\r\n" + 
				"    ORDER BY\r\n" + 
				"        LIKE_COUNT DESC,\r\n" + 
				"        REVIEW_COUNT DESC,\r\n" + 
				"        s.STORE_NO\r\n" + 
				") WHERE ROWNUM <= ?";
		Object[] params = {stationName , number};
		List list = jdbc.query(query, storeRowMapper,params);
		return list;
	}

	public int insertStore(Store store) {
		String query = "INSERT INTO STORE_TBL VALUES (STORE_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,?,2,?)";
		Object[] params = {store.getMemberNo(),store.getBusinessNo(),store.getStoreName(),store.getStoreAddr(),store.getStorePhone(),store.getHomePage(),store.getStoreSns(),store.getStoreDescription(),store.getFoodType(),store.getStoreImg(),store.getOpeningHour(),store.getClosingHour(),store.getBreakStart(),store.getBreakEnd(),store.getSubwayName(),store.getTimeToEat()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectStoreNo() {
		String query="select max(store_no) from STORE_TBL";
		int storeNo = jdbc.queryForObject(query, Integer.class);
		return storeNo;
	}

	public int insertEvidenceFile(EvidenceFile evidenceFile) {
		String query = "insert into EVIDENCE_FILE values(EVIDENCE_FILE_SEQ.nextval,?,?,?)";
		Object[] params = {evidenceFile.getStoreNo(),evidenceFile.getFilename(),evidenceFile.getFilepath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertClosedDay(int storeNo, String closedDay) {
		String query = "insert into closed_day_tbl values(?,?)";
		Object[] params = {storeNo, closedDay};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertMenu(Menu menu) {
		String query = "INSERT INTO MENU_TBL VALUES (MENU_SEQ.NEXTVAL,?,?,?,?)";
		Object[] params= {menu.getStoreNo(),menu.getMenuName(),menu.getMenuPrice(),menu.getMenuImg()};
		int result = jdbc.update(query,params);
		return result;
	}
}
