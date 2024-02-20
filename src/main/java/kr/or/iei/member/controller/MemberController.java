package kr.or.iei.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.or.iei.member.model.dto.Member;
import kr.or.iei.member.model.service.MemberService;

@Controller
@RequestMapping(value="/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping(value="/joinFrm")
	public String joinFrm() {
		return "member/joinFrm";
	}
	@GetMapping(value="/login")
	public String login() {
		return "/member/login";
	}
	@PostMapping(value="/signin")
	public String signin(String memberId, String memberPw, int memberLevel, HttpSession session, Model model) {
		Member member = memberService.selectOneMember(memberId, memberPw, memberLevel);
		if(member ==null) {
			model.addAttribute("title", "로그인");
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}else {
			session.setAttribute("member", member);
			
			model.addAttribute("title", "환영합니다.");
			model.addAttribute("msg", "맛집 입니다");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
	@GetMapping(value="/logout")
	public String logout(HttpSession session) {
		session.invalidate();		
		return "redirect:/";
	}
	@PostMapping(value="/join")
	public String join(Member m, Model model) {
		int result = memberService.insertMember(m);
		if(result>0) {
			model.addAttribute("title", "congratulation");
			model.addAttribute("msg", "together 맛'zip'");
			model.addAttribute("icon", "success");
			model.addAttribute("loc", "/");
		}else {
			model.addAttribute("title", "다음기회에");
			model.addAttribute("msg", "실패했습니다...ㅜㅜ");
			model.addAttribute("icon", "error");
			model.addAttribute("loc", "/");
		}
		return "common/msg";
	}
}
