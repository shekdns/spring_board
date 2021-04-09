package org.zerock.domain;


import lombok.Data;

/*
 * メンバー権限VO
 */
@Data
public class AuthVO {

  private String userid;
  private String auth;
  
}

/*
 * @param userid メンバーID
 * @param auth 権限
*/