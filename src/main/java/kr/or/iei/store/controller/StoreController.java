package kr.or.iei.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import kr.or.iei.FileUtils;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfoData;
import kr.or.iei.store.model.service.StoreService;

@Controller
@RequestMapping(value = "/store")
public class StoreController {
		@Autowired
		private StoreService storeService;
		@Value("${file.root}")
		private String root;
		@Autowired
		private FileUtils fileUtils;
		
		@GetMapping(value="/bussinessNumberCheck")
		public String bussinessNumberCheck(@SessionAttribute(required = false) Member member, Model model) {
			int memberLevel=member.getMemberLevel();
			if(memberLevel==2) {
				return "store/bussinessNumberCheck";								
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장회원으로 등록하세요.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");
				return "common/msg";
			}
		}
		
		@GetMapping(value="/storeEnrollFrm")
		public String storeEnrollFrm(int memberNo, String businessNo, Model model) {
			int count = storeService.selectStoreCount(memberNo);
			if(count==0) {
				List subwaylist = storeService.selectAllSubway();
				model.addAttribute("businessNo", businessNo);
				model.addAttribute("subway",subwaylist);
				return "store/storeEnrollFrm";
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","등록된 보유하신 매장이 있습니다.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");
				return "common/msg";
			}
		}
		
		@PostMapping(value="/storeEnroll")
		public String storeEnroll(Store store, MultipartFile[] edvienceUpFile, String address,String detailAddress, String[] closedDays, MultipartFile storeImgFile, MultipartFile[] menuImgFile, String[] name, int[] price, Model model) {
			//매장
			store.setStoreAddr(address+" "+detailAddress);	//도로명 + 상세주소
			String storeSavepath = root+"/store/";
			String storeFilepath = fileUtils.upload(storeSavepath, storeImgFile);
			store.setStoreImg(storeFilepath);
			//사업자증빙자료
			List<EvidenceFile> evidenceFileList = new ArrayList<EvidenceFile>();
			if(!edvienceUpFile[0].isEmpty()) { 
				String evidenceSavepath = root+"/store/evidence/";	
				for(MultipartFile file : edvienceUpFile) {
					String evidenceFilename = file.getOriginalFilename();
					String evidenceFilepath = fileUtils.upload(evidenceSavepath, file);
					EvidenceFile evidenceFile = new EvidenceFile();
					evidenceFile.setFilename(evidenceFilename);
					evidenceFile.setFilepath(evidenceFilepath);
					evidenceFileList.add(evidenceFile);
				}
			}
			//휴무일 
			//메뉴 등록값
			List<Menu> menuList = new ArrayList<Menu>();
			String menuSavepath = root+"/store/menu/";		
			for(int i=0; i<name.length; i++) {
				Menu menu = new Menu();
				String menuFilepath = fileUtils.upload(menuSavepath, menuImgFile[i]);				
				String menuName = name[i];
				int menuPrice = price[i];
				menu.setMenuImg(menuFilepath);
				menu.setMenuName(menuName);
				menu.setMenuPrice(menuPrice);
				menuList.add(menu);
			}
			int result = storeService.insertStore(store,evidenceFileList,closedDays,menuList);
			int count;//성공갯수
			if (closedDays != null) {
				count = 1+evidenceFileList.size()+closedDays.length+menuList.size();
		    } else {
		    	count = 1+evidenceFileList.size()+menuList.size();
		    }
			if(result==count) {
				model.addAttribute("title","성공");
				model.addAttribute("msg","매장등록에 성공했습니다.");
				model.addAttribute("icon","success");
			}else {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장등록에 실패했습니다.");
				model.addAttribute("icon","error");
			}
				model.addAttribute("loc","/");
			return "common/msg";
		}
		
		@GetMapping(value="/myStore")
		public String mypage(HttpSession session, Model model) {
			//매장없을시 등록페이지로이동
			Member member = (Member)session.getAttribute("member");
			int memberNo = member.getMemberNo();
			int count = storeService.selectStoreCount(memberNo);
			if(count==0) {
				model.addAttribute("title","실패");
				model.addAttribute("msg","매장을 먼저 등록해주세요.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/store/bussinessNumberCheck");
				return "common/msg";
			}else {
				//스토어,휴무일,메뉴
				StoreInfoData sid = storeService.selectOneStore(memberNo);
				model.addAttribute("store",sid.getStore());
				model.addAttribute("closedDayList",sid.getClosedDayList());
				model.addAttribute("menuList",sid.getMenuList());
				return "store/myStore";
			}
		}
		
		@GetMapping(value="/storeUpdateFrm")
		public String storeUpdateFrm(int storeNo, Model model) {
			Store store = storeService.selectGetStore(storeNo);
			if(store==null) {
				model.addAttribute("title","수정하기 실패");
				model.addAttribute("msg","매장이 존재하지 않습니다.");
				model.addAttribute("icon","error");
				model.addAttribute("loc","/");
				return "common/msg";
			}else {
				List subwaylist = storeService.selectAllSubway();
				List closedDayList = storeService.selectClosedDay(storeNo);
				model.addAttribute("subway",subwaylist);
				model.addAttribute("store",store);
				model.addAttribute("closedDayList",closedDayList);
				return "store/storeUpdateFrm";				
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
}
