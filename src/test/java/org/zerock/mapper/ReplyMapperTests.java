package org.zerock.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyVO;

import lombok.extern.log4j.Log4j;

/*
 *コメントテストクラス
 */
@RunWith(SpringRunner.class)
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" , 
	"file:src/main/webapp/WEB-INF/spring/security-context.xml" })
@Log4j
public class ReplyMapperTests {

	
	private int[] bnoArr = { 81,153,3,4,5,6,7,8,9,10 };

	@Autowired
	private ReplyMapper mapper;

	@Test
	public void testCreate() {

		IntStream.rangeClosed(1, 10).forEach(i -> {

			ReplyVO vo = new ReplyVO();

		
			vo.setBno(bnoArr[1]);
			vo.setReply("댓글 테스트 " + i);
			vo.setReplyer("replyer" + i);

			mapper.insert(vo);
		});

	}

	@Test
	public void testRead() {

		int targetRno = 11;

		ReplyVO vo = mapper.read(targetRno);

		log.info(vo);

	}

	@Test
	public void testMapper() {

		log.info("mapper====" + mapper);
	}

	@Test
	public void testDelete() {

		int targetRno = 11;
		mapper.delete(targetRno);
	}

	@Test
	public void testUpdate() {

		int targetRno = 11;

		ReplyVO vo = mapper.read(targetRno);

		vo.setReply("Update Reply ");

		int count = mapper.update(vo);

		log.info("UPDATE COUNT: " + count);
	}

	@Test
	public void testGetList() {

		Criteria cri = new Criteria();

		List<ReplyVO> replies = mapper.getList(cri, bnoArr[0]);

		replies.forEach(reply -> log.info(reply));

	}
	
	@Test
	public void testGetListWithPaging() {
		
		// 1page 10個 出力
		Criteria cri = new Criteria(1, 10);

		List<ReplyVO> replies = mapper.getListWithPaging(cri,81);

		replies.forEach(reply -> log.info(reply));

	}

}
