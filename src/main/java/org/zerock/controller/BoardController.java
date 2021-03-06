package org.zerock.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;
import org.zerock.util.UploadUtils;


import lombok.extern.log4j.Log4j;

/*
 * 掲示板コントローラー
 */
@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	
	
	@Value("${globalConfig.uploadPath}")
	private String uploadPath;
	
	@Autowired
	private BoardService service;
		
	/*
	 * 1. 掲示板リストページ
	 * Criteriaオブジェクトを受け取ってpageNumとamountを計算する。
	 * getTotalメソッドを通じて総文数を求める。
	 * PageDTOオブジェクトを通してCriteriaのpageNumとamountで開始ページと最後ページを求める。
	 * Modelオブジェクトを通じてJSPにパラメータを送る。
	 * PreAuthorize アノテーションを通じてログインしたユーザー及び管理者だけ掲示板ページに接続可能
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		
//		cri = new Criteria(cri.getPageNum(), 1);
		log.info("list : " + cri);
		
		// 掲示板の文は持続的に登録、削除されるので、毎回リストを呼び出すときtotalを求めなければならない。
		int total = service.getTotal(cri);
		log.info("total : " + total);
		
		model.addAttribute("list", service.getListWithPaging(cri));
		model.addAttribute("pageMaker", new PageDTO(cri, total));
		

	}
	/*
	 * 2. 文章登録ページ (GET)
	 * isAuthenticated() ログインした人だけ文を作成可能
	 */
	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void register() {
		
	}
	
	/*
	 * 2. 文章登録ページ (POST)
	 * formタグでBoardVO情報を受けて文を登録する。
	  *  文登録が完了したら、addFlashAttributeで登録された文番号と結果をJSPに転送する。 転送以降のモデルデータは消滅する。
	  * その後returnを通じてリストページに戻ることになる。
	  * Multipart Fileオブジェクトでアップロードファイル設定
	  * for文を繰り返しながらview側にアップロードされたファイル数だけsetFile数を指定
	 */
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {
		
		int index = 0;

		
		log.info("register : board");
		
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		
		service.register(board);
		
		rttr.addFlashAttribute("result", board.getBno());
		
		return "redirect:/board/list";
		
	}
	
	/*
	 * 3. 読み込みページおよび修正ページ (GET) 
	 * @RequestParamを通じて選択した文の番号をパラメータに転送し、
	 * @ModelAttributeで自動的に現在のページ番号をパラメータに転送する。
	 */
	@GetMapping({ "/get", "/modify" })
	public void get(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) {

		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
	}
	
	
	/*
	 * 4. 修正ページ (POST)
	  *  現在接続中のユーザー名と書き込みを登録したユーザー名が同じ場合のみ修整可能
	 * form タグでBoardVO 情報を受け取って再登録·修正
         *  正常に修正された場合、addFlashAttributeで結果を転送
	 * returnを通じてリストページに戻る。Criteriaオブジェクトを通じて元のページ番号に戻る。
	 * Multipart Fileオブジェクトでアップロードファイル設定
	 * for文を繰り返しながらview側にアップロードされたファイル数だけsetFile数を指定
	 */
	
	
	/* ファイル削除メソッド 
	 * attachListにあるファイルを全部消す。
	 */
	private void deleteFiles(List<BoardAttachVO> attachList) {
		//配列内に何の値もない場合にはreturn
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		//for Eachを回りながらファイル経路やuuid、名前を探してdeleteIfEixts関数を通じて削除させる。
		//もしファイルがイメージの場合はサムネイルイメージまで削除させる。
		attachList.forEach(attach ->{
			try {
				Path file = Paths.get(uploadPath+"\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
				
				Files.deleteIfExists(file);
				
				if(Files.probeContentType(file).startsWith("image")) {
					
					Path thumbNail = Paths.get(uploadPath+"\\"+attach.getUploadPath()+"\\"+attach.getUuid()+"_"+attach.getFileName());
					
					Files.delete(thumbNail);
				}
				
				
			}catch(Exception e) {
				log.error("delete file error" + e.getMessage());
				
			}
			
		});
		
	}
	
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		
		log.info("modify : " + board );
		
		
		
		if (service.modify(board)) {
	         rttr.addFlashAttribute("result", "success");
	    }

	    return "redirect:/board/list" + cri.getListLink();
		
	}
	
	
	/*
	 * 5. 文章削除
	  *  現在接続中のユーザー名と書き込みを登録したユーザー名が同じ場合のみ削除可能
	  *  選択した文番号をパラメータに転送して削除
	  *  正常に削除された場合、addFlashAttributeで結果を転送
	 * returnを通じてリストページに戻る。Criteriaオブジェクトを通じて元のページ番号に戻る。
	 */
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr, String writer) {

		log.info("remove..." + bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		if (service.remove(bno)) {
			
			deleteFiles(attachList);
			
			rttr.addFlashAttribute("result", "success");
		}
		
		return "redirect:/board/list" + cri.getListLink();
	}
	
	@GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(int bno){
		
		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
		
		
	}
	
}
