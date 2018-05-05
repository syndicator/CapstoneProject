package info.weigandt.goalacademy.fragments;

import android.support.v4.app.Fragment;

import info.weigandt.goalacademy.classes.Goal;

public abstract class BaseFragment extends Fragment {
    // public abstract void updateViewNotifyGoalChanged(int position);

    // This method is not perfect yet, each time a goal is inserted,
    //  the Trophy View also will update trophies and vice versa...
    // public abstract void updateViewNotifyGoalInserted();

    public interface OnFragmentInteractionListener {
        void onDataChangedByFragment();
        void onGoalChangedByFragment(Goal goal, int position);
        void onGoalCompleted(Goal goal, int position, String goldEarnedString);
        void onGoalProgressChanged(Goal goal, int position);
    }
}