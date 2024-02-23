package kr.or.iei.reserve.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.reseve.model.dto.Reserve;
import kr.or.iei.reseve.model.dto.ReserveDateRowMapper;
import kr.or.iei.reseve.model.dto.ReserveRowMapper;
import kr.or.iei.reseve.model.dto.ReserveTimeRowMapper;
import kr.or.iei.reseve.model.dto.TempClosedDay;
import kr.or.iei.reseve.model.dto.TempClosedDayRowMapper;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.ClosedDayRowMapper;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.MenuRowMapper;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreRowMapper;

@Repository
public class ReserveDao {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ReserveRowMapper reserveRowMapper;
	@Autowired
	private ReserveDateRowMapper reserveDateRowMapper;
	@Autowired
	private ReserveTimeRowMapper reserveTimeRowMapper;
	@Autowired
	private ClosedDayRowMapper closedDayRowMapper;
	@Autowired
	private TempClosedDayRowMapper tempClosedDayRowMapper;
	
	//임시로 storeRowMapper, menuRowMapper
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private MenuRowMapper menuRowMapper;
	
	//임시
	public Store searchStore(int storeNo) {
		String query = "select * from store_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<Store> store = jdbc.query(query, storeRowMapper, params);
		return store.get(0);
	}

	//임시
	public List<Menu> searchMenu(int storeNo) {
		String query = "select * from menu_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<Menu> menu = jdbc.query(query, menuRowMapper, params);
		return menu;
	}
	
	public int tableAmount(int storeNo) {
		String query = "select count(*) table_amount from table_tbl where store_no = ?";
		Object[] params = {storeNo};
		int tableAmount = jdbc.queryForObject(query, Integer.class, params);
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
		List<String> fullDays = jdbc.query(query, reserveDateRowMapper, params);
		
		return fullDays;
	}

	public List<String> fullTime(int storeNo, String day, int tableAmount) {
		String query = "select reserve_time " + 
						"from (select reserve_time, count(*) count" + 
						"        from reserve_tbl " + 
						"        where store_no = ? and reserve_date = ? and reserve_status != 3 " + 
						"        group by reserve_time " + 
						"        order by reserve_time) r " + 
						"where r.count = ?";
		Object[] params = {storeNo, day, tableAmount};
		List<String> fullTimes = jdbc.query(query, reserveTimeRowMapper, params);
		return fullTimes;
	}

	public List<ClosedDay> closedDays(int storeNo) {
		String query = "select * from closed_day_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<ClosedDay> list = jdbc.query(query, closedDayRowMapper, params);
		return list;
	}

	public List<TempClosedDay> tempClosedDays(int storeNo) {
		String query = "select * from temp_closed_day_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<TempClosedDay> list = jdbc.query(query, tempClosedDayRowMapper, params);
		return list;
	}

	
}
