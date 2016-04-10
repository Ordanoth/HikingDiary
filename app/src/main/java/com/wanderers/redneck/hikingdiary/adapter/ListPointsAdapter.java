package com.wanderers.redneck.hikingdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.model.Point;

import java.util.List;

/**
 * Created by Ari Iivari on 24.2.2015.
 */
public class ListPointsAdapter extends BaseAdapter {

    private List<Point> mItems;
    private LayoutInflater mInflater;


    public ListPointsAdapter(Context context, List<Point> listPoints) {
        this.setItems(listPoints);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Point getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = mInflater.inflate(R.layout.point_item, parent, false);
            holder = new ViewHolder();
            holder.txtPointName = (TextView) v.findViewById(R.id.point_name);
            holder.txtPointDate = (TextView) v.findViewById(R.id.point_day);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }
        Point currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtPointName.setText(currentItem.getPointName());
            holder.txtPointDate.setText(currentItem.getPointDate());
        }

        return v;
    }

    public List<Point> getItems() {
        return mItems;
    }

    public void setItems(List<Point> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtPointName;
        TextView txtPointDate;
    }
}
