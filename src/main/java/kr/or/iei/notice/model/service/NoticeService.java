package kr.or.iei.notice.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.iei.notice.model.dao.NoticeDao;
import kr.or.iei.notice.model.dto.NoticeListData;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	public NoticeListData selectNoticeList(int reqPage) {
		int numPerPage = 10;
		int end = reqPage*numPerPage;
		int start = end - numPerPage + 1;
		List list = noticeDao.selectNoticeList(start, end);
		
		int totalCount = noticeDao.selectAllNoticeCount();
		
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		} else {
			totalPage = totalCount/numPerPage + 1;
		}
		
		int pageNaviSize = 5;
		int pageNo = ((reqPage - 1)/pageNaviSize)*pageNaviSize + 1;
		
		String pageNavi = "<ul class='pagination'>";
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/notice/noticeList?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>navigate_before</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i=0;i<pageNaviSize;i++) {
			if(pageNo == reqPage) {
				pageNavi += "<li>";
				pageNavi += "<a href='/ntice/noticeList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			} else {
				pageNavi += "<li>";
				pageNavi += "<a href='/notice/noticeList?reqPage="+(pageNo)+"'>";
				pageNavi += pageNo;
				pageNavi += "</a></li>";
			}
			pageNo++;
			if(pageNo > totalPage) {
				break;
			}
		}
		
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a href='/notice/noticeList?reqPage="+(pageNo)+"'>";
			pageNavi += "<span class='material-icons'>navigate_next</span>";
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";
		
		NoticeListData nld = new NoticeListData(list, pageNavi);
		return nld;
	}
}
