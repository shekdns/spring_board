package org.zerock.service;

import java.util.List;

import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;

/*
 * コメント関連のSerivce interface
 */
public interface ReplyService {
	
	//コメント登録 
	public int register(ReplyVO vo);
	
	//コメントを見る
	public ReplyVO get(int rno);

	//コメント修正
	public int modify(ReplyVO vo);
	
	//コメント削除
	public int remove(int rno);
	
	//コメントリスト
	public List<ReplyVO> getList(Criteria cri, int bno);
	
	//コメントリストページング
	public ReplyPageDTO getListWithPaging(Criteria cri, int bno);

}
