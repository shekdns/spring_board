package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

/*
 * メンバー関連のSerivce interface
 */
public interface BoardService {

	/*
	 *掲示板の文章登録 
	 */
	public void register(BoardVO board);
	
	/*
	 * 掲示板の文章を読む
	 */
	public BoardVO get(int bno);
	
	/*
	 *掲示板の文の修正 
	 */
	public boolean modify(BoardVO board);
	
	/*
	 * 掲示板の文を削除
	 */
	public boolean remove(int bno);
	
	/*
	 * 掲示板のリスト
	 */
	public List<BoardVO> getList();
	
	/*
	 * 掲示板のリストページング
	 */
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	/*
	 *掲示板の文のtotalの数 
	 */
	public int getTotal(Criteria cri);
	
}
