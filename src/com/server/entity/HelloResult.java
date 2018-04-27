package com.server.entity;

import org.apache.ibatis.type.Alias;

@Alias("HelloResult")
public class HelloResult {

	public String resultCode;
	public String errorMessage;
	public String processingId;

	public String mebr_no ="";
	public String cust_id ="";
	public String ufpr_mebr_no;
	public String able_i_nam;
	public String flg;
	public String nb;

}