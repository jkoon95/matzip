package kr.or.iei.admin.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.iei.admin.model.dto.AdminListData;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.dto.MemberRowMapper;
import kr.or.iei.member.model.dto.OriginMemberRowMapper;
import kr.or.iei.notice.model.dto.NoticeListData;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreRowMapper;

@Repository
public class AdminDao {
	@Autowired 
	private JdbcTemplate jdbc;
	@Autowired
	private OriginMemberRowMapper originMemberRowMapper;
	@Autowired
	private StoreRowMapper storeRowMapper;
	
	public List selectAllMember() {
		String query = "select * from member_tbl order by 1 desc";
		List list = jdbc.query(query, originMemberRowMapper);
		return list;
	}

	public int changeLevel(Member m) {
		String query = "update member_tbl set member_level=? where member_no=?";
		Object[] params = {m.getMemberLevel(),m.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int changeStoreLevel(Store store) {
		String query = "update store_tbl set store_level=? where member_no=?";
		Object[] params = {store.getStoreLevel(),store.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectMemberList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * FROM member_tbl ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,originMemberRowMapper ,params);
		return list;
	}

	public int selectAllMemberCount() {
		String query="select count(*) from member_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}
	
	public List selectSearchAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_id || member_email || member_name like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public List selectSearchId(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_id like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public List selectSearchEmail(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_email like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public List selectSearchName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (select * from member_tbl where member_name like '%'||?||'%' ORDER BY 1 DESC)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, originMemberRowMapper,params);
		return list;
	}

	public int allTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_id || member_email || member_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int idTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_id like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int emailTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_email like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int nameTotalCount(String keyword) {
		String query = "SELECT count(*) from member_tbl where member_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public Member selectOneMember(int memberNo) {
		String query = "select * from member_tbl where member_no=?";
		Object[] params = {memberNo};
		List list = jdbc.query(query, originMemberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Member)list.get(0);
	}

	public int memberUpdate(Member member) {
		String query="update member_tbl set member_pw=?,member_phone=?,member_name=? where member_no=?";
		Object[] params = {member.getMemberPw(),member.getMemberPhone(),member.getMemberName(),member.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectStoreList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT * FROM store_tbl ORDER BY 1 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,storeRowMapper ,params);
		return list;
	}

	public int selectAllStoreCount() {
		String query="select count(*) from member_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}


	
}
