package kr.gdu.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
		// File(parent, fileName) : dir폴더에 filesystemName 파일
		File file = new File(dir, filesystemName);
		try {
			multipartFile.transferTo(file); // 이미지 업로드
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "/board/image/" + filesystemName; // url 리턴
	}
	public String sidoSelect1(String si, String gu) {
		BufferedReader br = null;
		String path = UPLOAD_IMAGE_DIR + "data/sido.txt";
		try {
			br = new BufferedReader(new FileReader(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if(si == null && gu == null) {
			try {
				while((data = br.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim());
				}
			} catch(IOException e) { e.printStackTrace(); }
		}
		List<String> list = new ArrayList<>(set);
		return list.toString();
	}
	public List<String> sigunSelect2(String si, String gu) {
		BufferedReader br = null;
		String path = UPLOAD_IMAGE_DIR + "data/sido.txt";
		try {
			br = new BufferedReader(new FileReader(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		if(si == null && gu == null) {
			try {
				while((data=br.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) set.add(arr[0].trim());
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(gu == null) {
			si = si.trim();
			try {
				while((data = br.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si)
							&& !arr[1].contains(arr[0])) {
						set.add(arr[1].trim());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			si = si.trim();
			gu = gu.trim();
			try {
				while((data = br.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3 && arr[0].equals(si)
							&& arr[1].equals(gu) && !arr[0].equals(arr[1])
							&& !arr[2].contains(arr[1])) {
						if(arr.length > 3) {
							if(arr[3].contains(arr[1])) continue;
							arr[2] += " " + arr[3];
						}
						set.add(arr[2].trim());
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<String>(set);
	}
	public String exchange1() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr");
			exdate = doc.select("p.table-unit").html();
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for(Element td : tds) {
					tdlist.add(td.html());
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD")
						|| tdlist.get(0).equals("CNH")
						|| tdlist.get(0).equals("JPY(100)")
						|| tdlist.get(0).equals("EUR")) {
						trlist.add(tdlist);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h4 class='w3-center'>수출입은행<br>" + exdate + "</h4>");
		sb.append("<table class='w3-table-all'>");
		sb.append
	("<tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th></tr>");
		for(List<String> tds : trlist) {
			sb.append
	("<tr><td>" + tds.get(0) + "<br>" + tds.get(1) + "</td><td>" + tds.get(4) + "</td>");
			sb.append("<td>" + tds.get(2) + "</td><td>" + tds.get(3) + "</td></tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	public Map<String, Object> exchange2() {
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr");
			exdate = doc.select("p.table-unit").html();
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for(Element td : tds) {
					tdlist.add(td.html());
				}
				if(tdlist.size() > 0) {
					if(tdlist.get(0).equals("USD")
						|| tdlist.get(0).equals("CNH")
						|| tdlist.get(0).equals("JPY(100)")
						|| tdlist.get(0).equals("EUR")) {
						trlist.add(tdlist);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("exdate", exdate);
		map.put("trlist", trlist);
		return map;
	}
	public Map<String, Integer> graph1(String id) {
		List<Map<String, Object>> list = boardDao.graph1(id);
		Map<String, Integer> map = new HashMap<>();
		for(Map<String, Object> m : list) {
			String writer = (String)m.get("writer");
			long cnt = (Long) m.get("cnt");
			map.put(writer, (int) cnt);
		}
		return map;
	}
}
