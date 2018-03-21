package com.ktds.community.web;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.community.service.CommunityService;
import com.ktds.community.vo.CommunityVO;
import com.ktds.member.constatnts.Member;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.DownloadUtil;

@Controller
public class CommunityController {

	private CommunityService communityService;

	public void setCommunityService(CommunityService communityService) {
		this.communityService = communityService;
	}

	@RequestMapping("/")
	public ModelAndView viewListPage(HttpSession session) {
		ModelAndView view = new ModelAndView();

		// if (session.getAttribute(Member.USER) == null) {
		// // /WEB-INF/view/community/list.jsp
		// return new ModelAndView("redirect:/login");
		// }

		view.setViewName("community/list");

		List<CommunityVO> communityList = communityService.getAll();

		view.addObject("communityList", communityList);
		return view;
	}

	@RequestMapping(value = "/write", method = RequestMethod.GET) // get
	// @GetMapping("/write")
	public String viewWritePage(HttpSession session) { // 데이터를 주면 ModelAndView 아니면 String도 상관 없음
		// if (session.getAttribute(Member.USER) == null) {
		// return "redirect:/login";
		// }
		return "community/write";

	}

	@RequestMapping(value = "/write", method = RequestMethod.POST) // post
	// @PostMapping("/write")
	public ModelAndView doWrite(@ModelAttribute("writeForm") @Valid // valid 다음 errors 순서 중요.(뒤에 와야함)
	CommunityVO communityVO, Errors errors, HttpSession session, HttpServletRequest request) {

		// if (session.getAttribute(Member.USER) == null) {
		// return new ModelAndView("redirect:/");
		// // HttpServletRequest request
		// // String title = request.getParameter("title");
		// }

		ModelAndView view = new ModelAndView();
		if (errors.hasErrors()) {
			view.setViewName("community/write");
			view.addObject("communityVO", communityVO);
			return view;
		}
		// 작성자의 IP를 얻어오는 코드
		String requestIp = request.getRemoteAddr();
		communityVO.setRequestIp(requestIp);
		// if (communityVO.getTitle() == null || communityVO.getTitle().length() == 0) {
		// session.setAttribute("status", "emptyTitle");
		// return "redirect:/write";
		// }else {
		// if (communityVO.getBody() == null || communityVO.getBody().length() == 0 ) {
		// session.setAttribute("status", "emptyBody");
		// return "redirect:/write";
		// }
		// else {
		// }
		// if (communityVO.getWriteDate() == null || communityVO.getWriteDate().length()
		// == 0 ) {
		// session.setAttribute("status", "emptywriteDate");
		// return "redirect:/write";
		// }
		// }
		communityVO.save();
		boolean isSuccess = communityService.createCommunity(communityVO); // 이것좀 만들어봐

		if (isSuccess) {
			return new ModelAndView("redirect:/");
		}
		return new ModelAndView("redirect:/write");
	}
	// session.removeAttribute("status");
	// return "redirect:/";
	//
	// }
	//
	// return "redirect:/write";
	// }

	@RequestMapping("/view/{id}") // id 이름 같아야함
	public ModelAndView viewViewPage(HttpSession session, @PathVariable int id) {
		// 로그인한 회원만 들어올 수 있다 아이디값 변수
		// if (session.getAttribute(Member.USER) == null) {
		// // /WEB-INF/view/community/list.jsp
		// return new ModelAndView("redirect:/login");
		// }
		ModelAndView view = new ModelAndView();

		view.setViewName("community/view");

		// TODO id 로 게시글 얻어오기
		CommunityVO community = communityService.getOne(id);
		view.addObject("community", community);
		return view;

	}

	@RequestMapping("/read/{id}")
	public String incrementViewCountFunc(@PathVariable int id) {
		if (communityService.incrementVC(id)) {
			return "redirect:/view/" + id;
		}
		return "redirect:/";

	}

	@RequestMapping("/recommend/{id}")
	public String recommendCount(@PathVariable int id) {
		if (communityService.incrementRC(id)) {
			return "redirect:/view/" + id;
		}

		return "redirect:/";
	}

	@RequestMapping("/get/{id}")
	public void download(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {

		CommunityVO community = communityService.getOne(id);
		String filename = community.getDisplayFilename();

		DownloadUtil download = new DownloadUtil(
				"/Users/Sol/Documents/workspace-sts-3.9.2.RELEASE/uploadFiles/" + filename);

		try {
			download.download(request, response, filename);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@RequestMapping (value="/modify/{id}", method=RequestMethod.GET)
	public ModelAndView viewModify(HttpSession session, @PathVariable int id) {
		
		ModelAndView view = new ModelAndView();
		view.setViewName("community/write");
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		CommunityVO community = communityService.getOne(id);
		
		int userId = member.getId();
		
		if ( userId != community.getUserId()) {
			System.out.println("dd");
			
			return new ModelAndView("error/404");
		}
		
		view.addObject("communityVO", community);
		view.addObject("mode", "modify");
		return view;
	
	}
	
	
	@RequestMapping(value="/modify/{id}", method= RequestMethod.POST)
	public String doModifyAction(HttpSession session,
								@PathVariable int id,
								HttpServletRequest request,
								@ModelAttribute("writeForm")
								@Valid CommunityVO communityVO,
								Errors errors) {
		
		MemberVO memberVO = (MemberVO) session.getAttribute(Member.USER);
		CommunityVO originalVO = communityService.getOne(id);
		
		if ( memberVO.getId() != originalVO.getUserId() ) {
			return "error/404";
		}
		if ( errors.hasErrors() ) {
			return "redirect:/modify/"+id;
		}
		
		CommunityVO newCommunity = new CommunityVO();
		newCommunity.setId(originalVO.getId());
		newCommunity.setUserId(memberVO.getId());
		
		boolean isModify = false;	//변경된게 하나도 없다. 
		
		//	1. IP 변경 확인
		String ip = request.getRemoteAddr();
		if ( !ip.equals(originalVO.getRequestIp())) {
			newCommunity.setRequestIp(ip);
			isModify = true;
		}
		
		//	2. 제목변경 확인. 
		if ( !originalVO.getTitle().equals(communityVO.getTitle())) {
			newCommunity.setTitle(communityVO.getTitle());
			isModify = true;
		}
		
		//	3. 내용변경확인
		if ( !originalVO.getTitle().equals(communityVO.getTitle())) {
			newCommunity.setTitle(communityVO.getTitle());
			isModify = true;
		}
		
		//	4. 파일변경 확인
	
		if ( communityVO.getDisplayFilename().length() > 0 ) {
			File file = new File("/Users/Sol/Documents/workspace-sts-3.9.2.RELEASE/uploadFiles/" + communityVO.getDisplayFilename());
			file.delete();
			communityVO.setDisplayFilename("");
		}
		else {
			communityVO.setDisplayFilename( originalVO.getDisplayFilename());
		}
		
		//  바뀐게 있으면 여기에 넣어준다.
		communityVO.save();
		if ( !originalVO.getDisplayFilename().equals(communityVO.getDisplayFilename())) {
			newCommunity.setTitle(communityVO.getDisplayFilename());
			isModify = true;
		}
		
		//	5. 변경있는지 확인.
		if (isModify) {
			//	6. update하는 service code 호출
			communityService.updateCommunity(newCommunity);
		}
		return "redirect:/view"+id;		
	}
	
	@RequestMapping("/delete/{id}")
	public String deletePage(@PathVariable int id,
							HttpSession session
							) {
		communityService.removePage(id);	//id받아온걸 서비스에 준다. 삭제한다. 
	
		MemberVO member = (MemberVO) session.getAttribute(Member.USER);
		
		CommunityVO community = communityService.getOne(id);
		boolean isMyCommunity = member.getId() == community.getUserId();
	      
	      if(isMyCommunity && communityService.removePage(id)) {
	         return "redirect:/";
	      }
	      return "/error/404";
	}
}
