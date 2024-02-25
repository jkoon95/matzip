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
import kr.or.iei.reseve.model.dto.TableNoAndCapacity;
import kr.or.iei.reseve.model.dto.TableNoAndCapacityRowMapper;
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
	@Autowired
	private TableNoAndCapacityRowMapper tableNoAndCapacityRowMapper;
	
	//임시로 storeRowMapper, menuRowMapper
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private MenuRowMapper menuRowMapper;
	
	
	public Store searchStore(int storeNo) {
		String query = "select * from store_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<Store> store = jdbc.query(query, storeRowMapper, params);
		return store.get(0);
	}

	
	public List<Menu> searchMenu(int storeNo) {
		String query = "select * from menu_tbl where store_no = ?";
		Object[] params = {storeNo};
		List<Menu> menus = jdbc.query(query, menuRowMapper, params);
		return menus;
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
		if(fullDays != null) {
			return fullDays;
		}else {
			return null;
		}
	}

	public List<String> fullTime(int storeNo, String selectedDay, int tableAmount) {
		String query = "select reserve_time " + 
						"from (select reserve_time, count(*) count" + 
						"        from reserve_tbl " + 
						"        where store_no = ? and reserve_date = ? and reserve_status != 3 " + 
						"        group by reserve_time " + 
						"        order by reserve_time) r " + 
						"where r.count = ?";
		Object[] params = {storeNo, selectedDay, tableAmount};
		List<String> fullTimes = jdbc.query(query, reserveTimeRowMapper, params);
		if(fullTimes != null) {
			return fullTimes;
		}else {
			return null;
		}
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

	public List<TableNoAndCapacity> tableNoAndCapacity(int storeNo, String reserveDate, String reserveTime) {
		String query = "select table_no, table_capacity from table_tbl where store_no = ? "
					 + "minus "
					 + "select table_no, table_capacity "
					 + "from table_tbl t join reserve_tbl using (table_no) "
					 + "where t.store_no = ? and reserve_date = ? and reserve_time = ? "
					 + "order by table_capacity";
		//"order by table_capacity" 이거 덕분에 식탁 수용가능 인원수가 적은 것 부터 index 0 번에 배치됨
		Object[] params = {storeNo, storeNo, reserveDate, reserveTime};
		List<TableNoAndCapacity> tableNoCapacity = jdbc.query(query, tableNoAndCapacityRowMapper, params);
		return tableNoCapacity;
	}

	//insert reserveStatus = 1
	public int insertReserve(Reserve reserve) {
		String query = "insert into reserve_tbl values (reserve_seq.nextval, ?,?,?,?,?,?,?,?)";
		Object[] params = {reserve.getReserveDate(), reserve.getReserveTime(), reserve.getReservePeople(),
						   reserve.getReserveRequest(), reserve.getReserveStatus(), reserve.getStoreNo(),
						   reserve.getMemberNo(), reserve.getTableNo()};
		int result = jdbc.update(query, params);
		return result;
	}

	public String selectDummy(int reserve_ststus, String reserveDate, String reserveDummyTime2) {
		String query = "select reserve_time from reserve_tbl "
					 + "where reserve_status = ? and reserve_date = ? and reserve_time = ?";
		Object[] params = {reserve_ststus, reserveDate, reserveDummyTime2};
		List<String> deleteTime = jdbc.query(query, reserveTimeRowMapper, params);
		return deleteTime.get(0);
	}


	public int deleteDummy0(String reserveDate, String reserveDummyTime2) {
		String query = "delete from reserve_time "
					 + "where reserve_status = 0 and reserve_date = ? and reserve_time = ?";
		Object[] params = {reserveDate, reserveDummyTime2};
		int deleteResult = jdbc.update(query, params);
		return deleteResult;
	}
	
	
	public int insertDummy(Reserve reserve) {
		String query = "insert into reserve_tbl values (reserve_seq.nextval, ?,?,?,?,?,?,?,?)";
		Object[] params = {reserve.getReserveDate(), reserve.getReserveTime(), reserve.getReservePeople(),
				   		   reserve.getReserveRequest(), reserve.getReserveStatus(), reserve.getStoreNo(),
				   		   reserve.getMemberNo(), reserve.getTableNo()};
		int result = jdbc.update(query, params);
		return result;
	}


	public int insertReserveMenu(int servings, int reserveNo, int menuNo) {
		String query = "insert into reserve_menu_tbl values (?, ?, ?)";
		Object[] params = {servings, reserveNo, menuNo};
		int result = jdbc.update(query, params);
		return result;
		
	}

	
}
