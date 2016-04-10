package com.wanderers.redneck.hikingdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wanderers.redneck.hikingdiary.R;
import com.wanderers.redneck.hikingdiary.model.Route;

import java.util.List;

/**
 * Created by Ari Iivari on 24.2.2015.
 */
public class ListRoutesAdapter extends BaseAdapter {

    private List<Route> mItems;
    private LayoutInflater mInflater;

    public ListRoutesAdapter(Context context, List<Route> listRoutes) {
        this.setItems(listRoutes);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Route getItem(int position) {
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
            v = mInflater.inflate(R.layout.trip_item, parent, false);
            holder = new ViewHolder();
            holder.txtRouteName = (TextView) v.findViewById(R.id.PlaceName);
            holder.txtRouteDate = (TextView) v.findViewById(R.id.textStartDay);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        Route currentItem = getItem(position);
        if(currentItem != null) {
            holder.txtRouteName.setText(currentItem.getName());
            holder.txtRouteDate.setText(currentItem.getDate());
        }

        return v;
    }

    public List<Route> getItems() {
        return mItems;
    }

    public void setItems(List<Route> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txtRouteName;
        TextView txtRouteDate;
    }
}
