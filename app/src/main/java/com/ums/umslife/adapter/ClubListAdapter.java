package com.ums.umslife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.bean.ClubBean;
import com.ums.umslife.utils.CommonViewHolder;

import java.util.List;

public class ClubListAdapter extends BaseAdapter {
	private List<ClubBean.ClubsBean> clubItemLists;
	private Context mContext;
	private final static String IS_JOIN = "1";
	private final static String CHECKING = "2";
	private final static String CHECK_NOT_PASS = "3";
	
	public ClubListAdapter(List<ClubBean.ClubsBean> clubItemLists, Context mContext) {
		super();
		this.clubItemLists = clubItemLists;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		 return clubItemLists==null?0:clubItemLists.size();
//		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView,
				parent, R.layout.item_club_list);
		TextView clubNameTv = viewHolder.getTv(R.id.club_name_tv);
		TextView synopsisTv = viewHolder.getTv(R.id.synopsis_tv);
		TextView memberTv = viewHolder.getTv(R.id.member_tv);
		TextView stateTv = viewHolder.getTv(R.id.apply_state_tv);
		View lineV = viewHolder.getView(R.id.view_line);
		if (position==clubItemLists.size()-1) {
			lineV.setVisibility(View.VISIBLE);
		}else {
			lineV.setVisibility(View.GONE);
		}
		clubNameTv.setText(clubItemLists.get(position).getClubName());
		synopsisTv.setText(clubItemLists.get(position).getSynopsis());
		memberTv.setText(clubItemLists.get(position).getMember());
		if (clubItemLists.get(position).getApplyState().isEmpty()) {
			stateTv.setText("未加入");
			stateTv.setTextColor(mContext.getResources().getColor(R.color.red_txt));
		}else if (clubItemLists.get(position).getApplyState().equals(IS_JOIN)) {
			stateTv.setText("已加入");
			stateTv.setTextColor(mContext.getResources().getColor(R.color.green_txt));
		}else {
			if (clubItemLists.get(position).getApplyState().equals(CHECKING)) {
				stateTv.setText("审核中");
				stateTv.setTextColor(mContext.getResources().getColor(R.color.green_txt));
			} else if (clubItemLists.get(position).getApplyState().equals(CHECK_NOT_PASS)) {
				stateTv.setText("未通过");
				stateTv.setTextColor(mContext.getResources().getColor(R.color.red_txt));
			}
		}
		return viewHolder.convertView;
	}

}
