package kr.gdu.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.gdu.Shop2Application;
import kr.gdu.config.MvcConfig;
import kr.gdu.exception.ShopException;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;
import kr.gdu.service.BoardService;

@Controller
@RequestMapping("board")
public class BoardController {

    private final Shop2Application shop2Application;

    private final MvcConfig mvcConfig;
	@Autowired
	private BoardService service;

    BoardController(MvcConfig mvcConfig, Shop2Application shop2Application) {
        this.mvcConfig = mvcConfig;
        this.shop2Application = shop2Application;
    }
	
	@GetMapping("*")
	public ModelAndView write() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Board());
		return mav;
	}
	/* Spring에서 파라미터 전달 방식
	 *   1. 파라미터이름과 매개변수의 이름이 같은 경우 매핑
	 *   2. Bean 클래스의 프로퍼티명과 파라미터이름이 같은 경우 매핑
	 *   3. Map 객체에 RequestParam 어노테이션을 이용한 매핑
	 * 
	 * @RequestParam : 파라미터값을 Map 객체에 매핑하여 전달
	 */
	@RequestMapping("list")
	public ModelAndView list(@RequestParam Map<String,String> param,
			 HttpSession session) {
//		System.out.println("param:" + param);
		Integer pageNum = null;
		for(String key : param.keySet()) {
			if(param.get(key) == null || param.get(key).trim().equals("")) {
			   param.put(key, null);	
			}
		}
		if (param.get("pageNum") != null) {
			   pageNum = Integer.parseInt(param.get("pageNum"));
		} else { 
			pageNum = 1;
		}
		String boardid = param.get("boardid");
		if(boardid == null) boardid = "1";
		String searchtype = param.get("searchtype");
		String searchcontent = param.get("searchcontent");
		
		ModelAndView mav = new ModelAndView();
		String boardName = null;
		switch(boardid) {
		   case "1" : boardName = "공지사항"; break;
		   case "2" : boardName = "자유게시판"; break;
		   case "3" : boardName = "QNA"; break;
		}
		//게시판 조회 처리
		int limit = 10; // 한 페이지 출력될 게시물 건수
		int listcount = service.boardcount(boardid,searchtype,searchcontent);
		// listcount : boardid 별 전체 게시물 건수 
		List<Board> boardlist = service.boardlist
				          (pageNum,limit,boardid,searchtype,searchcontent);
		// boardlist : 게시물 목록
		int maxpage = (int)((double)listcount/limit + 0.95);
		// maxpage : 최대 페이지 값
		int startpage = (int)((pageNum/10.0 + 0.9) - 1) * 10 + 1;
		// startpage 현재 화면에 보여질 시작 페이지 값
		int endpage = startpage + 9;
		if(endpage > maxpage) endpage = maxpage;
		int boardno = listcount - (pageNum - 1) * limit;
		mav.addObject("boardid",boardid);  
		mav.addObject("boardName", boardName); 
		mav.addObject("pageNum", pageNum); 
		mav.addObject("maxpage", maxpage); 
		mav.addObject("startpage", startpage);
		mav.addObject("endpage", endpage); 
		mav.addObject("listcount", listcount);
		mav.addObject("boardlist", boardlist);
		mav.addObject("boardno", boardno);		
		return mav;
	}
	
	@GetMapping("detail")
	public ModelAndView detail(int num) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num);
		service.addReadCnt(num);
		// if문을 통해 boardid null || 1공지 2자유 3qna
		String boardid =  board.getBoardid();
		if(boardid == null || boardid.equals("1")) {
			mav.addObject("boardName", "공지사항");
		} else if(boardid.equals("2")) {
			mav.addObject("boardName", "자유게시판");
		} else if(boardid.equals("3")) {
			mav.addObject("boardName", "QNA");
		}
		
		// 댓글
		List<Comment> commlist = service.commentlist(num);
		Comment comm = new Comment();
		comm.setNum(num);
		mav.addObject("board", board);
		mav.addObject("commlist", commlist);
		mav.addObject(comm);
		return mav;
	}
	
	@PostMapping("write")
	public ModelAndView writePost(@Valid Board board, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		if(board.getBoardid() == null) board.setBoardid("1");
		service.boardWrite(board, request);
		mav.setViewName("redirect:list?boardid=" + board.getBoardid());
		return mav;
	}
	
	@GetMapping({"reply", "update", "delete"})
	public ModelAndView getBoard(Integer num, String boardid) {
		ModelAndView mav = new ModelAndView();
		Board board = service.getBoard(num);
		mav.addObject("board", board);
		if(boardid == null || boardid.equals("1")) {
			mav.addObject("boardName", "공지사항");
		} else if(boardid.equals("2")) {
			mav.addObject("boardName", "자유게시판");
		} else if(boardid.equals("3")) {
			mav.addObject("boardName", "QNA");
		}
		return mav;
	}
	
	@PostMapping("update")
	public ModelAndView update(@Valid Board board, BindingResult bresult,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			return mav;
		}
		Board dbBoard = service.getBoard(board.getNum());
		if(!board.getPass().equals(dbBoard.getPass())) {
			throw new ShopException("비번 틀림", "update?num=" + board.getNum()
			+ "&oardid=" + dbBoard.getBoardid());
		}
		//입력값 정상, 비번 일치
		try {
			service.boardUpdate(board, request);
			mav.setViewName("redirect:detail?num=" + board.getNum());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShopException("게시글 수정 실패", "update?num=" + board.getNum()
			+ "&oardid=" + dbBoard.getBoardid());
		}
		return mav;
	}
	
	@PostMapping("delete")
	public ModelAndView delete(Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(board.getPass() == null || board.getPass().trim().equals("")) {
			bresult.reject("error.input.password");
			return mav;
		}
		
		Board dbBoard = service.getBoard(board.getNum());
		if(dbBoard.getPass().equals(board.getPass())) {
			service.boardDelete(board.getNum());
			mav.setViewName("redirect:list?boardid=" + board.getBoardid());
		} else {
			bresult.rejectValue("pass", "error.delete");
		}
		return mav;
	}
	@PostMapping("reply")
	public ModelAndView reply(@Valid Board board, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		
		// 유효성 검증
		if(bresult.hasErrors()) {
			return mav;
		}
		try {
			service.boardReply(board);
			mav.setViewName("redirect:list?boardid=" + board.getBoardid());
		} catch(Exception e) {
			e.printStackTrace();
			String url = "reply?num=" + board.getNum() +
					"&boardid=" + board.getBoardid();
			throw new ShopException("답변등록시 오류 발생", url);
		}
		return mav;
	}
	/*
	 * 1. 유효성 검사 - 파라미터 저장
	 * 		-원글 : num, grp, grplevel, grpstep, boardid
	 * 		-답글 : writer, pass, title, content
	 * 2. db에 insert => service.boardReply()
	 * 		원글의 grpstep 보다 큰 기존 등록된 답글의 grpstep 값 +1 로 수정
	 * 		=> boardDao.grpstepAdd()
	 * 
	 * 		num : maxNum() +1
	 * 		db에 insert => boardDao.insert()
	 * 		grp : 원글과 동일
	 * 		grplevel : 원글의 grplevel + 1
	 * 		grpstep : 원글의 grpstep + 1
	 * 3. 등록 성공 : list로 페이지 이동
	 *    등록 실패 : "답변 등록시 오류 발생" reply 페이지 이동
	 */
	
	@RequestMapping("comment") // 댓글등록
	public ModelAndView comment(@Valid Comment comm, BindingResult bresult) {
		ModelAndView mav = new ModelAndView("board/detail");
		if(bresult.hasErrors()) {
			return commdetail(comm);
		}
		
		// comment 테이블의 기본값 : num, seq
		int seq = service.commMaxSeq(comm.getNum());
		comm.setSeq(++seq);
		service.comminsert(comm);
		mav.setViewName("redirect:detail?num=" + comm.getNum() + "#comment");
		return mav;
	}
	
	private ModelAndView commdetail(Comment comm) {
		ModelAndView mav = detail(comm.getNum()); // 조회수증가. 수정 필요
		// comm : @Valid 완료한 객체. 오류 정보 저장
		mav.setViewName("board/detail");
		mav.addObject(comm);
		return mav;
	}
	
	@RequestMapping("commdel")
	public String commdel(Comment comm) {
		Comment dbComm = service.commSelectOne(comm.getNum(), comm.getSeq());
		if(comm.getPass().equals(dbComm.getPass())) {
			service.commdel(comm.getNum(), comm.getSeq());
		} else {
			throw new ShopException("댓글 삭제 실패",
					"detail?num=" + comm.getNum() + "#comment");
		}
		return "redirect:detail?num=" + comm.getNum() + "#comment";
	}
}
