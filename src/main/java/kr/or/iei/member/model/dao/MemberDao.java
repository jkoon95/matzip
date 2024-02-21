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
	
	public Member selectOneMember(String memberId, String memberPw, int memberLevel) {
		String query = "select * from member_tbl where member_id=? and member_pw=? and member_level=?";
		Object[] params = {memberId,memberPw,memberLevel};
		List list = jdbc.query(query, memberRowMapper,params);		
		if(list.isEmpty()) {
			return null;
		}
		return (Member)list.get(0);
	}
	public int insertMember(Member m) {
		String query = "insert into member_tbl values(member_seq.nextval,?,?,?,?,?,?,to_char(sysdate,'yyyy-mm-dd'),?)";
		Object[] params = {m.getMemberId(),m.getMemberEmail(),m.getMemberPw(),m.getMemberName(),m.getMemberNickname(),m.getMemberPhone(),m.getMemberLevel()};
		int result = jdbc.update(query,params);
		return result;
	}
	
}
