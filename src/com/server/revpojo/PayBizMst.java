package com.server.revpojo;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

@Alias("PayBizMst")
public class PayBizMst {

	private BigDecimal payId;
	private String payer;
	private String payForusers;
	private String payContent;
	private BigDecimal payAmount;
	private String registDate;

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