package org.zerock.service;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.zerock.domain.MemberVO;
import org.zerock.mapper.MemberMapper;

@Service
public class MailSendServiceImpl implements MailSendService{

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SqlSessionTemplate userSqlSession;
	
	@Autowired
	private MemberMapper mapper;
	
	/*
	 * 1. 会員登録完了時に入力したメールアドレスにメールを送信
	  *  会員VO memberオブジェクトに媒介変数として受け取ったid値を入れて情報を得る。
	  * そのユーザーの名前を持ってくること
	 * MimeMessageクラスでメールを送信する内容を設定
	 */
	@Override
	public void mailSendWithRegister(String e_mail, String userid, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		mapper = userSqlSession.getMapper(MemberMapper.class);
		
		MemberVO member = mapper.read(userid);
		String name = member.getUserid();
		
		MimeMessage mail = mailSender.createMimeMessage();
		
		String htmlStr = "<h2>안녕하세요 '"+ name +"' 님</h2><br><br>" 
				+ "<p>회원가입을 진심으로 축하드립니다~.</p>"
				+ "<h3><a href='http://localhost:8080/board/sign_up'>게시판 홈페이지 접속 ^0^</a></h3><br><br>"
				+ "(혹시 잘못 전달된 메일이라면 이 이메일을 무시하셔도 됩니다)";
		try {
			mail.setSubject("회원가입을 축하드립니다~", "utf-8");
			mail.setText(htmlStr, "utf-8", "html");
			mail.addRecipient(RecipientType.TO, new InternetAddress(e_mail));
			mailSender.send(mail);
		} catch (MessagingException e) { 
			e.printStackTrace();
		}
		

		
		
	}

}
