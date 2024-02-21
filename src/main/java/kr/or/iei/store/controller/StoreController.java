package kr.or.iei.store.controller;

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
		public String storeEnroll(Store store, MultipartFile[] edvienceFile, String address,String detailAddress, String[] closedDays, MultipartFile storeImgFile, MultipartFile[] menuImgFile, String[] name, int[] price, Model model) {
			
			store.setStoreAddr(address+" "+detailAddress);	//도로명 + 상세주소
			
			String storeSavepath = root+"/store/";	//첨부파일이 무조건 있다는 가정하에(검사안함)
			String storeFilepath = fileUtils.upload(storeSavepath, storeImgFile);
			store.setStoreImg(storeFilepath);
			
			
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
