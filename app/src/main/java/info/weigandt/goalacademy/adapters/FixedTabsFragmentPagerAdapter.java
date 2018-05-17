package info.weigandt.goalacademy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.fragments.BaseFragment;
import info.weigandt.goalacademy.fragments.GoalsFragment;
import info.weigandt.goalacademy.fragments.TrackFragment;
import info.weigandt.goalacademy.fragments.TrophiesFragment;
import timber.log.Timber;

public class FixedTabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private final FragmentManager mFragmentManager;
    public TrackFragment trackFragment;
    public GoalsFragment goalsFragment;
    public TrophiesFragment trophiesFragment;

    public FixedTabsFragmentPagerAdapter(FragmentManager fm, Context context, TrackFragment trackFragment) {
        super(fm);
        mContext = context;
        mFragmentManager = fm;
        if (trackFragment != null) {
            this.trackFragment = trackFragment;
        }
    }

    @Override
    public int getCount() {
        return Config.PAGE_COUNT;
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
                return TrackFragment.newInstance();
                /*
                if (trackFragment == null)
                {
                    return TrackFragment.newInstance();
                }
                else
                {
                    return this.trackFragment;
                }
                */
                /* TODO tried to add a tag :/
                mFragmentManager.beginTransaction()
                        .add(R.id.fragment_track, fragment, Constants.TRACK_FRAGMENT_TAG)
                        .commit();
                        */
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
                trackFragment = ((TrackFragment) createdFragment);
                break;
            case 1:
                goalsFragment = ((GoalsFragment) createdFragment);
                break;
            case 2:
                trophiesFragment = ((TrophiesFragment) createdFragment);
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

    public void updateViewsUpdateRecyclerViews() {
        // not possible, null pointer
        // trackFragment.updateRecyclerView();
        // goalsFragment.upda
        // TODO enter those fragments also
    }

    public void updateViewsNotifyGoalInserted() {
        trackFragment.updateViewNotifyGoalInserted();
        goalsFragment.updateViewNotifyGoalInserted();
    }

    public void updateViewsNotifyGoalRemoved(int position) {
        // TODO maybe not needed if listener is set??? hmmmmm
        trackFragment.updateViewNotifyGoalRemoved();
        goalsFragment.updateViewNotifyGoalRemoved();

    }

    public void updateViewsNotifyGoalUpdated(int position) {
        goalsFragment.updateViewNotifyGoalChanged(position);
        trackFragment.updateViewNotifyGoalChanged(position);
    }

    public void updateViewNotifyTrophyInserted() {
        if (trophiesFragment != null) {
            trophiesFragment.updateViewNotifyTrophyInserted();
        }
    }

    public void updateGoalsFragmentNotifyGoalChanged(int position) {    // TODO check if  still in use
        trackFragment.updateViewNotifyGoalChanged(position); // handled by button view logic also :/
        goalsFragment.updateViewNotifyGoalChanged(position);
    }

    public void clearAdapters(int sizeGoalList, int sizeTrophyList) {
        if (trackFragment != null) {
            trackFragment.clearAdapter(sizeGoalList);
        }
        if (goalsFragment != null) {
            goalsFragment.clearAdapter(sizeGoalList);
        }
        if (trophiesFragment != null) {
            trophiesFragment.clearAdapter(sizeTrophyList);
        }
    }

    public void updateViewNotifyQuoteChanged(String quoteText, String quoteAuthor) {
        if (goalsFragment != null) {
            goalsFragment.updateViewQuoteChanged(quoteText, quoteAuthor);
        }
    }
}