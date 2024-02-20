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

	public List selectTop5Store(String stationName) {
		String query = "select * from store_tbl where subway_name = ?";
		Object[] params = {stationName};
		List list = jdbc.query(query, storeRowMapper,params);
		return list;
	}
}
