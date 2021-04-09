package org.zerock.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.domain.MemberVO;
import org.zerock.mapper.MemberMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private MemberMapper mapper;
	
	@Autowired
	private SqlSessionTemplate userSqlSession;
	
	@Autowired
	private PasswordEncoder pwencoder;
	
	/*
	 * メンバー登録
	 * メンバーの客債を受け、メンバーを登録する。 パスワードはencoder を使用して暗号化される。
	 */
	@Override
	public int user_register(MemberVO user) {
		// TODO Auto-generated method stub
		log.info("register......." + user);
		
		user.setUserpw(pwencoder.encode(user.getUserpw()));
		return mapper.insert(user);
	}
	
	/*
	 * メンバーの読み込み
	 * メンバーIDをもらって、 そのメンバーの情報を持ってくる。
	 */
	@Override
	public MemberVO read(String userid) {
		// TODO Auto-generated method stub
		
		log.info("userid..............." + userid);
		
		return mapper.read(userid);
	}
	
	/*
	 * ID重複チェック
	 * ID入力ウィンドウの情報とテーブルの情報を比較
	 */
	@Override
	public int userIdCheck(String userid) {
		// TODO Auto-generated method stub
		mapper = userSqlSession.getMapper(MemberMapper.class);
		
		return mapper.checkOverId(userid);
	}

	/*
	 * 重複チェック
	 * 名前プレートウィンドウの情報とテーブルの情報を比較
	 */
	@Override
	public int userNameCheck(String username) {
		// TODO Auto-generated method stub
		mapper = userSqlSession.getMapper(MemberMapper.class);
		
		return mapper.checkOverName(username);
	}
	
	/*
	 *Eメール重複チェック 
	 *Eメール入力ウィンドウの情報とテーブルの情報を比較
	 */
	@Override
	public int userEmailCheck(String email) {
		// TODO Auto-generated method stub
		mapper = userSqlSession.getMapper(MemberMapper.class);
		
		return mapper.checkOverEmail(email);
	}

}
