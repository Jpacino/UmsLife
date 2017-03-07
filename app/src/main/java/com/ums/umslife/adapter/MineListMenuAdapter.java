package com.ums.umslife.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.bean.MineMenuBean;
import com.ums.umslife.utils.CommonViewHolder;

import java.util.List;

public class MineListMenuAdapter extends BaseAdapter {
	private List<MineMenuBean> itemLists;
	
	
	public MineListMenuAdapter(List<MineMenuBean> itemLists) {
		super();
		this.itemLists = itemLists;
	}

	@Override
	public int getCount() {
		return itemLists==null?0:itemLists.size();
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
		CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_mine_menu);
		TextView tv = viewHolder.getTv(R.id.tv_menu_name);
		MineMenuBean mineMenuBean = itemLists.get(position);
		tv.setText(mineMenuBean.getName()+"");
		return viewHolder.convertView;
	}

}
