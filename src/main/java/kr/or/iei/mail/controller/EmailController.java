package kr.or.iei.mail.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.iei.MatzipEmail;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;

@Controller
@RequestMapping(value="/mail")
public class EmailController {
	@Autowired
	private MatzipEmail matzipEmail;
	@Autowired
	private MemberService memberService;
	
	@GetMapping(value="idSearch")
	public String searchId() {
		return "email/searchId";
	}
	@GetMapping(value="pwSearch")
	public String searchPw() {
		return "email/searchPw";
	}
	@ResponseBody
	@GetMapping(value="checkName")
	public int checkName(String memberName) {
		Member member = memberService.emailCheckName(memberName);	
		if(member == null) {
			return 0;
		}else {
			return 1;
		}
	}
}
