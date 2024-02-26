package kr.or.iei.search.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.ClosedDayRowMapper;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.MenuRowMapper;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfo;
import kr.or.iei.store.model.dto.StoreInfoRowMapper;
import kr.or.iei.store.model.dto.StorePlusRowMapper;

@Repository
public class SearchDao {
	@Autowired
	private StorePlusRowMapper storePlusRowMapper;
	@Autowired
	private StoreInfoRowMapper storeInfoRowMapper;
	@Autowired
	private MenuRowMapper menuRowMapper;
	@Autowired
	private ClosedDayRowMapper closedDayRowMapper;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public int searchTotalCount(String stationName) {
		String query = "select count(*) from store_tbl where subway_name=?";
		Object[] params = {stationName};
		int totalCount = jdbc.queryForObject(query, Integer.class,params);
		return totalCount ;
	}
	
	public List selectTopStore(String stationName,int number) {
		String query = "SELECT * FROM (\r\n" + 
				"    SELECT\r\n" + 
				"        s.STORE_NO,\r\n" + 
				"        s.MEMBER_NO,\r\n" + 
				"        s.BUSINESS_NO,\r\n" + 
				"        s.STORE_NAME,\r\n" + 
				"        s.STORE_ADDR, -- 기존 주소\r\n" + 
				"        s.STORE_ADDR1, -- 추가된 주소 필드\r\n" + 
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
				"        COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT, -- 좋아요 수\r\n" + 
				"        COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT, -- 리뷰 수\r\n" + 
				"        AVG(r.REVIEW_STAR) AS REVIEW_SCORE, -- 평균 리뷰 점수\r\n" + 
				"        CASE\r\n" + 
				"            WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                 OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR\r\n" + 
				"                 AND TO_CHAR(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n" + 
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n" + 
				"            ELSE '마감'\r\n" + 
				"        END AS OPERATION_STATUS\r\n" + 
				"    FROM\r\n" + 
				"        STORE_TBL s\r\n" + 
				"        LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"        LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" + 
				"    WHERE\r\n" + 
				"        s.SUBWAY_NAME = ?\r\n" + 
				"    GROUP BY\r\n" + 
				"        s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, s.STORE_ADDR1,\r\n" + 
				"        s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" + 
				"        s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" + 
				"        s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT,\r\n" + 
				"        CASE\r\n" + 
				"            WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                 OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR\r\n" + 
				"                 AND TO_CHAR(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n" + 
				"            WHEN TO_CHAR(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n" + 
				"            ELSE '마감'\r\n" + 
				"        END\r\n" + 
				"    ORDER BY\r\n" + 
				"        LIKE_COUNT DESC\r\n" + 
				") WHERE ROWNUM <= ?";
		Object[] params = {stationName , number};
		List list = jdbc.query(query, storePlusRowMapper,params);
		return list;
	}

	public List selectSearchList(int start, int end,String stationName) {
		String query = "SELECT * \r\n" + 
				"FROM (\r\n" + 
				"    SELECT rownum AS rnum, sub.* \r\n" + 
				"    FROM (\r\n" + 
				"        SELECT\r\n" + 
				"            s.STORE_NO,\r\n" + 
				"            s.MEMBER_NO,\r\n" + 
				"            s.BUSINESS_NO,\r\n" + 
				"            s.STORE_NAME,\r\n" + 
				"            s.STORE_ADDR,\r\n" + 
				"            s.STORE_PHONE,\r\n" + 
				"            s.HOMEPAGE,\r\n" + 
				"            s.STORE_SNS,\r\n" + 
				"            s.STORE_DESCRIPTION,\r\n" + 
				"            s.FOOD_TYPE,\r\n" + 
				"            s.STORE_IMG,\r\n" + 
				"            s.OPENING_HOUR,\r\n" + 
				"            s.CLOSING_HOUR,\r\n" + 
				"            s.BREAK_START,\r\n" + 
				"            s.BREAK_END,\r\n" + 
				"            s.STORE_LEVEL,\r\n" + 
				"            s.SUBWAY_NAME,\r\n" + 
				"            s.STORE_STATUS,\r\n" + 
				"            s.TIME_TO_EAT,\r\n" + 
				"            \r\n" + 
				"            COUNT(DISTINCT l.LIKE_NO) AS LIKE_COUNT,\r\n" + 
				"            COUNT(DISTINCT r.REVIEW_NO) AS REVIEW_COUNT,\r\n" + 
				"            AVG(r.REVIEW_STAR) AS REVIEW_SCORE,\r\n" + 
				"            CASE\r\n" + 
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" + 
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n" + 
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n" + 
				"                ELSE '마감'\r\n" + 
				"            END AS OPERATION_STATUS,s.STORE_ADDR1\r\n" + 
				"        FROM\r\n" + 
				"            STORE_TBL s\r\n" + 
				"            LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"            LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" + 
				"        WHERE\r\n" + 
				"            s.SUBWAY_NAME = ?\r\n" + 
				"        GROUP BY\r\n" + 
				"            s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, \r\n" + 
				"            s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" + 
				"            s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" + 
				"            s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT,\r\n" + 
				"            CASE\r\n" + 
				"                WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO)\r\n" + 
				"                    OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) THEN '휴무'\r\n" + 
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR \r\n" + 
				"                    AND to_char(SYSDATE,'hh24:mi') NOT BETWEEN s.BREAK_START AND s.BREAK_END THEN '영업중'\r\n" + 
				"                WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.BREAK_START AND s.BREAK_END THEN 'break time'\r\n" + 
				"                ELSE '마감'\r\n" + 
				"            END, s.STORE_ADDR1\r\n" + 
				"    ) sub\r\n" + 
				") \r\n" + 
				"WHERE rnum BETWEEN ? AND ?";
				
		Object[] params = {stationName,start,end};
		List list = jdbc.query(query, storePlusRowMapper,params);
		return list;
	}

	public Store selectSearchOne(int storeNo) {
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
				//"        s.STORE_ADDR1,\r\n" + 
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
				"       AS OPERATION_STATUS,s.STORE_ADDR1\r\n" + 
				"    FROM\r\n" + 
				"        STORE_TBL s\r\n" + 
				"        LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"        LEFT JOIN REVIEW_TBL r ON s.STORE_NO = r.STORE_NO\r\n" + 
				"    GROUP BY\r\n" + 
				"s.STORE_NO, s.MEMBER_NO, s.BUSINESS_NO, s.STORE_NAME, s.STORE_ADDR, \r\n" + 
				"s.STORE_PHONE, s.HOMEPAGE, s.STORE_SNS, s.STORE_DESCRIPTION, s.FOOD_TYPE, \r\n" + 
				"s.STORE_IMG, s.OPENING_HOUR, s.CLOSING_HOUR, s.BREAK_START, s.BREAK_END, \r\n" + 
				"s.STORE_LEVEL, s.SUBWAY_NAME, s.STORE_STATUS, s.TIME_TO_EAT,\r\n" + 
				"CASE WHEN TO_CHAR(SYSDATE, 'DY') IN (SELECT CLOSED_DAY FROM CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) \r\n" + 
				"OR TO_CHAR(SYSDATE, 'YYYY-MM-DD') IN (SELECT TEMP_CLOSED_DAY FROM TEMP_CLOSED_DAY_TBL WHERE STORE_NO = s.STORE_NO) \r\n" + 
				"THEN '휴무' WHEN to_char(SYSDATE,'hh24:mi') BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR and to_char(SYSDATE,'hh24:mi') \r\n" + 
				"BETWEEN s.break_start AND s.break_end THEN 'break time' WHEN to_char(SYSDATE,'hh24:mi') \r\n" + 
				"BETWEEN s.OPENING_HOUR AND s.CLOSING_HOUR THEN '영업중' ELSE '마감' END , s.store_addr1\r\n" + 
				"\r\n" + 
				") \r\n" + 
				"WHERE store_no=?";
		Object[] params = {storeNo};
		List list = jdbc.query(query, storePlusRowMapper,params);
		
		return (Store)list.get(0);
	}

	public StoreInfo getStoreInfoByStoreNo(int storeNo) {
		String query = "select * from info_tbl where store_no=?";
		Object[] params = {storeNo};
		List list = jdbc.query(query, storeInfoRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (StoreInfo)list.get(0);
	}

	public List<Menu> selectAllMenu(int storeNo) {
		String query = "select * from menu_tbl where store_no=?";
		Object[] params = {storeNo};
		List<Menu> menuList = jdbc.query(query, menuRowMapper, params);
		return menuList;
	}

	public List<ClosedDay> selectClosedDay(int storeNo) {
		String query = "select * from closed_day_tbl where store_no=?";
		Object[] params = {storeNo};
		List<ClosedDay> closedDayList = jdbc.query(query, closedDayRowMapper, params);
		return closedDayList;
	}

	public int updateInfo(StoreInfo i) {
		String query = "update info_tbl set info_content=? where store_no=?";
		Object[] params = {i.getInfoContent(), i.getStoreNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertInfo(StoreInfo i) {
		String query = "insert into info_tbl values(info_seq.nextval,null,?,?)";
		Object[] params = {i.getInfoContent(), i.getStoreNo()};
		int result = jdbc.update(query,params);
		return result;
	}


}
