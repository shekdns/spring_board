package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper mapper;
	
	
	/*
	 * 1. 掲示板の文章登録 
	 *Boardオブジェクトを受け取って値をinsertさせる。 
	 */
	@Override
	public void register(BoardVO board) {
		// TODO Auto-generated method stub
		
		log.info("register......" + board);
		
		mapper.insertSelectKey(board);
	
	}
	
	/*
	 * 2. 掲示板の文章を読む
	 * bno変数で文章を詳細に読み込む
	 */
	@Override
	public BoardVO get(int bno) {
		// TODO Auto-generated method stub
		
		log.info("get......" + bno);
		return mapper.read(bno);
	}
	
	/*
	 * 3. 掲示板の文の修正 
	 */
	@Override
	public boolean modify(BoardVO board) {
		// TODO Auto-generated method stub
		
		
		log.info("modify......" + board);
		
		// SQLを成功させると、<update tagでupdateされた数をリターン。 従って、mapper.update(board)の値は1になる。 
		// return trueになる
		return mapper.update(board) == 1;
		
	}
	
	/*
	 * 4. 掲示板の文を削除
	 */
	@Override
	public boolean remove(int bno) {
		// TODO Auto-generated method stub
		
		log.info("remove......" + bno);
		
		// SQLを成功させると、<delete tagでdeleteされた数をリターン。 従って、mapper.delete(board)の値は1になる。 
		// return trueになる
		return mapper.delete(bno) == 1;
	}

	/*
	 *  5. 掲示板のリスト
	 */
	@Override
	public List<BoardVO> getList() {
		// TODO Auto-generated method stub
		
		log.info("getList..........");
		return mapper.getList();
	}
	
	/*
	 *  6. 掲示板のリストページング
	 *  CriteriaオブジェクトのpageNumとamountでページング実行
	 */
	@Override
	public List<BoardVO> getListWithPaging(Criteria cri) {
		// TODO Auto-generated method stub
		
		log.info("get List with paging.......... " + cri);
		return mapper.getListWithPaging(cri);
	}
	
	/*
	 * 7. 掲示板の文のtotalの数 
	 */
	@Override
	public int getTotal(Criteria cri) {
		// TODO Auto-generated method stub
		log.info("get total count...... " + cri);
		
		return mapper.getTotalCount(cri);
	}

}
