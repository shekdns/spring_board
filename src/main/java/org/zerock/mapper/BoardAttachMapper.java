package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardAttachVO;

/*
 * ファイルアップロード Mapper
 */
public interface BoardAttachMapper {
	
	/*
	 * ファイル登録
	 */
	public void insert(BoardAttachVO vo);
	
	/*
	 * ファイル削除
	 */
	public void delete(String uuid);
	
	/*
	 *ファイルの詳細値を取得
	 */
	public List<BoardAttachVO> findByBno(int bno);
	
	/*
	 * ファイルの全削除
	 */
	public void deleteAll(int bno);
	
}
