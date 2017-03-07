package com.ums.umslife.bean;

import java.util.List;

public class ClubDetailBean {
	private String code = "";
	private ClubDetailsBean data;
	private String reason = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ClubDetailsBean getData() {
		return data;
	}

	public void setData(ClubDetailsBean data) {
		this.data = data;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public static class ClubDetailsBean {
		/**
		 * applyState : 1 clubName : 俱乐部（一） clubNo : 1 clubUserList :
		 * [{"applyState"
		 * :0,"flag":0,"phone":"13770625739","userName":"魏晨"},{"applyState"
		 * :0,"flag"
		 * :0,"phone":"137137","userName":"陈立俊"},{"applyState":0,"flag":
		 * 0,"phone":"137","userName":"马炼"}] member : 3 synopsis : 一起俱乐部
		 */

		private String applyState;
		private String clubName;
		private String clubNo;
		private String member;
		private String synopsis;
		private List<ClubUserListBean> clubUserList;

		public String getApplyState() {
			return applyState;
		}

		public void setApplyState(String applyState) {
			this.applyState = applyState;
		}

		public String getClubName() {
			return clubName;
		}

		public void setClubName(String clubName) {
			this.clubName = clubName;
		}

		public String getClubNo() {
			return clubNo;
		}

		public void setClubNo(String clubNo) {
			this.clubNo = clubNo;
		}

		public String getMember() {
			return member;
		}

		public void setMember(String member) {
			this.member = member;
		}

		public String getSynopsis() {
			return synopsis;
		}

		public void setSynopsis(String synopsis) {
			this.synopsis = synopsis;
		}

		public List<ClubUserListBean> getClubUserList() {
			return clubUserList;
		}

		public void setClubUserList(List<ClubUserListBean> clubUserList) {
			this.clubUserList = clubUserList;
		}

		public static class ClubUserListBean {
			/**
			 * applyState : 0 flag : 0 phone : 13770625739 userName : 魏晨
			 */

			private int applyState;
			private int flag;
			private String phone;
			private String userName;

			public int getApplyState() {
				return applyState;
			}

			public void setApplyState(int applyState) {
				this.applyState = applyState;
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

			public String getUserName() {
				return userName;
			}

			public void setUserName(String userName) {
				this.userName = userName;
			}
		}
	}
}
