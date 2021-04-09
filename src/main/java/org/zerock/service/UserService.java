package org.zerock.service;

import org.zerock.domain.MemberVO;

/*
 * メンバー関連のSerivce interface
 */
public interface UserService {
	
	//メンバーの読み込み
	public int user_register(MemberVO user);
	
	//メンバー登録
	public MemberVO read(String userid);

	//ID重複チェック
	public int userIdCheck(String userid);
	
	//名前重複チェック
	public int userNameCheck(String username);
	
	//Eメール重複チェック
	public int userEmailCheck(String email);
	
	
}
