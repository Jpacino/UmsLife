package com.ums.umslife.bean;

public class UserBean {

	private String code;
	private DataBean data;
	private String reason;

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

	public static class DataBean {
		private String address;
		private String phone;
		private int sex;
		private String userName;
		private String userNo;
		private String jobNo;
		private String userDept;
		private String userPosition;
		private String totalIntegral;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
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

	}
}
