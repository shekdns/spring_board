package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

/*
 * 掲示板コントローラー
 */
@Controller
@Log4j
@RequestMapping("/board/*")
public class BoardController {
	
	/*
	 * 掲示板リストページ
	 */
	@GetMapping("/list")
	public void list() {
		
	}
}