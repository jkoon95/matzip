package kr.iei.admin.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ReportRowMapper implements RowMapper<Report>{

	@Override
	@Nullable
	public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
		Report r = new Report();
		r.setReport_no(rs.getInt("report_no"));
		r.setMember_no(rs.getInt("member_no"));
		r.setReport_reason(rs.getString("report_reason"));
		r.setReport_target(rs.getString("report_target"));
		r.setReport_type(rs.getInt("report_type"));
		r.setMember_id(rs.getString("member_id"));
		r.setStore_no(rs.getInt("store_no"));
		r.setStore_name(rs.getString("store_name"));
		return r;
	}


	
}
