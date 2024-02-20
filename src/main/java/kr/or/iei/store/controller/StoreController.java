package kr.or.iei.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.iei.store.model.service.StoreService;

@Controller
@RequestMapping(value = "/store")
public class StoreController {
		@Autowired
		private StoreService storeService;
		
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
}
