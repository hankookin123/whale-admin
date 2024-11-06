package com.tech.whale.admin.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tech.whale.admin.dao.AdminIDao;
import com.tech.whale.admin.report.service.AdminReportListService;
import com.tech.whale.admin.report.service.AdminReportResultService;
import com.tech.whale.admin.service.AdminAccountUserInfoService;
import com.tech.whale.admin.service.AdminAccountUserModifyService;
import com.tech.whale.admin.util.AdminSearchVO;



@Controller
@RequestMapping("/admin")
public class AdminReportController {
	
	@Autowired
	private AdminIDao AdminIDao;
	@Autowired
	private AdminReportListService adminReportListService;
	@Autowired
	private AdminAccountUserInfoService adminAccountUserInfoService;
	@Autowired
	private AdminReportResultService adminReportResultService;
	

	@ModelAttribute("myId")
    public String addUserIdToModel(HttpSession session) {
        return (String) session.getAttribute("user_id");
    }
	
	public void boardSubBar(Model model) {
	    Map<String, String> subMenu = new LinkedHashMap<>();
	    subMenu.put("adminReportListView", "신고");
	    subMenu.put("", "문의 생겨?");
	    
	    model.addAttribute("subMenu", subMenu);
	}
	
	@RequestMapping("/adminReportListView")
	public String adminReportListView(
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "신고문의");
		model.addAttribute("contentBlockJsp",
				"../report/adminReportListContent.jsp");
	    model.addAttribute("contentBlockCss",
	    		"/whale/static/css/admin/account/adminAccountUserListContent.css");
	    boardSubBar(model);
	    
	    adminReportListService.execute(model);
	    
		return "/admin/view/adminOutlineForm";
	}
	@RequestMapping("/adminReportContentView")
	public String adminReportContentView(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "신고문의");
		model.addAttribute("contentBlockJsp",
				"../report/adminReportContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/account/adminAccountUserInfoContent.css");
		boardSubBar(model);
		model.addAttribute("page", page);
		model.addAttribute("searchType", searchType);
		model.addAttribute("sk", sk);
		
		adminReportListService.content(model);
		adminAccountUserInfoService.execute(model);
		
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminReportSubmit")
	@Transactional
	public String reportStatusForm(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			@RequestParam("report_id") int report_id,
			HttpServletRequest request,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("pname", "신고문의");
		model.addAttribute("contentBlockJsp",
				"../report/adminReportContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/account/adminAccountUserInfoContent.css");
		boardSubBar(model);
		
		adminReportResultService.execute(model);
		adminReportResultService.userBan(model);
		adminReportResultService.writingDel(model);
		
		return "redirect:adminReportContentView?"
				+ "page="+page+"&sk="+sk+"&searchType="+searchType+"&report_id="+report_id;
	}
	
}
