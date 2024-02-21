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
				String evidenceSavepath = root+"/evidence/";	//-> 웹컨피그가서 값설정해줘야함 아직안함
				for(MultipartFile file : edvienceUpFile) {
					//업로드한 파일명을 추출
					String evidenceFilename = file.getOriginalFilename();
					String evidenceFilepath = fileUtils.upload(evidenceSavepath, file);
					EvidenceFile evidenceFile = new EvidenceFile();
					evidenceFile.setFilename(evidenceFilename);
					evidenceFile.setFilepath(evidenceFilepath);
					evidenceFileList.add(evidenceFile);
				}
			}
			
			//휴무일 날짜
			//휴무일 배열 보냄
			//휴무일 널체크 해줘야함??????????????하나라도 안들어올시 for length에서 에러
			//closedDays
			
			
			//메뉴 등록값
			List<Menu> menuList = new ArrayList<Menu>();
			String menuSavepath = root+"/menu/";		//->얘도 웹컨피그가서 해줘야댐
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
			
			
			
			
			//성공갯수 구해야댐
			
			
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
			model.addAttribute("title","성공");
			model.addAttribute("msg","환영합니다");
			model.addAttribute("icon","success");
			model.addAttribute("loc","/");
			return "common/msg";
		}
}
