package kr.or.iei.member.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.dto.MemberRowMapper;

@Repository
public class MemberDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private MemberRowMapper memberRowMapper;
	public Member selectOneMember(String memberId, String memberPw) {
		String query = "select * from member_tbl where member_id=? and member_pw=?";
		Object[] params = {memberId,memberPw};
		List list = jdbc.query(query, memberRowMapper,params);
		if(list.isEmpty()) {
			return null;
		}
		return (Member)list.get(0);
	}
	
}
