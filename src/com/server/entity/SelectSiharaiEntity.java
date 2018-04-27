package com.server.entity;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

@Alias("SelectSiharaiEntity")
public class SelectSiharaiEntity {

	private BigDecimal payId;
	private BigDecimal payAmount;
	private String payer;
	private String payForusers;
	private String payContent;
	private String registDate;

	private String adFrom;
	private String adDone;
	private String adAmount;
	private String adDate;

	public String getAdFrom() {
		return adFrom;
	}

	public void setAdFrom(String adFrom) {
		this.adFrom = adFrom;
	}

	public String getAdDone() {
		return adDone;
	}

	public void setAdDone(String adDone) {
		this.adDone = adDone;
	}

	public String getAdAmount() {
		return adAmount;
	}

	public void setAdAmount(String adAmount) {
		this.adAmount = adAmount;
	}

	public String getAdDate() {
		return adDate;
	}

	public void setAdDate(String adDate) {
		this.adDate = adDate;
	}

	public BigDecimal getPayId() {
		return payId;
	}

	public void setPayId(BigDecimal payId) {
		this.payId = payId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getPayForusers() {
		return payForusers;
	}

	public void setPayForusers(String payForusers) {
		this.payForusers = payForusers;
	}

	public String getPayContent() {
		return payContent;
	}

	public void setPayContent(String payContent) {
		this.payContent = payContent;
	}

	public String getRegistDate() {
		return registDate;
	}

	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}

}