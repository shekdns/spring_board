package org.zerock.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.log4j.Log4j;

/*
 * 権限等級に応じたページ移動クラス
 */
@Log4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {

		log.warn("Login Success");
		
		//Array Listを通じて現在ログインした権限情報を取得
		List<String> roleNames = new ArrayList<>();

		auth.getAuthorities().forEach(authority -> {

			roleNames.add(authority.getAuthority());

		});

		log.warn("ROLE NAMES: " + roleNames);
		
		//管理者なら、listページに移動
		if (roleNames.contains("ROLE_ADMIN")) {

			response.sendRedirect("/board/list");
			return;
		}
		
		//ユーザーなら、listページに移動
		if (roleNames.contains("ROLE_MEMBER")) {

			response.sendRedirect("/board/list");
			return;
		}

		response.sendRedirect("/");
	}
}
