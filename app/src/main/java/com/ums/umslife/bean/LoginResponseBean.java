package com.ums.umslife.bean;

import java.io.Serializable;

public class LoginResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code = "";
	private DataBean data;
	private String reason = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public static class DataBean implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String address = "";
		private String createTime = "";
		private int flag;
		private String phone = "";
		private int regState;
		private int sex;
		private String userName = "";
		private String user_name = "";
		private String userNo = "";
		private String jobNo = "";
		private String userDept = "";
		private String userPosition = "";
		private String totalIntegral = "";
		private String updateTime = "";

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public int getRegState() {
			return regState;
		}

		public void setRegState(int regState) {
			this.regState = regState;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUserNo() {
			return userNo;
		}

		public void setUserNo(String userNo) {
			this.userNo = userNo;
		}

		public String getJobNo() {
			return jobNo;
		}

		public void setJobNo(String jobNo) {
			this.jobNo = jobNo;
		}

		public String getUserDept() {
			return userDept;
		}

		public void setUserDept(String userDept) {
			this.userDept = userDept;
		}

		public String getUserPosition() {
			return userPosition;
		}

		public void setUserPosition(String userPosition) {
			this.userPosition = userPosition;
		}

		public String getTotalIntegral() {
			return totalIntegral;
		}

		public void setTotalIntegral(String totalIntegral) {
			this.totalIntegral = totalIntegral;
		}

		public String getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(String updateTime) {
			this.updateTime = updateTime;
		}

	}
}
