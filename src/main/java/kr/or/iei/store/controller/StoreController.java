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
import org.springframework.web.multipart.MultipartFile;

import kr.or.iei.FileUtils;
import kr.or.iei.board.model.dto.BoardFile;
import kr.or.iei.store.model.dto.EvidenceFile;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
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
		public String bussinessNumberCheck() {
			return "store/bussinessNumberCheck";
		}
		
		@GetMapping(value="/storeEnrollFrm")
		public String storeEnrollFrm(String businessNo, Model model) {
			List subwaylist = storeService.selectAllSubway();
			model.addAttribute("businessNo", businessNo);
			model.addAttribute("subway",subwaylist);
			return "store/storeEnrollFrm";
		}
		
		@PostMapping(value="/storeEnroll")
		public String storeEnroll(Store store, MultipartFile[] edvienceUpFile, String address,String detailAddress, String[] closedDays, MultipartFile storeImgFile, MultipartFile[] menuImgFile, String[] name, int[] price, Model model) {
			
			//store 값 설정
			store.setStoreAddr(address+" "+detailAddress);	//도로명 + 상세주소
			
			String storeSavepath = root+"/store/";	//첨부파일이 무조건 있다는 가정하에(검사안함)
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
			
			//성공갯수 구해서 결과 화면 출력	-> 구해야댐..
			
			int count = 1+evidenceFileList.size()+closedDays.length+menuList.size();
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
			
			
			/*데이터 확인용
			System.out.println(store);
			
			
			for(String closeDay : closedDays) {
				System.out.print(closeDay);
			}
			System.out.println();
			
			for(String menuName : name) {
				System.out.println(menuName);
			}
			for(int menuPrice : price) {
				System.out.println(menuPrice);
			}
			*/
			
			
			return "common/msg";
		}
}
