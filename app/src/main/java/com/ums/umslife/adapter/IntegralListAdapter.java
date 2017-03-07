package com.ums.umslife.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.bean.IntegralBean;
import com.ums.umslife.utils.CommonViewHolder;

import java.util.List;

public class IntegralListAdapter extends BaseAdapter {
	private List<IntegralBean.IntegralsBean> integralLists;

	public IntegralListAdapter(List itemList) {
		super();
		this.integralLists = itemList;
	}

	@Override
	public int getCount() {
		return integralLists == null ? 0 : integralLists.size();
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
				parent, R.layout.item_integral_list);
		TextView wayTv = viewHolder.getTv(R.id.way_tv);
		TextView integralTv = viewHolder.getTv(R.id.integral_tv);
		TextView detailsTv = viewHolder.getTv(R.id.details_tv);
		TextView timeTv = viewHolder.getTv(R.id.time_tv);
		View lineV = viewHolder.getView(R.id.view_line);
		if (position == integralLists.size() - 1) {
			lineV.setVisibility(View.VISIBLE);
		} else {
			lineV.setVisibility(View.GONE);
		}
		integralTv.setText("+" + integralLists.get(position).getActIntegral());
		detailsTv.setText(integralLists.get(position).getActivityTheme());
		timeTv.setText(integralLists.get(position).getSignTime());
		return viewHolder.convertView;
	}

}
