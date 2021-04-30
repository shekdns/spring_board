package org.zerock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/")
@RestController
@Log4j
public class ReplyController {
	
	@Autowired
	private ReplyService service;
	
	
	/*
	 * 1. コメント生成メソッド
	 * consumes は呼び出す側からapplicationjson の要請のみ受け入れる。 リクエストコンテンツタイプ制限
	 * producesは条件に指定したメディアタイプに関する応答を生成。 応答コンテンツタイプ制限
	 * Response Entityは、基本value値以外にステータスコード、応答メッセージを含めて送信するために使用
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/new", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> create(@RequestBody ReplyVO vo) {

		log.info("ReplyVO: " + vo);

		int insertCount = service.register(vo);

		log.info("Reply INSERT COUNT: " + insertCount);
		
		//三項演算子の使用
		//1なら、success 1でなければ、コメントエラーサーバーエラーを表示する。
		return insertCount == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>("Fail!! 댓글 등록 중 오류 발생",HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	/*
	 * 2.コメント詳細表示メソッド
	 * @PathVariableアノテーションを使用してURLにrno変数を入れることができる。 この変数を通じて選択したコメントを見ることができる。
	 */
	@GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") int rno) {

		log.info("get: " + rno);

		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}
	
	/*
	 * 3. コメント修正メソッド
	 * PUT、PATCH methodをすべて適用しなければならないので@Request Mappingを使用
	  *  今接続したユーザーと書き込みを作成したユーザーが同一でなければ修正できない。
	 */
	@PreAuthorize("principal.username == #vo.replyer")
	@RequestMapping(value = "/{rno}", method = { RequestMethod.PUT, RequestMethod.PATCH },
					consumes = "application/json", produces = {	MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") int rno) {
		
		//@Request Body処理されるデータは、一般パラメータや@Path Variableで処理することができない。
		vo.setRno(rno);

		log.info("rno: " + rno);
		log.info("modify: " + vo);

		//三項演算子の使用
		//1なら、success 1でなければ、コメントエラーサーバーエラーを表示する。
		return service.modify(vo) == 1 
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>("Fail!! 댓글 수정 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	/*
	 * 4. コメント削除メソッド
	 * 今接続したユーザーと書き込みを作成したユーザーが同一でなければ削除できない。
	 */
	@PreAuthorize("principal.username == #vo.replyer")
	@DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo, @PathVariable("rno") int rno) {

		log.info("remove: " + rno);
		log.info("replyer: " + vo.getReplyer());
		
		//三項演算子の使用
		//1なら、success 1でなければ、コメントエラーサーバーエラーを表示する。
		return service.remove(rno) == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>("Fail!! 댓글 삭제 중 오류 발생",HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	/*
	 * 5.コメントリストとページング
	  * リストメソッドはCriteriaを利用してパラメータを収集するが、bnoのページ値はCriteriaを生成して処理しなければならない。
	 *@PathVariableを使用してpageとbnoをURLに入れる。
	 */
	@GetMapping(value = "/pages/{bno}/{page}", produces = { MediaType.APPLICATION_XML_VALUE,
															MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") int bno) {
		
		//ページ数と表示数
		Criteria cri = new Criteria(page, 10);
		
		log.info("get Reply List bno: " + bno);

		log.info("cri:" + cri);

		return new ResponseEntity<>(service.getListWithPaging(cri, bno), HttpStatus.OK);
	}

}

