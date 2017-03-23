package com.ums.umslife.bean;

public class MineMenuBean {
	private int res;
	private String name;
	private int imgId;

    public MineMenuBean(int res, String name, int imgId) {
        this.res = res;
        this.name = name;
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
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
