package com.ums.umslife.bean;

import java.io.Serializable;

public class ActivityApplyBean implements Serializable {

	private String code = "";
	private String reason = "";
	private ApplysBean data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ApplysBean getData() {
		return data;
	}

	public void setData(ApplysBean data) {
		this.data = data;
	}

	public static class ApplysBean implements Serializable {

		private String actIntegral = "";
		private String activityNo = "";
		private String createTime = "";
		private String enrollment = "";
		private String enterTime = "";
		private int flag;
		private String joinState = "";
		private String phone = "";

		public String getActIntegral() {
			return actIntegral;
		}

		public void setActIntegral(String actIntegral) {
			this.actIntegral = actIntegral;
		}

		public String getActivityNo() {
			return activityNo;
		}

		public void setActivityNo(String activityNo) {
			this.activityNo = activityNo;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getEnrollment() {
			return enrollment;
		}

		public void setEnrollment(String enrollment) {
			this.enrollment = enrollment;
		}

		public String getEnterTime() {
			return enterTime;
		}

		public void setEnterTime(String enterTime) {
			this.enterTime = enterTime;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public String getJoinState() {
			return joinState;
		}

		public void setJoinState(String joinState) {
			this.joinState = joinState;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

	}
}
