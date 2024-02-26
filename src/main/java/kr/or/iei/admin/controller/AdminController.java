package kr.or.iei.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import kr.iei.admin.model.dto.AdminListData;
import kr.or.iei.FileUtils;
import kr.or.iei.admin.model.dao.AdminDao;
import kr.or.iei.admin.model.service.AdminService;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreEvidenceData;
import kr.or.iei.store.model.dto.StoreFileData;
import kr.or.iei.store.model.dto.StoreInfoData;
import kr.or.iei.store.model.service.StoreService;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private StoreService storeService;
	@Value("${file.root}")
	private String root;
	@Autowired
	private FileUtils fileUtils;
	
	@GetMapping(value="/allMember")
	public String allMember(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllMember(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/allMember";

	}
	
	@ResponseBody
	@GetMapping(value="/changeLevel")
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
	public String memberView(int memberNo, Model model) {
		Member member = adminService.selectOneMember(memberNo);
		int count=0;
		if(member.getMemberLevel()==2) {
			count = storeService.selectStoreCount(memberNo);
			if(count>0) {
				Store store = adminService.selectStoreNo(memberNo);
				int storeNo=store.getStoreNo();
				model.addAttribute("storeNo",storeNo);			
				System.out.println(store);
				System.out.println(storeNo);				
			}
		}
		model.addAttribute("count",count);
		model.addAttribute("member",member);
		return "admin/memberView";
	}
	
	
	@ResponseBody
	@GetMapping(value="/changeStoreLevel")
	public int changeLevel(Store store, Model model) {
		int result = adminService.changeStoreLevel(store);
		return result;
	}
	
	@ResponseBody
	@PostMapping(value="/updateMember")
	public int updateMember(Member member, Model model) {
		System.out.println(member);
		int result=adminService.memberUpdate(member);
		if(result>0) {
			return result;
		}
		return result;
	}
	
	@GetMapping(value="/deleteMember")
	public String deleteMember(int memberNo, Model model) {
		int result = memberService.deleteMember(memberNo);
		if(result>0) {
			model.addAttribute("title", "탈퇴");
			model.addAttribute("msg", "탈퇴되었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/admin/allMember?reqPage=1");
		}else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "NOPE");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/admin/allMember?reqPage=1");
		}
		return "common/msg";
	}
	
	@GetMapping(value="/allStore")
	public String allStore(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllStore(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/allStore";
	}
	
	@GetMapping(value="/searchStore")
	public String searchStore(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchStore(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/allStore";
	}
	
	@GetMapping(value="/storeView")
	public String storeView(int storeNo, Model model) {
		Store store = adminService.adminSelectOneStore(storeNo);
		Member member = adminService.selectMemberId(store.getMemberNo());
		String addr;
		if(store.getStoreAddr1()!=null) {
			addr = store.getStoreAddr()+" "+store.getStoreAddr1();
		}else {
			addr=store.getStoreAddr();
		}
		model.addAttribute("addr",addr);
		model.addAttribute("member",member);
		model.addAttribute("store",store);
		return "admin/storeView";
	}
	
	@GetMapping(value="/filedown")
	public void filedown(EvidenceFile file, HttpServletResponse response) {
		String savepath = root+"/store/evidence/";
		fileUtils.downloadFile(savepath,file.getFilename(),file.getFilepath(),response);
	}
	
	@GetMapping(value = "/storeStatus")
	public String storeStatus(int storeNo, Model model) {
		int result = adminService.updateStoreStatus(storeNo);
		if(result>0) {
			model.addAttribute("title", "승인완료");
			model.addAttribute("msg", "매장이 승인되었습니다.");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/admin/storeView?storeNo="+storeNo);
		}else {
			model.addAttribute("title", "실패");
			model.addAttribute("msg", "NOPE");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/admin/storeView?storeNo="+storeNo);
		}
		return "common/msg";
	}
	
	@GetMapping(value="/storeBlackList")
	public String storeBlackList(int reqPage,Model model) {
		AdminListData ald = adminService.selectAllBlackStore(reqPage);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		return "admin/storeBlackList";
	}
	
	//searchBlackStore
	@GetMapping(value="/searchBlackStore")
	public String searchBlackStore(int reqPage, String type, String keyword, Model model) {
		AdminListData ald = adminService.searchBlackStore(reqPage,type,keyword);
		model.addAttribute("list",ald.getList());
		model.addAttribute("pageNavi",ald.getPageNavi());
		model.addAttribute("type",type);
		model.addAttribute("keyword",keyword);
		return "admin/storeBlackList";
	}
	//storeBlackChangeLevel
	@ResponseBody
	@GetMapping(value="/storeBlackChangeLevel")
	public int changeLevel(int storeNo) {
		int result = adminService.updatestoreBlackChangeLevel(storeNo);
		return result;
	}
	
}
