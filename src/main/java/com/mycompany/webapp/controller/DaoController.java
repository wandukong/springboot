package com.mycompany.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.webapp.dto.Board;
import com.mycompany.webapp.dto.Pager;
import com.mycompany.webapp.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/dao")
@Slf4j
public class DaoController {

	@Resource
	private BoardService boardService;

	@RequestMapping("/content")
	public String content() {
		log.info("실행");
		return "dao/content";
	}

	@RequestMapping("/boardList")
	public String boardList(@RequestParam(defaultValue = "1") int pageNo, Model model) {
		log.info("실행");

		int totalRows = boardService.getTotalBoardNum();
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		model.addAttribute("pager", pager);

		List<Board> boards = boardService.getBoards(pager);
		model.addAttribute("boards", boards);
		return "dao/boardList";
	}

	@GetMapping("/boardWriteForm")
	public String boardWriteForm() {
		log.info("실행");
		return "dao/boardWriteForm";
	}

	@PostMapping("/boardWrite")
	public String boardWrite(Board board) throws IllegalStateException, IOException {
		log.info("실행");

		// 첨부파일 있을 경우
		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			MultipartFile mf = board.getBattach();
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachsname(new Date().getTime() + "-" + mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			File file = new File("C:/hyundai_ite/upload_files/" + board.getBattachsname());
			mf.transferTo(file);
		}
		boardService.writeBoard(board);
		return "redirect:/dao/boardList";
	}

	@GetMapping("/boardDetail")
	public String boardDetail(int bno, Model model) {
		log.info("실행");
		Board board = boardService.getBoard(bno);
		model.addAttribute("board", board);
		return "dao/boardDetail";
	}

	@GetMapping("/battachDownload")
	public void battachDownload(int bno, HttpServletResponse response) throws Exception {
		log.info("실행");
		Board board = boardService.getBoard(bno);
		String battachoname = board.getBattachoname();
		if (battachoname == null)
			return;

		battachoname = new String(battachoname.getBytes("UTF-8"), "ISO-8859-1"); // 한글 파일명

		String battachsname = board.getBattachsname();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + battachoname + "\";"); // 다운로드를 강제로 받게함.

		String battachtype = board.getBattachtype();
		response.setContentType(battachtype);

		String battachspath = "C:/hyundai_ite/upload_files/" + battachsname;
		InputStream is = new FileInputStream(battachspath);
		OutputStream os = response.getOutputStream();
		FileCopyUtils.copy(is, os);
		is.close();
		os.flush();
		os.close();
	}

	@GetMapping("/boardUpdateForm")
	public String boardUpdateForm(int bno, Model model) {
		log.info("실행");
		Board board = boardService.getBoard(bno);
		model.addAttribute("board", board);
		return "dao/boardUpdateForm";
	}

	@PostMapping("/boardUpdate")
	public String boardUpdate(Board board) throws Exception {
		log.info("실행");

		if (board.getBattach() != null && !board.getBattach().isEmpty()) {
			MultipartFile mf = board.getBattach();
			board.setBattachoname(mf.getOriginalFilename());
			board.setBattachsname(new Date().getTime() + "-" + mf.getOriginalFilename());
			board.setBattachtype(mf.getContentType());
			File file = new File("C:/hyundai_ite/upload_files/" + board.getBattachsname());
			mf.transferTo(file);
		}

		boardService.updateBoard(board);
		return "redirect:/dao/boardDetail?bno=" + board.getBno();
	}

	@GetMapping("/boardDelete")
	public String boardDelete(int bno) {
		log.info("실행");
		boardService.removeBoard(bno);
		return "redirect:/dao/boardList";
	}
}
