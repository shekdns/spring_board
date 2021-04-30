package org.zerock.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.MemberVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

/*
 *ユーザーマッパーテストクラス
 */
@RunWith(SpringRunner.class)
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" , 
	"file:src/main/webapp/WEB-INF/spring/security-context.xml" })
@Log4j
public class MemberMapperTests {

	@Setter(onMethod_ = @Autowired)
	private MemberMapper mapper;

	@Autowired
	private PasswordEncoder pwencoder;
	
	@Test
	public void testRead() {

		MemberVO vo = mapper.read("admin0");

		log.info(vo);
		
		vo.getAuthList().forEach(authVO -> log.info(authVO));

	}
	
	@Test
	public void testInsert() {

			MemberVO user = new MemberVO();

			user.setUserid("admin3");
			user.setUserpw(pwencoder.encode("pw3"));
			user.setUserName("테스트3");
			user.setEmail("test@naver.com");
			
			mapper.insert(user);
	
	}

}
