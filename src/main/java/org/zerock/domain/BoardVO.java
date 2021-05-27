package org.zerock.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

/*
  * 掲示板 文 VO
 */
@Data
public class BoardVO {

	private int bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updateDate;
	private int replyCnt;
	
	
	private List<BoardAttachVO> attachList;
	//private String file_1;
	//private String file_2;
	//private String file_3;
	
	/*
	 * @param bno 掲示板の文番号
	 * @param title 掲示板の文のタイトル
	 * @param content 掲示板の文の内容
	 * @param writer 掲示板の文の作成者
	 * @param regDate 登録日
	 * @param updateDate  更新日
	 * @param replyCnt コメントカウント
	 * @param file_1 ファイル1
	 * @param file_2 ファイル2
	 * @param file_3 ファイル3
	 */ 
}
