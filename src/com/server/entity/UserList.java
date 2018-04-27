package com.server.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("UserList")
public class UserList {

	private List<UserMst> userList;

	public List<UserMst> getUserList() {
		return userList;
	}

	public void setUserList(List<UserMst> userList) {
		this.userList = userList;
	}

}