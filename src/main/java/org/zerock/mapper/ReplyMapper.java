package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

/*
 * コメント関連のmapper interface
 */
public interface ReplyMapper {
	
	//コメント登録
	public int insert(ReplyVO vo);
	
	//コメントの読み込み。
	public ReplyVO read(int bno);
	
	//コメント削除
	public int delete(int bno);
	
	//コメント修正
	public int update(ReplyVO reply);
	
	//コメントリスト
	public List<ReplyVO> getList(@Param("cri") Criteria cri, @Param("bno") int bno);
	
	//コメントリストページング
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") int bno);

	//コメントの全体数。
	public int getCountByBno(int bno);
	

}
