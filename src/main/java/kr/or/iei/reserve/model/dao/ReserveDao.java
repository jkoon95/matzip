package kr.or.iei.reserve.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.reseve.model.dto.Reserve;
import kr.or.iei.reseve.model.dto.ReserveDateRowMapper;
import kr.or.iei.reseve.model.dto.ReserveRowMapper;
import kr.or.iei.store.model.dto.Store;

@Repository
public class ReserveDao {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ReserveRowMapper reserveRowMapper;
	@Autowired
	private ReserveDateRowMapper reserveDateRowMapper;

	public int tableAmount(int storeNo) {
		String query = "select count(*) table_amount from table_tbl where store_no = ?";
		Object[] params = {storeNo};
		int tableAmount = jdbc.queryForObject(query, Integer.class);
		return tableAmount;
	}
	
	public List<String> fullDays(int storeNo, int allCount) {
		String query = "select reserve_date "
				+ 		"from (select reserve_date, count(*) count "
				+ 				"from reserve_tbl "
				+ 				"where store_no = ? "
				+ 					"and reserve_date >= to_char(sysdate,'yyyy-mm-dd') "
				+ 					"and reserve_status != 3 group by reserve_date) r "
				+ 		"where r.count = ? "
				+ 		"order by reserve_date";
		Object[] params = {storeNo, allCount};
		List<String> reserveFullDays = jdbc.query(query, reserveDateRowMapper, params);
		
		if(reserveFullDays.isEmpty()) {//실패시
			return null;
		} else {
			return reserveFullDays;
		}
	}
	
	
}
