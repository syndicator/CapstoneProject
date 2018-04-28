package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.weigandt.goalacademy.R; // TODO try to remove this later on
import info.weigandt.goalacademy.fragments.GoalsFragment;
import info.weigandt.goalacademy.fragments.TrackFragment;

public class FixedTabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public FixedTabsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;   // TODO use value from config or so?
    }

    @Override
    public Fragment getItem(int position) {
        // Do NOT try to save references to the Fragments in getItem(),
        // because getItem() is not always called. If the Fragment
        // was already created, then it will be retrieved from the FragmentManger
        // and not here (i.e. getItem() won't be called again).
        switch (position) {
            case 0:
                return TrackFragment.newInstance(null, null);  // TODO enter correct fragment
            case 1:
                return new GoalsFragment().newInstance(null, null);// TODO enter correct fragment
            case 2:
                return new Fragment();// TODO enter correct fragment
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getResources().getString(R.string.category_track);
        } else if (position == 2) {
            return mContext.getString(R.string.category_track); // TODO enter tab sections here
        } else {
            return mContext.getString(R.string.category_track); // TODO enter tab sections here
        }
    }
}
