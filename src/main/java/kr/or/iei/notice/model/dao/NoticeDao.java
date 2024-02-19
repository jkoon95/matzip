package kr.or.iei.notice.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.notice.model.dto.NoticeFile;
import kr.or.iei.notice.model.dto.NoticeFileRowMapper;
import kr.or.iei.notice.model.dto.NoticeRowMapper;

@Repository
public class NoticeDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private NoticeRowMapper noticeRowMapper;
	@Autowired
	private NoticeFileRowMapper noticeFileRowMapper;
	
	
	public List selectNoticeList(int start, int end) {
		String query = "select * from (select rownum as rnum, n.* from (select * from notice_tbl order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, noticeRowMapper, params);
		return list;
	}

	public int selectAllNoticeCount() {
		String query = "select count(*) from notice_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int insertNotice(Notice n) {
		String query = "insert into notice_tbl values(notice_seq.nextval,?,?,?,0,to_char(sysdate,'yyyy-mm-dd'))";
		Object[] params = {n.getNoticeTitle(),n.getNoticeWriter(),n.getNoticeContent()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertNoticeFile(NoticeFile noticeFile) {
		String query = "insert into notice_file values(notice_file_seq.nextval,?,?,?)";
		Object[] params = {noticeFile.getNoticeNo(),noticeFile.getFilename(),noticeFile.getFilepath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectNoticeNo() {
		String query = "select max(notice_no) from notice_tbl";
		int noticeNo = jdbc.queryForObject(query, Integer.class);
		return noticeNo;
	}
	
}
