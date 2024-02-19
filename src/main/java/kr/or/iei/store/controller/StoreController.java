package kr.or.iei.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import kr.or.iei.store.model.service.StoreService;

@Controller
public class StoreController {
	@Autowired
	private StoreService storeService;
	
	
}
