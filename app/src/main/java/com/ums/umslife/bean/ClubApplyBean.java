package com.ums.umslife.bean;

import java.util.List;

public class ClubApplyBean {
    private String code;
    private ClubApplysBean data;
    private String reason;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ClubApplysBean getData() {
        return data;
    }

    public void setData(ClubApplysBean data) {
        this.data = data;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class ClubApplysBean {

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
