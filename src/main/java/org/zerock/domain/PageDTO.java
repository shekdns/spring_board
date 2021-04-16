package org.zerock.domain;

import lombok.Data;

/*
 * ページング処理DTO
 * 
 */
@Data
public class PageDTO {

	private int startPage;  // スタートページ
	private int endPage;    // エンドページ
	private boolean prev, next; // 前のページ、次のページ
	
	private int total;      // 総掲示板の文章数
	private Criteria cri;   // Criteriaオブジェクト
	
	public PageDTO(Criteria cri, int total) {
		
		this.cri = cri;
		this.total = total;
		
		
		/* 例えば、total(全掲示文)が450個、amount(一つの画面に表示される文章の数)が10であれば、
		  *  現在pageNumが12なら（現在12ページなら）
		  *  現在のページ番号でendPage計算(ページナビゲーションに出てくるラストの番号)
		 * (1210.0) = 2) * 10 = 20 (endPage=>ページナビゲーションが11、12、13、...20まで出なければならない)
		 */
	    this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;

	    /* 開始番号（ページナビからの開始番号）
	     * 20 - 9 = 11に沿ってページナビゲーションが11から開始
	     */
	    this.startPage = this.endPage - 9;
	    
	    
	    /* 実際のページナビゲーションのラストの番号
	     * (450* 1.0 = 450.0) / 10 = 45
	         *  従って、ページナビゲーションは以下のようになる。
	     * 1,2,3..10
	     * 11,12,13..20
	     * 21,22,23..30
	     * 31,32,33..40
	     * 41、42、43、44、45 (45が最後の番号である。)
	     */
	    int realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
	    
	    
	    /* endPageをまた計算
	     * 45 < 20 = false
	         *  もし、endPageが50になると45<50=trueであるため、endPage=45になる。
	     * 41、42、43、44、45 (45が最後の番号である。)
	     */
	    if (realEnd < this.endPage) {
	      this.endPage = realEnd;
	    }
	    
	    /* 前のボタン
	     * << 11, 12, 13... 20 >> このように前の次のボタンがすべて出る。 
	     */
	    this.prev = this.startPage > 1;
	    
	    /* 次のボタン
	     * << 11, 12, 13... 20 >> このように前の次のボタンがすべて出る。 
	         * もし、endPageが50になったら、50<45=falseなので次のボタンが出てこない。
	     */
	    this.next = this.endPage < realEnd;
	}
	
	
	
}
