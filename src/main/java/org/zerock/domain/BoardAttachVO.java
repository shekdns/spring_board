package org.zerock.domain;

import lombok.Data;

/*
 * 掲示板boardファイルアップロードVO
 */
@Data
public class BoardAttachVO {
	
	private String uuid;
	private String uploadPath;
	private String fileName;
	private boolean fileType;
	
	private int bno;
	
	
}

/* 
 * @param uuid ファイル固有uuid(固有番号)
 * @param uploadPath ファイル経路
 * @param fileName ファイル名
 * @param fileType イメージファイルチェック
 * @param bno   掲示板 idx
 */ 