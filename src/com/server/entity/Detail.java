package com.server.entity;

import org.apache.ibatis.type.Alias;

@Alias("Detail")
public class Detail {

	private String name;
	private String done;
	private String amount;
	private String adDate;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDone() {
		return done;
	}
	public void setDone(String done) {
		this.done = done;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAdDate() {
		return adDate;
	}
	public void setAdDate(String adDate) {
		this.adDate = adDate;
	}

}