package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.mapper.BoardMapper;
import org.zerock.mapper.ReplyMapper;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
//@AllArgsConstructor
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper mapper;
	
	@Autowired
	private BoardMapper boardMapper;
	
	/*
	 * 1. コメント登録メソッド
	 * 一つのメソッドの中に2つのmapperインターフェースを使用するため、
	 * トランザクション処理のために@Transactional使用 
	 * コメントが登録されるとboardMapperのupdateReplyCnt実行RelpyCnt増加
	 */
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		log.info("register......" + vo);
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		return mapper.insert(vo);
	}
	
	/*
	 * 2.コメントを見るメソッド。
	 * rno変数でコメントを詳細に読み込む
	 */
	@Override
	public ReplyVO get(int rno) {
		log.info("get......" + rno);
		return mapper.read(rno);
	}

	/*
	 * 3.コメント修正メソッド
	 * 
	 */
	@Override
	public int modify(ReplyVO vo) {
		log.info("modify......" + vo);
		return mapper.update(vo);
	}
	
	/*
	 * 4. コメント削除メソッド
	 * 一つのメソッドの中に2つのmapperインターフェースを使用するため、
	 * トランザクション処理のために@Transactional使用 
	 * コメントが削除されるとboardMapperのupdateReplyCnt実行RelpyCnt減少
	 */
	@Transactional
	@Override
	public int remove(int rno) {
		log.info("remove...." + rno);
		
		ReplyVO vo = mapper.read(rno);
		boardMapper.updateReplyCnt(vo.getBno(), -1);
		return mapper.delete(rno);
	}
	
	/*
	 * 5. コメントリスト
	 */
	@Override
	public List<ReplyVO> getList(Criteria cri, int bno) {
		log.info("get Reply List of a Board " + bno);
		return mapper.getListWithPaging(cri, bno);
	}
	
	/*
	 * 6. コメントリストページング
	 */
	@Override
	public ReplyPageDTO getListWithPaging(Criteria cri, int bno) {
       
		return new ReplyPageDTO(
				mapper.getCountByBno(bno), 
				mapper.getListWithPaging(cri, bno));
	}
}

