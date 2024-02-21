package kr.or.iei.store.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
				"        COUNT(l.LIKE_NO) AS LIKE_COUNT\r\n" + 
				"    FROM\r\n" + 
				"        STORE_TBL s\r\n" + 
				"        LEFT JOIN STORE_LIKE_TBL l ON s.STORE_NO = l.STORE_NO\r\n" + 
				"    WHERE\r\n" + 
				"        s.SUBWAY_NAME = ? \r\n" + 
				"    GROUP BY\r\n" + 
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
				"        s.TIME_TO_EAT\r\n" + 
				"    ORDER BY\r\n" + 
				"        LIKE_COUNT DESC,\r\n" + 
				"        s.STORE_NO\r\n" + 
				")\r\n" + 
				"WHERE ROWNUM <= ?";
		Object[] params = {stationName , number};
		List list = jdbc.query(query, storeRowMapper,params);
		return list;
	}
}
