package org.zerock.mapper;

import org.zerock.domain.MemberVO;

/*
 * メンバー関連のmapper interface
 */
public interface MemberMapper {

	//メンバーの読み込み
	public MemberVO read(String userid);

	//メンバー登録
	public int insert(MemberVO user);
	
	//ID重複チェック
	public int checkOverId(String userid);
	
	//名前重複チェック
	public int checkOverName(String username);
	
	//Eメール重複チェック
	public int checkOverEmail(String email);
}
