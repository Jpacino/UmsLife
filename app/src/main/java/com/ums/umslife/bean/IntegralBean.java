package com.ums.umslife.bean;

import java.util.List;

public class IntegralBean {
	private String code = "";
	private String reason = "";
	private List<IntegralsBean> data;

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

	public List<IntegralsBean> getData() {
		return data;
	}

	public void setData(List<IntegralsBean> data) {
		this.data = data;
	}

	public static class IntegralsBean {

		private String actIntegral;
		private String activityNo;
		private String activityTheme;
		private long createTime;
		private String enterTime;
		private int flag;
		private String joinState;
		private String phone;
		private String signTime;

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

		public String getActivityTheme() {
			return activityTheme;
		}

		public void setActivityTheme(String activityTheme) {
			this.activityTheme = activityTheme;
		}

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
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

		public String getSignTime() {
			return signTime;
		}

		public void setSignTime(String signTime) {
			this.signTime = signTime;
		}
	}
}
