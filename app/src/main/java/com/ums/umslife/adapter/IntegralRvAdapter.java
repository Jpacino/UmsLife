package com.ums.umslife.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ums.umslife.R;
import com.ums.umslife.bean.IntegralBean;

import java.util.List;

/**
 * Created by Javen on 2017/3/22.
 */

public class IntegralRvAdapter extends BaseQuickAdapter<IntegralBean.IntegralsBean,BaseViewHolder> {
    public IntegralRvAdapter(int layoutResId, List<IntegralBean.IntegralsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntegralBean.IntegralsBean item) {
        helper.setText(R.id.tv_integral,item.getActIntegral())
                .setText(R.id.tv_details,item.getActivityTheme())
                .setText(R.id.tv_time,item.getSignTime());

    }
}
