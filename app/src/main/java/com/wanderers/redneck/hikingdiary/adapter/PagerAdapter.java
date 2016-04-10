package com.wanderers.redneck.hikingdiary.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.wanderers.redneck.hikingdiary.activities.PointFragment;
import com.wanderers.redneck.hikingdiary.model.Point;

import java.util.List;

/**
 * Created by Ari Iivari on 21.3.2015.
 */
public class PagerAdapter  extends FragmentPagerAdapter{
    private final List<Point> mList;

    public PagerAdapter(FragmentManager fm, List<Point> lp) {
        super(fm);
        mList = lp;
    }

    @Override
    public Fragment getItem(int i) {
        return (PointFragment.newInstance(mList.get(i)));
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
