package com.server.retpojo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.server.revpojo.EventDetail;

@Alias("EventList")
public class EventList {

	private List<EventDetail> adEvents;

	public List<EventDetail> getAdEvents() {
		return adEvents;
	}

	public void setAdEvents(List<EventDetail> adEvents) {
		this.adEvents = adEvents;
	}

}