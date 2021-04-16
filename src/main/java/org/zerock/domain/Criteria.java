package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

/*
 * ページング処理クラス
 * 
 */
@Data
public class Criteria {
	
	private int pageNum;  // ページナンバー
	private int amount;   // ページ数
 	 
	private String type;  // 検索タイプ
	private String keyword; // 検索キーワード
	
	//基本開始ページは、1    , ページの数は10
	public Criteria() {
		this(1,10);
	}
	
	/*
	 * ユーザ指定作成者
	 */
	public Criteria(int pageNum, int amount) {
		
		this.pageNum = pageNum;
		this.amount = amount;
		
	}
	
	/*
	 * 検索条件配列処理GetTypeArrメソッドを利用して動的タグを活用
	 */
	public String[] getTypeArr() {
		
		// 検索タイプがnullでリターンされた場合、""で検索結果リターン
		return type == null? new String[] {}: type.split("");
	}
	
	/*
	 * UriComponents Builderを通じて複数のパラメータを接続してURL形式にしてから転送
	 * query Paramメソッドでパラメータを追加
	 */
	public String getListLink() {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.getPageNum())
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
		
	}
	
	
}
