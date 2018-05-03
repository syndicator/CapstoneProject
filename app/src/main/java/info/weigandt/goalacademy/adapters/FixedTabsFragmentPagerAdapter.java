package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import info.weigandt.goalacademy.R; // TODO try to remove this later on
import info.weigandt.goalacademy.fragments.BaseFragment;
import info.weigandt.goalacademy.fragments.GoalsFragment;
import info.weigandt.goalacademy.fragments.TrackFragment;
import info.weigandt.goalacademy.fragments.TrophiesFragment;
import timber.log.Timber;

public class FixedTabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public FixedTabsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        fragmentList = new ArrayList<>();
    }

    public ArrayList<BaseFragment> fragmentList;

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        // Do NOT try to save references to the Fragments in getItem(),
        // because getItem() is not always called. If the Fragment
        // was already created, then it will be retrieved from the FragmentManger
        // and not here (i.e. getItem() won't be called again).
        BaseFragment fragment;

        switch (position) {
            case 0:
                fragment = TrackFragment.newInstance(null, null);
                fragmentList.add(fragment);
                return fragment;
            case 1:
                fragment = GoalsFragment.newInstance(null, null);
                fragmentList.add(fragment);
                return fragment;
            case 2:
                fragment = TrophiesFragment.newInstance(null, null);
                fragmentList.add(fragment);
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getResources().getString(R.string.category_track);
        } else if (position == 1) {
            return mContext.getString(R.string.category_goals);
        } else {
            return mContext.getString(R.string.category_trophies);
        }
    }

    public void updateViewNotifyGoalInserted() {
        for (BaseFragment fragment : fragmentList) {
            // fragment.updateViewNotifyGoalChanged();
            fragment.updateViewNotifyGoalInserted();
            Timber.e("updating views from tabsFragmentPager. current fragment:");
            Timber.e(fragment.toString());
        }
    }

    public void updateViewNotifyGoalUpdated(int position) {
        for (BaseFragment fragment : fragmentList) {
            // fragment.updateViewNotifyGoalChanged();
            fragment.updateViewNotifyGoalChanged(position);
        }
    }
}