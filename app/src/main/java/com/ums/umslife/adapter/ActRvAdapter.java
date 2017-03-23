package com.ums.umslife.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ums.umslife.R;
import com.ums.umslife.bean.ActivityBean;

import java.util.List;

/**
 * Created by Javen on 2017/3/20.
 */

public class ActRvAdapter extends BaseQuickAdapter<ActivityBean.DataBean.AllActivityListBean, BaseViewHolder> {
    private final static String IS_SIGN = "1";
    private final static String NOT_SIGN = "0";

    public ActRvAdapter(int layoutResId, List<ActivityBean.DataBean.AllActivityListBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityBean.DataBean.AllActivityListBean item) {
        helper.setText(R.id.tv_act_name, item.getActivityTheme())
        .setText(R.id.tv_integral,item.getIntegral()+"分");
        TextView clubTv = helper.getView(R.id.tv_club_name);
        TextView joinStateTv = helper.getView(R.id.tv_joinState);
        if (item.getClubNo().equals("0")) {
            clubTv.setText("全体成员");
        } else {
            clubTv.setText(item.getClubName());
        }
        if (item.getJoinState().equals(IS_SIGN)) {
            joinStateTv.setText("已签到");
            joinStateTv.setTextColor(mContext.getResources().getColor(
                    R.color.green_txt));
        } else if (item.getJoinState().equals(NOT_SIGN)) {
            joinStateTv.setText("已报名");
            joinStateTv.setTextColor(mContext.getResources().getColor(
                    R.color.green_txt));
        } else if (item.getJoinState().isEmpty()) {
            joinStateTv.setText("");
        }
    }
}