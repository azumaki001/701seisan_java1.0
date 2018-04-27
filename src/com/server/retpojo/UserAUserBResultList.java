package com.server.retpojo;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.server.entity.UserAUserB_RESULT;

@Alias("UserAUserB_RESULT_LIST")
public class UserAUserBResultList {

	private List<UserAUserB_RESULT> detailListAB;
	private List<UserAUserB_RESULT> detailListBA;

	public List<UserAUserB_RESULT> getDetailListBA() {
		return detailListBA;
	}

	public void setDetailListBA(List<UserAUserB_RESULT> detailListBA) {
		this.detailListBA = detailListBA;
	}

	public List<UserAUserB_RESULT> getDetailListAB() {
		return detailListAB;
	}

	public void setDetailListAB(List<UserAUserB_RESULT> detailListAB) {
		this.detailListAB = detailListAB;
	}
}