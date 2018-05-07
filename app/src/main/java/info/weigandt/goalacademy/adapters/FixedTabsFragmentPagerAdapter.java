package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.fragments.BaseFragment;
import info.weigandt.goalacademy.fragments.GoalsFragment;
import info.weigandt.goalacademy.fragments.TrackFragment;
import info.weigandt.goalacademy.fragments.TrophiesFragment;

public class FixedTabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private TrackFragment mTrackFragment;
    private GoalsFragment mGoalsFragment;
    private TrophiesFragment mTrophiesFragment;
    public ArrayList<BaseFragment> fragmentList;

    public FixedTabsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        fragmentList = new ArrayList<>();
    }

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
                return fragment;
            case 1:
                fragment = GoalsFragment.newInstance(null, null);
                return fragment;
            case 2:
                fragment = TrophiesFragment.newInstance(null, null);
                return fragment;
            default:
                return null;
        }
    }

    // Here we can finally safely save a reference to the created
    // Fragment, no matter where it came from (either getItem() or
    // FragmentManger). Simply save the returned Fragment from
    // super.instantiateItem() into an appropriate reference depending
    // on the ViewPager position.   ->     #loveIt :)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                mTrackFragment = ((TrackFragment) createdFragment);
                fragmentList.add(mTrackFragment);
                break;
            case 1:
                mGoalsFragment = ((GoalsFragment) createdFragment);
                fragmentList.add(mGoalsFragment);
                break;
            case 2:
                mTrophiesFragment = ((TrophiesFragment) createdFragment);
                fragmentList.add(mTrophiesFragment);
                break;
        }
        return createdFragment;
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

    public void updateViewsNotifyGoalInserted() {
        mTrackFragment.updateViewNotifyGoalInserted();
        mGoalsFragment.updateViewNotifyGoalInserted();
    }

    public void updateViewsNotifyGoalRemoved(int position) {
        // TODO maybe not needed if listener is set??? hmmmmm
        mTrackFragment.updateViewNotifyGoalRemoved();
        mGoalsFragment.updateViewNotifyGoalRemoved();

    }

    public void updateViewsNotifyGoalUpdated(int position) {
        mGoalsFragment.updateViewNotifyGoalChanged(position);
        mTrackFragment.updateViewNotifyGoalChanged(position);
    }

    public void updateViewNotifyTrophyInserted() {
        if (mTrophiesFragment != null) {
            mTrophiesFragment.updateViewNotifyTrophyInserted();
        }
    }

    public void updateGoalsFragmentNofifyGoalChanged(int position) {
        mGoalsFragment.updateViewNotifyGoalChanged(position);
    }

    public void clearAdapters(int sizeGoalList, int sizeTrophyList) {
        if (mTrackFragment != null) {
            mTrackFragment.clearAdapter(sizeGoalList);
        }
        if (mGoalsFragment != null) {
            mGoalsFragment.clearAdapter(sizeGoalList);
        }
        if (mTrophiesFragment != null) {
            mTrophiesFragment.clearAdapter(sizeTrophyList);
        }
    }
}