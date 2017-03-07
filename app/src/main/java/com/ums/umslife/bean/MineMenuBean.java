package com.ums.umslife.bean;

public class MineMenuBean {
	private int res;
	private String name;
	public MineMenuBean(int res, String name) {
		super();
		this.res = res;
		this.name = name;
	}
	public MineMenuBean() {
		super();
	}
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
