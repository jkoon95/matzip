package kr.or.iei.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.iei.admin.model.dto.AdminListData;
import kr.or.iei.admin.model.service.AdminService;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.store.model.dto.Store;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/allMember")
	public String allMember(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllMember(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/allMember";

	}
	
	@ResponseBody
	@GetMapping("/changeLevel")
	public int changeLevel(Member m, Model model) {
		int result = adminService.changeLevel(m);
		return result;
	}
	
	@GetMapping(value="/search")
	public String search(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchMember(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/allMember";
	}
	
	@GetMapping(value="/memberView")
	public String search(int memberNo, Model model) {
		Member member = adminService.selectOneMember(memberNo);
		model.addAttribute("member",member);
		return "admin/memberView";
	}
	
	
	@ResponseBody
	@GetMapping("/changeStoreLevel")
	public int changeLevel(Store store, Model model) {
		int result = adminService.changeStoreLevel(store);
		return result;
	}
	
}
