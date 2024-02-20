package kr.or.iei.reserve.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.reseve.model.dto.ReserveRowMapper;

@Repository
public class ReserveDao {

	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private ReserveRowMapper reserveRowMapper;
	
	
}
