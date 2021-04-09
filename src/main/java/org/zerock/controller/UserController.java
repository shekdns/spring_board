package org.zerock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.zerock.domain.MemberVO;
import org.zerock.service.UserService;

import com.google.gson.Gson;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
public class UserController {

	@Autowired
	private UserService service;
	
	/*
	 * 会員登録ページメソッド
	 */
	@GetMapping("/sign_in")
	public String sign_in() {
		return "board/sign_in";

	}
	
	/*
	 * 1. 会員登録メソッド
	 * 入力した情報をオブジェクトで受け取り登録メソッドを実行する
	 * @return ログインページに移動
	 */
	@PostMapping("/sign_in")
	public String sign_in(MemberVO user) {

		log.info("user_register: " + user);
		service.user_register(user);
		
		return "redirect:/board/sign_up";
	}
	
	/*
	 * 2. 会員登録id重複チェックコントローラ
	 * @param 入力したID情報
	 * @return テーブルの結果値をGsonにStringに変換
	 * ajaxで入力した情報とテーブルのid情報を比較する。 そして、結果値をgsonを通じて再び返却して検査する。
	 */
	@RequestMapping(value = "sign_in/idCheck", method = RequestMethod.GET)
	@ResponseBody
	public String idCheck(@RequestParam("id") String id) {

		int count = service.userIdCheck(id);
		Gson gson = new Gson();

		return gson.toJson(count);

	}
	
	/*
	 * 3. 会員登録名重複チェックコントローラー
	 * @param 入力した名情報
	 * @return テーブルの結果値をGsonにStringに変換
	 * ajaxで入力した情報とテーブルの名情報を比較する。 そして、結果値をgsonを通じて再び返却して検査する。
	 */
	@RequestMapping(value = "sign_in/nameCheck", method = RequestMethod.GET)
	@ResponseBody
	public String nameCheck(@RequestParam("name") String name) {

		int count = service.userNameCheck(name);
		Gson gson = new Gson();

		return gson.toJson(count);
			
	}
		
	/*
	 * 4. 会員登録Eメール重複チェックコントローラー
	 * @param 入力したEメール情報
	 * @return テーブルの結果値をGsonにStringに変換
	 * ajaxで入力した情報とテーブルのEメール情報を比較する。 そして、結果値をgsonを通じて再び返却して検査する。
	 */
	@RequestMapping(value = "sign_in/emailCheck", method = RequestMethod.GET)
	@ResponseBody
	public String eamilCheck(@RequestParam("email") String email) {

		int count = service.userEmailCheck(email);
		Gson gson = new Gson();
																	
		return gson.toJson(count);
			
	}
		
	
}
