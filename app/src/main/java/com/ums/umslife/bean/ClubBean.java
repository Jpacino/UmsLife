package com.ums.umslife.bean;

import java.io.Serializable;
import java.util.List;

public class ClubBean implements Serializable {

	private String code = "";
	private String reason = "";
	private List<ClubsBean> data;

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

	public List<ClubsBean> getData() {
		return data;
	}

	public void setData(List<ClubsBean> data) {
		this.data = data;
	}

	public static class ClubsBean implements Serializable {
		private String clubNo = "";
		private String clubName = "";
		private String synopsis = "";
		private String member = "";
		private String applyState = "";

		public String getClubNo() {
			return clubNo;
		}

		public void setClubNo(String clubNo) {
			this.clubNo = clubNo;
		}

		public String getClubName() {
			return clubName;
		}

		public void setClubName(String clubName) {
			this.clubName = clubName;
		}

		public String getSynopsis() {
			return synopsis;
		}

		public void setSynopsis(String synopsis) {
			this.synopsis = synopsis;
		}

		public String getMember() {
			return member;
		}

		public void setMember(String member) {
			this.member = member;
		}

		public String getApplyState() {
			return applyState;
		}

		public void setApplyState(String applyState) {
			this.applyState = applyState;
		}

	}
}
