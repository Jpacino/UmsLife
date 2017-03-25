package com.ums.umslife.net;

import com.ums.umslife.bean.ActivityApplyBean;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.bean.ActivitySignBean;
import com.ums.umslife.bean.ChangePwdBean;
import com.ums.umslife.bean.ClubApplyBean;
import com.ums.umslife.bean.ClubBean;
import com.ums.umslife.bean.ClubDetailBean;
import com.ums.umslife.bean.IntegralBean;
import com.ums.umslife.bean.LoginResponseBean;
import com.ums.umslife.bean.SendMsgBean;
import com.ums.umslife.bean.UserBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface HttpService {
    /**
     * 用户名密码登录
     */
    @POST("/control/user/userLogin.do")
    Call<LoginResponseBean> getLoginResponseBean(@Query("phone") String phone,
                                                 @Query("user_pwd") String user_pwd);

    /**
     * 短信验证登录
     */
    @POST("/control/user/shortMessageLogin.do")
    Call<LoginResponseBean> getLoginResponseBean2(@Query("phone") String phone,
                                                  @Query("verCode") String verCode);

    /**
     * 用户信息查询
     */
    @POST("control/user/queryUserInfo.do")
    Call<UserBean> getUserBean(@Query("phone") String phone);

    /**
     * 活动详情查询
     */
    @POST("control/act/activityDetail.do")
    Call<ActivityBean> getActivityBean(@Query("phone") String phone,
                                       @Query("param") String param);

    /**
     * 报名和报名取消
     */
    @POST("control/act/activityApplicationCancel.do")
    Call<ActivityApplyBean> getActivityApplyBean(@Query("phone") String phone,
                                                 @Query("activityNo") String activityNo,
                                                 @Query("joinState") String joinState);

    /**
     * 签到
     */
    @POST("control/act/activitySign.do")
    Call<ActivitySignBean> getActivitySignBean(@Query("phone") String phone,
                                               @Query("activityNo") String activityNo,
                                               @Query("actIntegral") String actIntegral);

    /**
     * 俱乐部查询
     */
    @POST("control/club/queryClubInfo.do")
    Call<ClubBean> getClubBean(@Query("phone") String phone,
                               @Query("param") String param);

    /**
     * 俱乐部详情查询
     */
    @POST("control/club/queryClubDetail.do")
    Call<ClubDetailBean> getClubDetailBean(@Query("phone") String phone,
                                           @Query("clubNo") String clubNo);

    /**
     * 俱乐部申请加入、退出接口
     */
    @POST("control/club/clubApplyCancel.do")
    Call<ClubApplyBean> getClubApplyBean(@Query("phone") String phone,
                                         @Query("clubNo") String clubNo,
                                         @Query("applyState") String applyState);

    /**
     * 短信验证码接口
     */
    @POST("control/sends/sendPhoneSeccode.do")
    Call<SendMsgBean> getSendMsgBean(@Query("phone") String phone);

    /**
     * 修改密码
     */
    @POST("control/user/modifyPwd.do")
    Call<ChangePwdBean> getChangePwdBean(@Query("phone") String phone,
                                         @Query("verCode") String verCode, @Query("user_pwd") String user_pwd);

    /**
     * 查询积分明细
     */
    @POST("control/act/queryIntegral.do")
    Call<IntegralBean> getIntegralBean(@Query("phone") String phone);

    /**
     *  图片上传
     */
    @Multipart
    @POST("control/member/headPic/save.do")
    Call<ActivitySignBean> upLoadPic(@Query("phone") String phone,
                           @Query("activityNo") String activityNo,
                           @PartMap Map<String, RequestBody> params);
}
