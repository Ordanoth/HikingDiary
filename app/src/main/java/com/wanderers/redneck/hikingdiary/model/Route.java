package com.wanderers.redneck.hikingdiary.model;

import java.io.Serializable;

/**
 * Created by Ari Iivari on 24.2.2015.
 */
public class Route implements Serializable{

    private long mId;
    private String mName;
    private String mDate;

    public Route(){}

    public Route(String name, String date){
        this.mName = name;
        this.mDate = date;
    }

    public long getId(){
        return mId;
    }

    public void setId(long mId){
        this.mId = mId;
    }

    public String getName(){
        return mName;
    }

    public void setName(String mRoutename){
        this.mName = mRoutename;
    }

    public String getDate(){
        return mDate;
    }

    public void setDate(String mRoutedate){
        this.mDate = mRoutedate;
    }

//    @Override
//    public Route getItem(int position) {
//        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
//    }
//
//    public List<Route> getItems() {
//        return mItems;
//    }
}
