package com.ums.umslife.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.bean.ClubDetailBean;
import com.ums.umslife.utils.CommonViewHolder;

import java.util.List;

public class ClubMemberListAdapter extends BaseAdapter {
	private List<ClubDetailBean.ClubDetailsBean.ClubUserListBean> clubUserLists;

	public ClubMemberListAdapter(List itemList) {
		super();
		this.clubUserLists = itemList;
	}

	@Override
	public int getCount() {
		return clubUserLists == null ? 0 : clubUserLists.size();
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
				parent, R.layout.item_club_member_list);
		TextView nameTv = viewHolder.getTv(R.id.member_name_tv);
		TextView phoneTv = viewHolder.getTv(R.id.member_phone_tv);
		nameTv.setText(clubUserLists.get(position).getUserName());
		phoneTv.setText(clubUserLists.get(position).getPhone());
		return viewHolder.convertView;
	}

}
