package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private BoardAttachMapper attachMapper;
	
	/*
	 * 1. 掲示板の文章登録 
	 *Boardオブジェクトを受け取って値をinsertさせる。 
	 */
	@Transactional
	@Override
	public void register(BoardVO board) {
		// TODO Auto-generated method stub
		
		log.info("register......" + board);
		
		mapper.insertSelectKey(board);
		
		//boardのattatchListの値がない場合はreturn
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		
		//foreachを回りながらDBに値を入れる。
		board.getAttachList().forEach(attach -> {
			
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
		
	
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
	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		// TODO Auto-generated method stub
		
		
		log.info("modify......" + board);
		/*
		 * 修正方式では、既存のファイルをすべて削除した後、再uploadする方式で駆動される。
		 * 
		 */
		attachMapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board) == 1;
		
		//修正されるboardVO値とファイルアップロードの値がある場合は、foreach文を通じてBnoidx値をattachListに入れる。
		
		if(modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0) {
			
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
			
		}
		
		// SQLを成功させると、<update tagでupdateされた数をリターン。 従って、mapper.update(board)の値は1になる。 
		// return trueになる
		return modifyResult;
		
	}
	
	/*
	 * 4. 掲示板の文を削除
	 */
	@Transactional
	@Override
	public boolean remove(int bno) {
		// TODO Auto-generated method stub
		
		log.info("remove......" + bno);
		
		// SQLを成功させると、<delete tagでdeleteされた数をリターン。 従って、mapper.delete(board)の値は1になる。 
		// return trueになる
		attachMapper.deleteAll(bno);
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

	@Override
	public List<BoardAttachVO> getAttachList(int bno) {
		// TODO Auto-generated method stub
		return attachMapper.findByBno(bno);
	}

}
