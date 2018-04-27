package com.server.revpojo;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

@Alias("EventDetail")
public class EventDetail {

	private BigDecimal eventId;
	private String eventDetail;
	private String adUserFrom;
	private String adUserTo;
	private BigDecimal adAmount;
	private String adDate;

	private BigDecimal eventFrom;
	private BigDecimal eventTo;

	public BigDecimal getEventId() {
		return eventId;
	}
	public void setEventId(BigDecimal eventId) {
		this.eventId = eventId;
	}

	public String getEventDetail() {
		return eventDetail;
	}
	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}

	public BigDecimal getAdAmount() {
		return adAmount;
	}
	public void setAdAmount(BigDecimal adAmount) {
		this.adAmount = adAmount;
	}

	public String getAdUserFrom() {
		return adUserFrom;
	}
	public void setAdUserFrom(String adUserFrom) {
		this.adUserFrom = adUserFrom;
	}
	public String getAdUserTo() {
		return adUserTo;
	}
	public void setAdUserTo(String adUserTo) {
		this.adUserTo = adUserTo;
	}
	public String getAdDate() {
		return adDate;
	}
	public void setAdDate(String adDate) {
		this.adDate = adDate;
	}
	public BigDecimal getEventFrom() {
		return eventFrom;
	}
	public void setEventFrom(BigDecimal eventFrom) {
		this.eventFrom = eventFrom;
	}
	public BigDecimal getEventTo() {
		return eventTo;
	}
	public void setEventTo(BigDecimal eventTo) {
		this.eventTo = eventTo;
	}

}