package kr.or.iei.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
