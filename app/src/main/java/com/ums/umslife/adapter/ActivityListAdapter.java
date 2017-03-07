package com.ums.umslife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.utils.CommonViewHolder;

import java.util.List;

public class ActivityListAdapter extends BaseAdapter {
	private final static String IS_SIGN = "1";
	private final static String NOT_SIGN = "0";
	private List<ActivityBean.ActivitysBean> activityLists;
	private Context mContext;

	public ActivityListAdapter(List<ActivityBean.ActivitysBean> activityLists,
			Context mContext) {
		super();
		this.activityLists = activityLists;
		this.mContext = mContext;

	}

	@Override
	public int getCount() {
		return activityLists == null ? 0 : activityLists.size();
		// return 20;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView,
				parent, R.layout.item_activity_list);
		TextView contentTv = viewHolder.getTv(R.id.tv_activity_name);
		TextView clubTv = viewHolder.getTv(R.id.tv_activity_club);
		TextView countTv = viewHolder.getTv(R.id.tv_activity_count);
		TextView timeTv = viewHolder.getTv(R.id.tv_activity_time);
		TextView joinStateTv = viewHolder.getTv(R.id.tv_activity_joinState);
		View lineV = viewHolder.getView(R.id.view_line);
		if (position==activityLists.size()-1) {
			lineV.setVisibility(View.VISIBLE);
		}else {
			lineV.setVisibility(View.GONE);
		}
		contentTv.setText(activityLists.get(position).getActivityTheme());
		if (activityLists.get(position).getClubNo().equals("0")) {
			clubTv.setText("全体成员");
		} else {

			clubTv.setText(activityLists.get(position).getClubName());
		}
		countTv.setText(activityLists.get(position).getIntegral() + "分");
		timeTv.setText("截止" + activityLists.get(position).getStopTime());
		if (activityLists.get(position).getJoinState().equals(IS_SIGN)) {
			joinStateTv.setText("已签到");
			joinStateTv.setTextColor(mContext.getResources().getColor(
					R.color.green_txt));
		} else if (activityLists.get(position).getJoinState().equals(NOT_SIGN)) {
			joinStateTv.setText("已报名");
			joinStateTv.setTextColor(mContext.getResources().getColor(
					R.color.green_txt));
		} else if (activityLists.get(position).getJoinState().isEmpty()) {
			joinStateTv.setText("");
		}
		return viewHolder.convertView;
	}

}
