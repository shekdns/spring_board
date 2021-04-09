package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

/*
 * メンバー関連のVOクラス 
 */
@Data
public class MemberVO {

	private String userid;
	private String userpw;
	private String userName;
	private boolean enabled;
	private String email;
	private Date regDate;
	private Date updateDate;
	private List<AuthVO> authList;
	
	/*
	 * @param userid メンバーID
	 * @param userpw メンバーパスワード
	 * @param userName メンバーの名前
	 * @param email Eメール
	 * @param regDate 登録日
	 * @param updateDate  更新日
	 * @param authList 権限リスト
	 */ 
}
