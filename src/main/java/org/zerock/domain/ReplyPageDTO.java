package org.zerock.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * コメントページDTO
 */
@Data
@AllArgsConstructor
public class ReplyPageDTO {
	
	private int replyCnt;
	private List<ReplyVO> list;
}

/*
 * @param replyCnt コメント数。
 * @param list コメントVO。
 */
