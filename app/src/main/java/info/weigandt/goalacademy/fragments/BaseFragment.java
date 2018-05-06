package info.weigandt.goalacademy.fragments;

import android.support.v4.app.Fragment;

import info.weigandt.goalacademy.classes.Goal;

public abstract class BaseFragment extends Fragment {

    // Add abstract methods here if they are needed in all children of BaseFragment.
    // They might be used for communication TO all derived fragments from activity.
    // public abstract void updateViewNotifyGoalChanged(int position);

    public interface OnFragmentInteractionListener {
        void onGoalCompleted(Goal goal, String goldEarnedString);
        void onGoalChangedByFragment(Goal goal);
        void onGoalFailed(Goal goal);
    }
}