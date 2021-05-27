package org.zerock.domain;

import lombok.Data;

/*
 * Jsonファイル情報DTO
 */
@Data
public class AttachFileDTO {
	
	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;
}

/*
 * @param fileName ファイル名
 * @param uploadPath ファイル経路
 * @param uuid ファイル固有uuid(固有番号)
 * @param image イメージファイルチェック
 */ 