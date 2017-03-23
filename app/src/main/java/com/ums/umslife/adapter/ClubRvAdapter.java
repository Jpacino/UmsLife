package com.ums.umslife.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ums.umslife.R;
import com.ums.umslife.bean.ClubBean;

import java.util.List;

/**
 * Created by Javen on 2017/3/22.
 */

public class ClubRvAdapter extends BaseQuickAdapter<ClubBean.ClubsBean,BaseViewHolder> {

    private final static String IS_JOIN = "1";
    private final static String CHECKING = "2";
    private final static String CHECK_NOT_PASS = "3";
    public ClubRvAdapter(int layoutResId, List<ClubBean.ClubsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClubBean.ClubsBean item) {
        helper.setText(R.id.tv_club_name,item.getClubName())
                .setText(R.id.tv_synopsis,item.getSynopsis())
                .setText(R.id.tv_member,item.getMember());
        TextView stateTv = helper.getView(R.id.tv_joinState);
        if (item.getApplyState().isEmpty()) {
            stateTv.setText("未加入");
            stateTv.setTextColor(mContext.getResources().getColor(R.color.red_txt));
        }else if (item.getApplyState().equals(IS_JOIN)) {
            stateTv.setText("已加入");
            stateTv.setTextColor(mContext.getResources().getColor(R.color.green_txt));
        }else {
            if (item.getApplyState().equals(CHECKING)) {
                stateTv.setText("审核中");
                stateTv.setTextColor(mContext.getResources().getColor(R.color.green_txt));
            } else if (item.getApplyState().equals(CHECK_NOT_PASS)) {
                stateTv.setText("未通过");
                stateTv.setTextColor(mContext.getResources().getColor(R.color.red_txt));
            }
        }
    }
}
