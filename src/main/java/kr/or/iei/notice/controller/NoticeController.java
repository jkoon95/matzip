package kr.or.iei.notice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import kr.or.iei.FileUtils;
import kr.or.iei.notice.model.dto.Notice;
import kr.or.iei.notice.model.dto.NoticeFile;
import kr.or.iei.notice.model.dto.NoticeListData;
import kr.or.iei.notice.model.service.NoticeService;

@Controller
@RequestMapping(value="/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	@Value("${file.root}")
	private String root;
	@Autowired
	private FileUtils fileUtils;
	
	@GetMapping(value="noticeList")
	public String noticeList(int reqPage, Model model) {
		NoticeListData nld = noticeService.selectNoticeList(reqPage);
		model.addAttribute("noticeList",nld.getList());
		model.addAttribute("pageNavi",nld.getPageNavi());
		return "notice/noticeList";
	}
	
	@GetMapping(value="/noticeWriteFrm")
	public String noticeWriteFrm() {
		return "notice/noticeWriteFrm";
	}
	
	@PostMapping(value="/write")
	public String write(Notice n, MultipartFile[] upfile, Model model) {
		List<NoticeFile> fileList = new ArrayList<NoticeFile>();
		if(!upfile[0].isEmpty()) {
			String savepath = root+"/notice/";
			for(MultipartFile file : upfile) {
				String filename = file.getOriginalFilename();
				String filepath = fileUtils.upload(savepath, file);
				NoticeFile noticeFile = new NoticeFile();
				noticeFile.setFilename(filename);
				noticeFile.setFilepath(filepath);
				fileList.add(noticeFile);
			}
		}
		int result = noticeService.insertNotice(n,fileList);
		if(result == (fileList.size()+1)) {
			model.addAttribute("title", "성공");
			model.addAttribute("msg", "공지사항 작성에 성공했습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "작성에 실패했습니다.");
			model.addAttribute("icon", "error");
		}
		model.addAttribute("loc", "/notice/noticeList?reqPage=1");
		return "common/msg";
	}
	
	
}
