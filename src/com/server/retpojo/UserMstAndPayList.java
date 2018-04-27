package com.server.retpojo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.server.entity.UserMst;

@Alias("UserMstAndPayList")
public class UserMstAndPayList {

	private List<UserMst> userMst;
	private List<PayList> payList;
	private List<PayList> miPayList;

	public List<UserMst> getUserMst() {
		return userMst;
	}
	public void setUserMst(List<UserMst> userMst) {
		this.userMst = userMst;
	}
	public List<PayList> getPayList() {
		return payList;
	}
	public void setPayList(List<PayList> payList) {
		this.payList = payList;
	}
	public List<PayList> getMiPayList() {
		return miPayList;
	}
	public void setMiPayList(List<PayList> miPayList) {
		this.miPayList = miPayList;
	}

}