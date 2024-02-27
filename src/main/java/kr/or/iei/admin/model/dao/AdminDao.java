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
import kr.or.iei.store.model.dto.StoreMemberRowMapper;
import kr.or.iei.store.model.dto.StoreRowMapper;
import kr.or.iei.subway.model.dto.subwayRowMapper;

@Repository
public class AdminDao {
	@Autowired 
	private JdbcTemplate jdbc;
	@Autowired
	private OriginMemberRowMapper originMemberRowMapper;
	@Autowired
	private StoreRowMapper storeRowMapper;
	@Autowired
	private StoreMemberRowMapper storeMemberRowMapper;
	
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
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT * FROM store_tbl order by store_status desc,store_no desc)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,storeRowMapper ,params);
		return list;
	}

	public int selectAllStoreCount() {
		String query="select count(*) from store_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public List selectSearchStoreAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (select * from store_tbl where store_name || subway_name like '%'||?||'%' ORDER BY 1 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeRowMapper ,params);
		return list;
	}

	public List selectSearchStoreSubway(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (select * from store_tbl where subway_name like '%'||?||'%' ORDER BY 1 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeRowMapper, params);
		return list;
	}

	public List selectSearchStoreName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (select * from store_tbl where store_name like '%'||?||'%' ORDER BY 1 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeRowMapper, params);
		return list;
	}

	public int allStoreTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl where store_name || subway_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int subwayTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl where subway_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int storeNameTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl where store_name like '%'||?||'%'";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int updateStoreStatus(int storeNo) {
		String query="update store_tbl set STORE_status=1 where store_no=?";
		Object[] params= {storeNo};
		int result=jdbc.update(query,params);
		return result;
	}

	public Store selectStoreNo(int memberNo) {
		String query="select * from store_tbl where member_no=?";
		Object[] params= {memberNo};
		List list = jdbc.query(query, storeRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Store)list.get(0);
	}

	public List selectBlackStoreList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 order by 3)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,storeMemberRowMapper ,params);
		return list;
	}

	public int selectBlackAllStoreCount() {
		String query="select count(*) from store_tbl where store_level=2";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public List selectSearchBlackStoreAll(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name || member_id like '%'||?||'%') ORDER BY 3 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeMemberRowMapper ,params);
		return list;
	}

	public List selectSearchBlackStoreid(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 and (member_id like '%'||?||'%') ORDER BY 3 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeMemberRowMapper ,params);
		return list;
	}

	public List selectSearchBlackStoreName(int start, int end, String keyword) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, s.* FROM (SELECT member_no,member_id,store_no,store_name,store_phone,store_level from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name like '%'||?||'%') ORDER BY 3 DESC)s) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {keyword,start,end};
		List list = jdbc.query(query, storeMemberRowMapper ,params);
		return list;
	}

	public int allBlackStoreTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name || member_id like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int blackStoreIdTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl join member_tbl using(member_no) where store_level=2 and (member_id like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int blackStoreNameTotalCount(String keyword) {
		String query = "SELECT count(*) from store_tbl join member_tbl using(member_no) where store_level=2 and (store_name like '%'||?||'%')";
		Object[] params = {keyword};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int updatestoreBlackChangeLevel(int storeNo) {
		String query="update store_tbl set STORE_level=1 where store_no=?";
		Object[] params= {storeNo};
		int result=jdbc.update(query,params);
		return result;
	}

	public List selectMemberStoreList(int start, int end) {
		String query ="SELECT * FROM (SELECT ROWNUM AS RNUM, m.* FROM (SELECT * from member_tbl where member_level in(4,5,6) order by 1)m) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list = jdbc.query(query,originMemberRowMapper ,params);
		return list;
	}

	public int selectBlackAllMemberCount() {
		String query="select count(*) from member_tbl where member_level in(4,5,6)";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}


	
}
