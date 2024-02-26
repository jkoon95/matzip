package kr.or.iei.search.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpSession;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.search.model.dto.SearchListData;
import kr.or.iei.search.model.service.SearchService;
import kr.or.iei.store.model.dao.TwoList;
import kr.or.iei.store.model.dto.ClosedDay;
import kr.or.iei.store.model.dto.Menu;
import kr.or.iei.store.model.dto.Store;
import kr.or.iei.store.model.dto.StoreInfo;
import kr.or.iei.store.model.dto.StoreReview;
import kr.or.iei.store.model.service.StoreService;

@Controller
@RequestMapping(value = "/search")
public class SearchController {
	@Autowired
	private StoreService storeService;
	@Autowired
	private SearchService searchService; 
	
	
	@GetMapping(value="/searchStoreBySubway")
	public String searchStorePageBySubway() {
		return "search/searchStoreBySubway";
	}
	
	@GetMapping(value="/searchStore")
	public String searchStore() {
		return "search/searchStore";
	}
	
	@ResponseBody
	@GetMapping(value = "/ajaxSelectTop5Store")
	public List ajaxSelectTop5Store(String stationName,@SessionAttribute(required = false)Member member) {
		int number = 5;
		int memberNo = 0;
		
		if(member != null) {
			memberNo = member.getMemberNo();
		}
		
		//List list = searchService.memberLike(memberNo,)
		
		
		System.out.println(member);
		
		List list = searchService.selectTopStore(stationName,number,memberNo);
		//System.out.println("탑5:" + list);
		
		
		
		if(list.isEmpty()) {
			return null;
		}else {
			System.out.println(list);
			return list;
		}
	}
	

//	@ResponseBody
//	@GetMapping(value = "ajaxStoreList")
//	public List ajaxStoreList(int start,int amount,String stationName) {
//		//List list = storeService.ajaxStoreList(stationName);
//		
//		
//		int totalCount = searchService.storeTotalCount(stationName);
//		
//		
//	
//		List searchList = searchService.selectSearchList(start,amount,stationName);
//		System.out.println(stationName);
//		System.out.println(searchList);
//
//		
//		
//		return searchList;	
//	}
	
	@ResponseBody
	@GetMapping(value = "ajaxStoreList2")
	public TwoList ajaxStoreList2(int start,int amount,String stationName) {
		//List list = storeService.ajaxStoreList(stationName);
		
		int totalCount = searchService.storeTotalCount(stationName);
		System.out.println(totalCount);//성공
	
		List searchList = searchService.selectSearchList(start,amount,stationName);
		System.out.println(searchList);
		
		TwoList twoList = new TwoList();
		twoList.setTotalCount(totalCount);
		twoList.setSearchList(searchList);
		
		return twoList;	
	}

	//@ResponseBody
	//@GetMapping(value = "")
	
	@GetMapping(value="storeDetail")
	public String storeDetail() {
		return "search/storeDetail";
	}
	
	@GetMapping(value = "conveyStoreInfoToDetail")
	public String conveyStoreInfoToDetail(int storeNo,Model model) {
		Store store = searchService.selectSearchOne(storeNo);
	    // 상점의 공지 정보 조회 (INFO_TBL)
	    StoreInfo info = searchService.getStoreInfoByStoreNo(storeNo);
	    // 상점의 메뉴 정보 조회 (MENU_TBL)
	    List<Menu> menuList = searchService.selectAllMenu(storeNo);
		// 상점의 정기 휴무일 조회 (CLOSED_DAY_TBL)
	    List<ClosedDay> closedDayList = searchService.selectClosedDay(storeNo);
	    // 상점의 리뷰 조회 (REVIEW_TBL)
	    List<StoreReview> reviewList = searchService.selectStoreReview(storeNo);
	    
		model.addAttribute("store",store);
		model.addAttribute("info", info);
	    model.addAttribute("menuList", menuList);
	    model.addAttribute("closedDayList", closedDayList);
		model.addAttribute("reviewList", reviewList);
	    
		System.out.println("클릭!!!!!!!!!!!!"+store);
		
		return "search/storeDetail";
	}
	
	//@ResponseBody
	//@GetMapping(value = "likeView")
	//public String likeView(int memberNo,@SessionAttribute)
	
	@GetMapping(value = "searchStoreList")
	public String searchStoreList() {
		return "search/searchStoreList";
	}
	
	@PostMapping(value = "/updateInfo")
	public String updateInfo(StoreInfo i, Model model) {
	    int result = searchService.updateInfo(i);
	    if (result > 0) {
	    	// 상세 정보 페이지로 리다이렉트
	    	return "redirect:/search/conveyStoreInfoToDetail?storeNo=" + i.getStoreNo();
	    } else {
	    	return "redirect:/search/conveyStoreInfoToDetail?storeNo=" + i.getStoreNo();
	    }
	}
	
	@PostMapping(value = "/insertInfo")
	public String insertInfo(StoreInfo i, Model model) {
		int result = searchService.insertInfo(i);
		 if (result > 0) {
			 // 상세 정보 페이지로 리다이렉트
			 return "redirect:/search/conveyStoreInfoToDetail?storeNo=" + i.getStoreNo();
		 } else {
			 return "redirect:/search/conveyStoreInfoToDetail?storeNo=" + i.getStoreNo();
		 }
	}
	
	@PostMapping(value = "/insertReview")
	public String insertReview(StoreReview sr, Model model) {
		int result = searchService.insertReview(sr);
		if(result > 0) {
			model.addAttribute("title", "리뷰작성 성공");
			model.addAttribute("msg", "고객님의 리뷰가 작성되었습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "리뷰작성 실패");
			model.addAttribute("msg", "처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "warning");
		}
		model.addAttribute("loc", "/search/conveyStoreInfoToDetail?storeNo="+sr.getStoreNo());
		return "common/msg";
	}

	@PostMapping(value = "/updateReview")
	public String updateReview(StoreReview sr, Model model) {
		int result = searchService.updateReview(sr);
		if(result > 0) {
			model.addAttribute("title", "리뷰수정 성공");
			model.addAttribute("msg", "고객님의 리뷰가 수정되었습니다.");
			model.addAttribute("icon", "success");
		} else {
			model.addAttribute("title", "리뷰수정 실패");
			model.addAttribute("msg", "처리 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
			model.addAttribute("icon", "warning");
		}
		model.addAttribute("loc", "/search/conveyStoreInfoToDetail?storeNo="+sr.getStoreNo());
		return "common/msg";
	}
	
	@GetMapping(value="/deleteReview")
	public String deleteReview(int reviewNo, int storeNo, Model model) {
		int result = searchService.deleteReview(reviewNo);
		if(result > 0) {
			return "redirect:/search/conveyStoreInfoToDetail?storeNo=" + storeNo;
		} else {
			model.addAttribute("title", "삭제 실패");
			model.addAttribute("msg", "리뷰 삭제를 실패했습니다.");
			model.addAttribute("icon", "warning");
			model.addAttribute("loc", "/search/conveyStoreInfoToDetail?storeNo="+storeNo);
			return "common/msg";
		}
	}
	
}
