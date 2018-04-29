package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.weigandt.goalacademy.R; // TODO try to remove this later on
import info.weigandt.goalacademy.fragments.GoalsFragment;
import info.weigandt.goalacademy.fragments.TrackFragment;
import info.weigandt.goalacademy.fragments.TrophiesFragment;

public class FixedTabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public FixedTabsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
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
        switch (position) {
            case 0:
                return TrackFragment.newInstance(null, null);
            case 1:
                return GoalsFragment.newInstance(null, null);
            case 2:
                return TrophiesFragment.newInstance(null, null);
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
}
