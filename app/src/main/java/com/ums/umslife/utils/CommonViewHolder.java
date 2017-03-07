package com.ums.umslife.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class CommonViewHolder {
    public final View convertView;
    private Map<Integer, View> views = new HashMap<>();


    private CommonViewHolder(View convertView) {
        this.convertView = convertView;
    }

    public static CommonViewHolder createCVH(View convertView, ViewGroup parent, int layoutRes) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(layoutRes, parent, false);
            CommonViewHolder viewHolder = new CommonViewHolder(convertView);

            convertView.setTag(viewHolder);
        }

        return (CommonViewHolder) convertView.getTag();
    }


    public View getView(int id) {
        if (views.get(id) == null) {
            views.put(id, convertView.findViewById(id));
        }
        return views.get(id);
    }

    public TextView getTv(int id) {
        return ((TextView) getView(id));
    }

    public ImageView getIv(int id) {
        return ((ImageView) getView(id));
    }
}
