package org.zerock.service;

import javax.servlet.http.HttpServletRequest;

/*
 * メール関連のSerivce interface
 */
public interface MailSendService {
	
	/*
	 * 会員登録完了時に入力したメールアドレスにメールを送信
	 */
	public void mailSendWithRegister(String e_mail, String userid, HttpServletRequest request);
	
}
