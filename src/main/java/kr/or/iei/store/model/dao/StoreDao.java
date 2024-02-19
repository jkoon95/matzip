package kr.or.iei.store.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.store.model.dto.StoreRowMapper;

@Repository
public class StoreDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private StoreRowMapper storeRowMapper;
}
