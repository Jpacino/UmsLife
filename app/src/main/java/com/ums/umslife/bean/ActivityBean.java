package com.ums.umslife.bean;

import java.io.Serializable;
import java.util.List;

public class ActivityBean implements Serializable {

	/**
	 * code : 0
	 * data : {"allActivityList":[{"activityContent":"一起看樱花吧。","activityNo":"3","activityPlace":"请查看活动详情的相关地点。","activityTheme":"看樱花","clubName":"全体员工","clubNo":"0","createTime":1489457890000,"endTime":"2017-03-17 00:00:00","enrollment":"1","flag":0,"integral":"3","isHot":"1","joinState":"0","lng_lat":"","picURL":"pingan.jpg","signEndTime":"2017-03-19 23:00:00","signStartTime":"2017-03-19 00:00:00","sign_atta":"1","startTime":"2017-03-16 00:00:00","stopTime":"2017-03-18 00:00:00"},{"activityContent":"一起去献血","activityNo":"4","activityPlace":"请查看活动详情的相关地点。","activityTheme":"献血","clubName":"全体员工","clubNo":"0","createTime":1489458041000,"endTime":"2017-04-08 00:00:00","enrollment":"2","flag":0,"integral":"5","isHot":"1","joinState":"0","lng_lat":"","picURL":"banner1.jpg","signEndTime":"2017-03-30 00:00:00","signStartTime":"2017-03-24 00:00:00","sign_atta":"1","startTime":"2017-03-15 00:00:00","stopTime":"2017-03-24 00:00:00"},{"activityContent":"一起踏青吧。","activityNo":"2","activityPlace":"南京市玄武区玄武湖公园-北门","activityTheme":"踏青","clubName":"全体员工","clubNo":"0","createTime":1489454792000,"endTime":"2017-03-20 00:00:00","enrollment":"1","flag":0,"integral":"3","isHot":"","joinState":"0","lng_lat":"32.09277,118.799698","picURL":"pingan1.jpg","signEndTime":"2017-03-19 00:00:00","signStartTime":"2017-03-18 00:00:00","sign_atta":"","startTime":"2017-03-15 00:00:00","stopTime":"2017-03-17 00:00:00","updateTime":1489457756000},{"activityContent":"一起去春游。","activityNo":"1","activityPlace":"请查看活动详情的相关地点。","activityTheme":"春游","clubName":"全体员工","clubNo":"0","createTime":1489454698000,"endTime":"2017-03-20 00:00:00","enrollment":"0","flag":0,"integral":"3","isHot":"","joinState":"","lng_lat":"","picURL":"pingan1.jpg","signEndTime":"2017-03-19 00:00:00","signStartTime":"2017-03-18 00:00:00","sign_atta":"1","startTime":"2017-03-14 00:00:00","stopTime":"2017-03-17 00:00:00","updateTime":1489457787000}],"hotActivityList":[{"activityContent":"一起看樱花吧。","activityNo":"3","activityPlace":"请查看活动详情的相关地点。","activityTheme":"看樱花","clubName":"全体员工","clubNo":"0","createTime":1489457890000,"endTime":"2017-03-17 00:00:00","enrollment":"1","flag":0,"integral":"3","isHot":"1","joinState":"0","lng_lat":"","picURL":"pingan.jpg","signEndTime":"2017-03-19 23:00:00","signStartTime":"2017-03-19 00:00:00","sign_atta":"1","startTime":"2017-03-16 00:00:00","stopTime":"2017-03-18 00:00:00"},{"activityContent":"一起去献血","activityNo":"4","activityPlace":"请查看活动详情的相关地点。","activityTheme":"献血","clubName":"全体员工","clubNo":"0","createTime":1489458041000,"endTime":"2017-04-08 00:00:00","enrollment":"2","flag":0,"integral":"5","isHot":"1","joinState":"0","lng_lat":"","picURL":"banner1.jpg","signEndTime":"2017-03-30 00:00:00","signStartTime":"2017-03-24 00:00:00","sign_atta":"1","startTime":"2017-03-15 00:00:00","stopTime":"2017-03-24 00:00:00"},{"activityContent":"一起踏青吧。","activityNo":"2","activityPlace":"南京市玄武区玄武湖公园-北门","activityTheme":"踏青","clubName":"全体员工","clubNo":"0","createTime":1489454792000,"endTime":"2017-03-20 00:00:00","enrollment":"1","flag":0,"integral":"3","isHot":"","joinState":"0","lng_lat":"32.09277,118.799698","picURL":"pingan1.jpg","signEndTime":"2017-03-19 00:00:00","signStartTime":"2017-03-18 00:00:00","sign_atta":"","startTime":"2017-03-15 00:00:00","stopTime":"2017-03-17 00:00:00","updateTime":1489457756000}]}
	 * reason : 查询成功
	 */

	private String code= "";
	private DataBean data;
	private String reason= "";

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

	public static class DataBean implements Serializable{
		private List<AllActivityListBean> allActivityList;
		private List<HotActivityListBean> hotActivityList;

		public List<AllActivityListBean> getAllActivityList() {
			return allActivityList;
		}

		public void setAllActivityList(List<AllActivityListBean> allActivityList) {
			this.allActivityList = allActivityList;
		}

		public List<HotActivityListBean> getHotActivityList() {
			return hotActivityList;
		}

		public void setHotActivityList(List<HotActivityListBean> hotActivityList) {
			this.hotActivityList = hotActivityList;
		}



		public static class AllActivityListBean implements Serializable{
			/**
			 * activityContent : 一起看樱花吧。
			 * activityNo : 3
			 * activityPlace : 请查看活动详情的相关地点。
			 * activityTheme : 看樱花
			 * clubName : 全体员工
			 * clubNo : 0
			 * createTime : 1489457890000
			 * endTime : 2017-03-17 00:00:00
			 * enrollment : 1
			 * flag : 0
			 * integral : 3
			 * isHot : 1
			 * joinState : 0
			 * lng_lat :
			 * picURL : pingan.jpg
			 * signEndTime : 2017-03-19 23:00:00
			 * signStartTime : 2017-03-19 00:00:00
			 * sign_atta : 1
			 * startTime : 2017-03-16 00:00:00
			 * stopTime : 2017-03-18 00:00:00
			 * updateTime : 1489457756000
			 */

			private String activityContent= "";
			private String activityNo= "";
			private String activityPlace= "";
			private String activityTheme= "";
			private String clubName= "";
			private String clubNo= "";
			private long createTime = 0;
			private String endTime= "";
			private String enrollment= "";
			private int flag = 0;
			private String integral= "";
			private String isHot= "";
			private String joinState= "";
			private String lng_lat= "aaaaaaaaaaaaaaaaaa";
			private String picURL= "";
			private String signEndTime= "";
			private String signStartTime= "";
			private String sign_atta= "";
			private String startTime= "";
			private String stopTime= "";
			private String detail = "";
			private long updateTime = 0;

			public String getDetail() {
				return detail;
			}

			public void setDetail(String detail) {
				this.detail = detail;
			}

			public String getActivityContent() {
				return activityContent;
			}

			public void setActivityContent(String activityContent) {
				this.activityContent = activityContent;
			}

			public String getActivityNo() {
				return activityNo;
			}

			public void setActivityNo(String activityNo) {
				this.activityNo = activityNo;
			}

			public String getActivityPlace() {
				return activityPlace;
			}

			public void setActivityPlace(String activityPlace) {
				this.activityPlace = activityPlace;
			}

			public String getActivityTheme() {
				return activityTheme;
			}

			public void setActivityTheme(String activityTheme) {
				this.activityTheme = activityTheme;
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

			public long getCreateTime() {
				return createTime;
			}

			public void setCreateTime(long createTime) {
				this.createTime = createTime;
			}

			public String getEndTime() {
				return endTime;
			}

			public void setEndTime(String endTime) {
				this.endTime = endTime;
			}

			public String getEnrollment() {
				return enrollment;
			}

			public void setEnrollment(String enrollment) {
				this.enrollment = enrollment;
			}

			public int getFlag() {
				return flag;
			}

			public void setFlag(int flag) {
				this.flag = flag;
			}

			public String getIntegral() {
				return integral;
			}

			public void setIntegral(String integral) {
				this.integral = integral;
			}

			public String getIsHot() {
				return isHot;
			}

			public void setIsHot(String isHot) {
				this.isHot = isHot;
			}

			public String getJoinState() {
				return joinState;
			}

			public void setJoinState(String joinState) {
				this.joinState = joinState;
			}

			public String getLng_lat() {
				return lng_lat;
			}

			public void setLng_lat(String lng_lat) {
				this.lng_lat = lng_lat;
			}

			public String getPicURL() {
				return picURL;
			}

			public void setPicURL(String picURL) {
				this.picURL = picURL;
			}

			public String getSignEndTime() {
				return signEndTime;
			}

			public void setSignEndTime(String signEndTime) {
				this.signEndTime = signEndTime;
			}

			public String getSignStartTime() {
				return signStartTime;
			}

			public void setSignStartTime(String signStartTime) {
				this.signStartTime = signStartTime;
			}

			public String getSign_atta() {
				return sign_atta;
			}

			public void setSign_atta(String sign_atta) {
				this.sign_atta = sign_atta;
			}

			public String getStartTime() {
				return startTime;
			}

			public void setStartTime(String startTime) {
				this.startTime = startTime;
			}

			public String getStopTime() {
				return stopTime;
			}

			public void setStopTime(String stopTime) {
				this.stopTime = stopTime;
			}

			public long getUpdateTime() {
				return updateTime;
			}

			public void setUpdateTime(long updateTime) {
				this.updateTime = updateTime;
			}
		}

		public static class HotActivityListBean implements Serializable{
			/**
			 * activityContent : 一起看樱花吧。
			 * activityNo : 3
			 * activityPlace : 请查看活动详情的相关地点。
			 * activityTheme : 看樱花
			 * clubName : 全体员工
			 * clubNo : 0
			 * createTime : 1489457890000
			 * endTime : 2017-03-17 00:00:00
			 * enrollment : 1
			 * flag : 0
			 * integral : 3
			 * isHot : 1
			 * joinState : 0
			 * lng_lat :
			 * picURL : pingan.jpg
			 * signEndTime : 2017-03-19 23:00:00
			 * signStartTime : 2017-03-19 00:00:00
			 * sign_atta : 1
			 * startTime : 2017-03-16 00:00:00
			 * stopTime : 2017-03-18 00:00:00
			 * updateTime : 1489457756000
			 */

			private String activityContent= "";
			private String activityNo= "";
			private String activityPlace= "";
			private String activityTheme= "";
			private String clubName= "";
			private String clubNo= "";
			private long createTime = 0;
			private String endTime= "";
			private String enrollment= "";
			private int flag = 0;
			private String integral= "";
			private String isHot= "";
			private String joinState= "";
			private String lng_lat= "";
			private String picURL= "";
			private String signEndTime= "";
			private String signStartTime= "";
			private String sign_atta= "";
			private String startTime= "";
			private String stopTime= "";
			private String detail = "";
			private long updateTime = 0;

			public String getDetail() {
				return detail;
			}

			public void setDetail(String detail) {
				this.detail = detail;
			}

			public String getActivityContent() {
				return activityContent;
			}

			public void setActivityContent(String activityContent) {
				this.activityContent = activityContent;
			}

			public String getActivityNo() {
				return activityNo;
			}

			public void setActivityNo(String activityNo) {
				this.activityNo = activityNo;
			}

			public String getActivityPlace() {
				return activityPlace;
			}

			public void setActivityPlace(String activityPlace) {
				this.activityPlace = activityPlace;
			}

			public String getActivityTheme() {
				return activityTheme;
			}

			public void setActivityTheme(String activityTheme) {
				this.activityTheme = activityTheme;
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

			public long getCreateTime() {
				return createTime;
			}

			public void setCreateTime(long createTime) {
				this.createTime = createTime;
			}

			public String getEndTime() {
				return endTime;
			}

			public void setEndTime(String endTime) {
				this.endTime = endTime;
			}

			public String getEnrollment() {
				return enrollment;
			}

			public void setEnrollment(String enrollment) {
				this.enrollment = enrollment;
			}

			public int getFlag() {
				return flag;
			}

			public void setFlag(int flag) {
				this.flag = flag;
			}

			public String getIntegral() {
				return integral;
			}

			public void setIntegral(String integral) {
				this.integral = integral;
			}

			public String getIsHot() {
				return isHot;
			}

			public void setIsHot(String isHot) {
				this.isHot = isHot;
			}

			public String getJoinState() {
				return joinState;
			}

			public void setJoinState(String joinState) {
				this.joinState = joinState;
			}

			public String getLng_lat() {
				return lng_lat;
			}

			public void setLng_lat(String lng_lat) {
				this.lng_lat = lng_lat;
			}

			public String getPicURL() {
				return picURL;
			}

			public void setPicURL(String picURL) {
				this.picURL = picURL;
			}

			public String getSignEndTime() {
				return signEndTime;
			}

			public void setSignEndTime(String signEndTime) {
				this.signEndTime = signEndTime;
			}

			public String getSignStartTime() {
				return signStartTime;
			}

			public void setSignStartTime(String signStartTime) {
				this.signStartTime = signStartTime;
			}

			public String getSign_atta() {
				return sign_atta;
			}

			public void setSign_atta(String sign_atta) {
				this.sign_atta = sign_atta;
			}

			public String getStartTime() {
				return startTime;
			}

			public void setStartTime(String startTime) {
				this.startTime = startTime;
			}

			public String getStopTime() {
				return stopTime;
			}

			public void setStopTime(String stopTime) {
				this.stopTime = stopTime;
			}

			public long getUpdateTime() {
				return updateTime;
			}

			public void setUpdateTime(long updateTime) {
				this.updateTime = updateTime;
			}
		}
	}
}
