package com.tech.whale.admin.controllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tech.whale.admin.board.service.AdminBoardDelLogListService;
import com.tech.whale.admin.board.service.AdminBoardFeedDelete;
import com.tech.whale.admin.board.service.AdminBoardListService;
import com.tech.whale.admin.board.service.AdminBoardPostContentService;
import com.tech.whale.admin.board.service.AdminBoardPostDelete;
import com.tech.whale.admin.board.service.AdminNoticeListService;
import com.tech.whale.admin.board.service.AdminWhaleNotiService;
import com.tech.whale.admin.dao.AdminIDao;
import com.tech.whale.admin.dto.AdminPFCDto;
import com.tech.whale.admin.dto.AdminSearchDto;
import com.tech.whale.admin.service.AdminMyImgUrl;
import com.tech.whale.admin.util.AdminSearchVO;
import com.tech.whale.community.dao.ComDao;
import com.tech.whale.community.dto.CommentDto;
import com.tech.whale.community.dto.PostDto;
import com.tech.whale.community.service.ComLikeCommentService;
import com.tech.whale.community.service.PostUpdateService;
import com.tech.whale.feed.dao.FeedDao;
import com.tech.whale.feed.dto.FeedCommentDto;
import com.tech.whale.feed.dto.FeedDto;
import com.tech.whale.feed.service.FeedCommentService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminBoardController {

	private final AdminBoardListService adminBoardListService;
	private final AdminBoardPostContentService adminBoardPostContentService;
	private final ComLikeCommentService comLikeCommentService;
	private final AdminBoardPostDelete adminBoardPostDelete;
	private final AdminBoardFeedDelete adminBoardFeedDelete;
	private final FeedCommentService feedCommentsService;
	private final AdminNoticeListService adminNoticeListService;
	private final AdminWhaleNotiService adminWhaleNotiService;
	private final AdminBoardDelLogListService AdminBoardDelLogListService;
	private final PostUpdateService postUpdateService;
	private final ComDao comDao;
	private final FeedDao feedDao;
	private final AdminMyImgUrl adminMyImgUrl;
	
	@ModelAttribute("myId")
    public String addUserIdToModel(HttpSession session) {
        return (String) session.getAttribute("user_id");
    }
	
	// 본인 아이디 이미지 유알엘
	@ModelAttribute("myImgUrl")
	public String myImgUrl(Model model,HttpSession session) {
		return adminMyImgUrl.myImgSty(model);
	}
	
	// 서브 메뉴바 이름 삽입. 순서있어야 해서 LinkedHashMap 사용.
	public void boardSubBar(Model model) {
	    Map<String, String> subMenu = new LinkedHashMap<>();
	    subMenu.put("adminBoardListView", "게시글");
	    subMenu.put("adminBoardCommentsListView", "댓글");
	    subMenu.put("adminNoticeListView", "커뮤 공지사항");
	    subMenu.put("adminWhaleNotiListView", "알람 공지사항");
	    subMenu.put("adminBoardDelLogListView", "삭제내역");
	    
	    model.addAttribute("subMenu", subMenu);
	}
	
	//게시판 목록 검색
	@RequestMapping("/adminBoardListView")
	public String adminBoardListView(
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "게시글");
		model.addAttribute("contentBlockJsp",
				"../board/adminBoardListContent.jsp");
	    model.addAttribute("contentBlockCss",
	    		"/whale/static/css/admin/account/adminAccountUserListContent.css");
	    boardSubBar(model);
	    
	    AdminSearchDto<AdminPFCDto> searchDto =
	    		adminBoardListService.execute(model);
	    model.addAttribute("searchDto", searchDto);
	    
	    System.out.println(searchDto.getList().size()+" < 리스트 개수");
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminBoardPostContentView")
	public String adminBoardContentView(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			@RequestParam("communityName") String communityName,
			@RequestParam("postId") String postId,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("pname", "게시글");
		model.addAttribute("contentBlockJsp",
				"../board/adminBoardPostContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/board/adminBoardPostContent.css");
		boardSubBar(model);
		
		PostDto postDetail = comDao.getPost(postId);
		List<CommentDto> commentsList = comLikeCommentService.getCommentsForPost(postId);
		postDetail.setComments(commentsList);

		model.addAttribute("page", page);
		model.addAttribute("searchType", searchType);
		model.addAttribute("sk", sk);
		model.addAttribute("communityName", communityName);
		model.addAttribute("postId", postId);
		model.addAttribute("postDetail", postDetail);
		adminBoardPostContentService.execute(model);
		
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminBoardPostContentDelete")
	public String adminBoardPostContentDelete(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		adminBoardPostDelete.execute(model);
		
		return "redirect:adminBoardListView?page="+page+
				"&sk="+sk+"&searchType="+searchType;
	}
	
	@RequestMapping("/adminBoardFeedContentView")
	public String adminBoardFeedContentView(
			@RequestParam("f") String feedId,
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("pname", "피드");
		model.addAttribute("contentBlockJsp",
				"../board/adminBoardFeedContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/board/adminBoardFeedContent.css");
		boardSubBar(model);
		
		String now_id = (String) session.getAttribute("user_id");
		
		FeedDto feedDetail = feedDao.getFeedOne(feedId);
		
		List<FeedCommentDto> commentsList = feedCommentsService.getCommentsForFeed(feedId);
		
	    for (FeedCommentDto comment : commentsList) {
	        List<FeedCommentDto> replies = 
	        		feedCommentsService.getRepliesForComment(comment.getFeed_comments_id());
	        comment.setReplies(replies);
	    }
		
		feedDetail.setFeedComments(commentsList);
		
		model.addAttribute("now_id", now_id);
		model.addAttribute("feedDetail", feedDetail);
		
		
		model.addAttribute("page", page);
		model.addAttribute("searchType", searchType);
		model.addAttribute("sk", sk);
		
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminBoardFeedContentDelete")
	public String adminBoardFeedContentDelete(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		adminBoardFeedDelete.execute(model);
		
		return "redirect:adminBoardListView?page="+page+
				"&sk="+sk+"&searchType="+searchType;
	}
	
	@RequestMapping("/adminBoardFeedCommentsContentDelete")
	public String adminBoardFeedCommentsContentDelete(
			@RequestParam("page") int page,
			@RequestParam("f") String f,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		adminBoardFeedDelete.commentsDelete(model);
		
		return "redirect:adminBoardFeedContentView?page="+page+
				"&sk="+sk+"&searchType="+searchType+"&f="+f;
	}
	
	@RequestMapping("/adminBoardPostCommentsContentDelete")
	public String adminBoardPostCommentsContentDelete(
			@RequestParam("page") int page,
			@RequestParam("postId") String postId,
			@RequestParam("communityName") String communityName,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		adminBoardPostDelete.commentsDelete(model);
		
		///////  자식 댓글있으면 전부 삭제?
		
		return "redirect:adminBoardPostContentView?page="+page+
				"&sk="+sk+"&searchType="+searchType+"&postId="+postId+"&communityName="+communityName;
	}
	
	@RequestMapping("/adminBoardCommentsListView")
	public String adminBoardCommentsListView(
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "댓글");
		model.addAttribute("contentBlockJsp",
				"../board/adminBoardCommentsListContent.jsp");
	    model.addAttribute("contentBlockCss",
	    		"/whale/static/css/admin/account/adminAccountUserListContent.css");
	    boardSubBar(model);
	    
	    adminBoardListService.comments(model);
	    
		return "/admin/view/adminOutlineForm";
	}
	
	
	
	@RequestMapping("/adminBoardCommentsDelete")
	public String adminBoardCommentsDelete(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			@RequestParam(value = "feedId", required = false) String feedId,
	        @RequestParam(value = "postId", required = false) String postId,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		
		System.out.println("feedId :  "+feedId);
		System.out.println("postId :  "+postId);
		
		if(feedId != null) {
			adminBoardFeedDelete.commentsDelete(model);
		} else if(postId != null) {
			adminBoardPostDelete.commentsDelete(model);
		}else {
			System.out.println("글번호 안들어옴 오류");
		}
		
		return "redirect:adminBoardCommentsListView?page="+page+
				"&sk="+sk+"&searchType="+searchType;
	}
	
	
	@RequestMapping("/adminNoticeListView")
	public String adminNoticeListView(
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "공지사항");
		model.addAttribute("contentBlockJsp",
				"../board/adminNoticeListContent.jsp");
	    model.addAttribute("contentBlockCss",
	    		"/whale/static/css/admin/account/adminAccountUserListContent.css");
	    boardSubBar(model);
	    
	    adminNoticeListService.execute(model);
	    
	    
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminNoticeRegView")
	public String adminNoticeRegView(
			HttpServletRequest request,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("pname", "공지사항");
		model.addAttribute("contentBlockJsp",
				"../board/adminNoticeRegContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/board/adminNoticeRegContent.css");
		boardSubBar(model);
		
		adminNoticeListService.communitySelect(model);
		
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminNoticeRegDo")
	public String communityRegDo(
			@RequestParam("community_id") int communityId,
            @RequestParam("post_title") String post_title,
            @RequestParam("post_text") String post_text,
            @RequestParam("user_id") String user_id,
            @RequestParam("post_tag_id") int post_tag_id,
            Model model) {
		
		PostDto postDto = new PostDto();
		postDto.setCommunity_id(communityId);
		postDto.setUser_id(user_id);
		postDto.setPost_title(post_title);
		postDto.setPost_text(post_text);
		postDto.setPost_tag_id(post_tag_id);
		
        try {
            // 게시글 및 이미지 저장
        	adminNoticeListService.registerNotice(postDto, model);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "이미지 업로드 중 오류가 발생했습니다.");
            return "errorPage";
        }

        return "redirect:adminNoticeListView";
	}
	
	@RequestMapping("/adminNoticeUpdateView")
	public String adminNoticeUpdateView(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			@RequestParam("postId") String postId,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		model.addAttribute("pname", "공지사항");
		model.addAttribute("contentBlockJsp",
				"../board/adminNoticeUpdateContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/board/adminNoticeRegContent.css");
		boardSubBar(model);
		
		model.addAttribute("page", page);
		model.addAttribute("searchType", searchType);
		model.addAttribute("sk", sk);
		adminBoardPostContentService.execute(model);
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminNoticeContentView")
	public String adminNoticeContentView(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			@RequestParam("postId") String postId,
			HttpServletRequest request,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("pname", "게시글");
		model.addAttribute("contentBlockJsp",
				"../board/adminNoticeContent.jsp");
		model.addAttribute("contentBlockCss",
				"/whale/static/css/admin/board/adminBoardPostContent.css");
		boardSubBar(model);
		
		PostDto postDetail = comDao.getPost(postId);
		List<CommentDto> commentsList = comLikeCommentService.getCommentsForPost(postId);
		postDetail.setComments(commentsList);

		model.addAttribute("page", page);
		model.addAttribute("searchType", searchType);
		model.addAttribute("sk", sk);
		model.addAttribute("postId", postId);
		model.addAttribute("postDetail", postDetail);
		adminBoardPostContentService.execute(model);
		
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminBoardNoticeCommentsContentDelete")
	public String adminBoardNoticeCommentsContentDelete(
			@RequestParam("page") int page,
			@RequestParam("postId") String postId,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		adminBoardPostDelete.commentsDelete(model);
		
		///////  자식 댓글있으면 전부 삭제?
		
		return "redirect:adminNoticeContentView?page="+page+
				"&sk="+sk+"&searchType="+searchType+"&postId="+postId;
	}
	
	@RequestMapping("/adminNoticeContentDelete")
	public String adminNoticeContentDelete(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpSession session,
			HttpServletRequest request,
			Model model) {
		model.addAttribute("request", request);
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("user_id", user_id);
		adminBoardPostDelete.noticeDel(model);
		
		return "adminNoticeListView?page="+page+
				"&sk="+sk+"&searchType="+searchType;
	}
	
	
	@RequestMapping("/adminNoticeUpdateDo")
	public String adminNoticeUpdateDo(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			@RequestParam("post_id") int postId,
			@RequestParam("community_id") int communityId,
            @RequestParam("post_title") String postTitle,
            @RequestParam("post_text") String postText,
            @RequestParam("post_tag_id") int postTagId,
            RedirectAttributes redirectAttributes,
            Model model) {
		
		PostDto postDto = new PostDto();
	    postDto.setPost_title(postTitle);
	    postDto.setPost_text(postText);
	    postDto.setPost_tag_id(postTagId);
	    
	    List<Integer> notice_list = adminNoticeListService.noticeList(postId);
	    for(Integer var : notice_list) {
	    	postDto.setPost_id(var);
	    	postUpdateService.updatePost(postDto);
	    }

        return "redirect:adminNoticeContentView?postId="+postId
        		+ "&page=" + page
        		+ "&searchType=" + searchType
        		+ "&sk=" + sk;
	}
	
	
	//////////////////////// 공지알람 리스트 자리
	@RequestMapping("/adminWhaleNotiListView")
	public String adminWhaleNotiListView(
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "공지사항");
		model.addAttribute("contentBlockJsp",
				"../board/adminWhaleNotiListContent.jsp");
	    model.addAttribute("contentBlockCss",
	    		"/whale/static/css/admin/account/adminAccountUserListContent.css");
	    boardSubBar(model);
	    
	    adminWhaleNotiService.execute(model);
	    
		return "/admin/view/adminOutlineForm";
	}
	
	@RequestMapping("/adminWhaleNotiRegDo")
	public String adminWhaleNotiRegDo(
			@RequestParam("page") int page,
			@RequestParam("searchType") String searchType,
			@RequestParam("sk") String sk,
			HttpServletRequest request,
            Model model) {
		
		model.addAttribute("request", request);
		adminWhaleNotiService.whaleNotiRegDo(model);

        return "redirect:adminWhaleNotiListView?"
        		+ "page=" + page
        		+ "&searchType=" + searchType
        		+ "&sk=" + sk;
	}
	
	@RequestMapping("/adminBoardDelLogListView")
	public String adminBoardDelLogListView(
			HttpServletRequest request,
			AdminSearchVO searchVO,
			Model model) {
		
		model.addAttribute("request", request);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("pname", "삭제내역");
		model.addAttribute("contentBlockJsp",
				"../board/adminBoardDelListContent.jsp");
	    model.addAttribute("contentBlockCss",
	    		"/whale/static/css/admin/account/adminAccountUserListContent.css");
	    boardSubBar(model);
	    
	    AdminBoardDelLogListService.execute(model);
	    
		return "/admin/view/adminOutlineForm";
	}
	
}
