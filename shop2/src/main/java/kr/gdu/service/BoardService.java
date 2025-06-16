package kr.gdu.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.gdu.dao.BoardDao;
import kr.gdu.dao.CommDao;
import kr.gdu.logic.Board;
import kr.gdu.logic.Comment;

@Service
public class BoardService {
	
	@Value("${summernote.imgupload}")
	private String UPLOAD_IMAGE_DIR;
	@Autowired
	BoardDao boardDao;
	@Autowired
	CommDao commDao;
	
	public int boardcount(String boardid, String searchtype, String searchcontent) {
		return boardDao.count(boardid,searchtype,searchcontent);
	}
	public List<Board> boardlist
	(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
		return boardDao.list(pageNum,limit,boardid,searchtype,searchcontent);
	}
	public Board getBoard(int num) {
		return boardDao.selectOne(num);
	}
	public void addReadCnt(int num) {
		boardDao.addReadCnt(num);
	}
	public void boardWrite(Board board, HttpServletRequest request) {
		int maxnum = boardDao.maxNum();
		board.setNum(++maxnum);;
		board.setGrp(maxnum);
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")
					+ "board/file/";
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.insert(board);
	}
	private void uploadFileCreate(MultipartFile file, String path) {
		String orgFile = file.getOriginalFilename();
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		try {
			file.transferTo(new File(path + orgFile));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void boardUpdate(Board board, HttpServletRequest request) {
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")
					+ "board/file/";
			this.uploadFileCreate(board.getFile1(), path);
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		boardDao.update(board);
	}
	public void boardDelete(int num) {
		boardDao.delete(num);
	}
	public void boardReply(Board board) {
		boardDao.grpStepAdd(board);
		int max = boardDao.maxNum();
		board.setNum(++max);
		board.setGrplevel(board.getGrplevel() + 1);
		board.setGrpstep(board.getGrpstep() + 1);
		boardDao.insert(board);
	}
	public List<Comment> commentlist(Integer num) {
		return commDao.list(num);
	}
	public int commMaxSeq(int num) {
		return commDao.maxSeq(num);
	}
	public void comminsert(Comment comm) {
		commDao.insert(comm);
	}
	public Comment commSelectOne(int num, int seq) {
		return commDao.selectOne(num, seq);
	}
	public void commdel(int num, int seq) {
		commDao.delete(num, seq);
	}
	public String summernoteImageUpload(MultipartFile multipartFile) {
		File dir = new File(UPLOAD_IMAGE_DIR + "board/image");
		if(!dir.exists()) dir.mkdirs();
		String filesystemName = multipartFile.getOriginalFilename();
		File file = new File(dir, filesystemName);
		try {
			multipartFile.transferTo(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "/board/image/" + filesystemName;
	}
}
