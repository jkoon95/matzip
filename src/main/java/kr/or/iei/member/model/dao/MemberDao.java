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
		String query = "select \r\n" + 
				"    member_no,member_id,substr(member_email,1,instr(member_email,'@',1,1)-1) member_email,substr(member_email,instr(member_email,'@',1,1)) email_Address,member_pw, member_name, member_nickname, member_phone, member_join_date,member_level\r\n" + 
				"from member_tbl where member_id=? and member_pw=? and member_level=?";
		Object[] params = {memberId,memberPw,memberLevel};
		List list = jdbc.query(query, memberRowMapper,params);		
		if(list.isEmpty()) {
			return null;
		}
		return (Member)list.get(0);
	}
	public int insertMember(Member m) {
		String query = "insert into member_tbl values(member_seq.nextval,?,?,?,?,?,?,to_char(sysdate,'yyyy-mm-dd'),?)";
		Object[] params = {m.getMemberId(),m.getMemberEmail()+m.getEmailAddress(),m.getMemberPw(),m.getMemberName(),m.getMemberNickname(),m.getMemberPhone(),m.getMemberLevel()};
		int result = jdbc.update(query,params);
		return result;
	}
	public int updateMember(Member m) {
		String query = "update member_tbl set member_email=?,member_pw=?,member_name=?,member_phone=? where member_no=?";
		Object[] params = {m.getMemberEmail(),m.getMemberPw(),m.getMemberName(),m.getMemberPhone(),m.getMemberNo()};
		int result = jdbc.update(query, params);
		return result;
	}
	public int storeUpdate(Member m) {
		String query = "update member_tbl set member_email=?,member_pw=?,member_name=?,member_phone=? where member_no=?";
		Object[] params = {m.getMemberEmail(),m.getMemberPw(),m.getMemberName(),m.getMemberPhone(),m.getMemberNo()};
		int result = jdbc.update(query, params);
		return result;
	}
	
}
