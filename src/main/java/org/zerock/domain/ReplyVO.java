package org.zerock.domain;

import java.util.Date;

import lombok.Data;

/*
 * コメントVO
 */
@Data
public class ReplyVO {

	private int rno;
	private int bno;
	
	private String reply;
	private String replyer;
	private Date replyDate;
	private Date updateDate;
	
	
}
/*
 * @param rno コメント番号
 * @param bno 文番号
 * @param reply コメントの内容。
 * @param replyer 作成者
 * @parem replyDate 登録日
 * @param updateDate 更新日
*/

