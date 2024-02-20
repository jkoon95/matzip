package kr.or.iei.board.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.or.iei.board.model.dto.Board;
import kr.or.iei.board.model.dto.BoardCommentRowMapper;
import kr.or.iei.board.model.dto.BoardFile;
import kr.or.iei.board.model.dto.BoardFileRowMapper;
import kr.or.iei.board.model.dto.BoardRowMapper;

@Repository
public class BoardDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private BoardRowMapper boardRowMapper;
	@Autowired
	private BoardFileRowMapper boardFileRowMapper;
	@Autowired
	private BoardCommentRowMapper boardCommentRowMapper;
	
	public List selectBoardList(int start, int end) {
		String query = "SELECT * FROM (SELECT ROWNUM AS RNUM, B.* FROM (SELECT * FROM BOARD_TBL ORDER BY 1 DESC)B) WHERE RNUM BETWEEN ? AND ?";
		Object[] params = {start,end};
		List list=jdbc.query(query,boardRowMapper,params);
		return list;
	}

	public int selectAllBoardCount() {
		String query = "select count(*) from board_tbl";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int insertBoard(Board b) {
		String query = "insert into board_tbl values(board_seq.nextval,?,?,?,0,to_char(sysdate,'yyyy-mm-dd'))";
		Object[] params = {b.getBoardTitle(),b.getBoardWriter(),b.getBoardContent()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int insertBoardFile(BoardFile boardFile) {
		String query = "insert into board_file values(board_file_seq.nextval,?,?,?)";
		Object[] params = {boardFile.getBoardNo(),boardFile.getFilename(),boardFile.getFilepath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectBoardNo() {
		String query = "select max(board_no) from board_tbl";
		int boardNo = jdbc.queryForObject(query, Integer.class);
		return boardNo;

	}

	public Board selectOneBoard(int boardNo) {
		String query = "select * from board_tbl where board_no=?";
		Object[] params = {boardNo};
		List list = jdbc.query(query, boardRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (Board)list.get(0);
	}

	public List selectBoardFile(int boardNo) {
		String query = "select * from board_file where board_no=?";
		Object[] params = {boardNo};
		List list = jdbc.query(query, boardFileRowMapper, params);
		return list;
	}

	public int updateReadCount(int boardNo) {
		String query = "update board_tbl set read_count = read_count+1 where board_no=?";
		Object[] params = {boardNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int deleteBoard(int boardNo) {
		String query = "delete from board_tbl where board_no=?";
		Object[] params = {boardNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int updateBoard(Board b) {
		String query = "update board_tbl set board_title=?, board_content=? where board_no=?";
		Object[] params = {b.getBoardTitle(), b.getBoardContent(), b.getBoardNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public BoardFile selectOneBoardFile(int fileNo) {
		String query = "select * from board_file where file_no=?";
		Object[] params = {fileNo};
		List list = jdbc.query(query, boardFileRowMapper, params);
		return (BoardFile)list.get(0);
	}

	public int deleteBoardFile(int fileNo) {
		String query = "delete from board_file where file_no=?";
		Object[] params = {fileNo};
		int result = jdbc.update(query,params);
		return result;
	}
	
	
}
