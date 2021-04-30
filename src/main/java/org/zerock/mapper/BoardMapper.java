package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;


/*
 * 掲示板関連のmapper interface
 */
public interface BoardMapper {
	
	/*
	 * 掲示板のリスト
	 */
	public List<BoardVO> getList();
	
	/*
	 * 掲示板のリストページング
	 */
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	/*
	 *掲示板の文章登録 
	 */
	public void insert(BoardVO board);
	
	/*
	 * 掲示板の文章登録sequence使用
	 */
	public Integer insertSelectKey(BoardVO board);
	
	/*
	 * 掲示板の文章を読む
	 */
	public BoardVO read(int bno);
	
	/*
	 * 掲示板の文を削除
	 */
	public int delete(int bno);
	
	/*
	 *掲示板の文の修正 
	 */
	public int update(BoardVO board);
	
	/*
	 *掲示板の文のtotalの数 
	 */
	public int getTotalCount(Criteria cri);
	
	
	// 2つ以上のパラメータを越えるために@Paramを使用。コメント追加·削除時にamountの1/-1値
	public void updateReplyCnt(@Param("bno") int bno, @Param("amount") int amount);
	
}
