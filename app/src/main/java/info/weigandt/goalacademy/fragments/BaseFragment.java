package info.weigandt.goalacademy.fragments;

import android.support.v4.app.Fragment;

import info.weigandt.goalacademy.classes.Goal;

public abstract class BaseFragment extends Fragment {
    public abstract void updateViewNotifyGoalChanged(int position);
    public abstract void updateViewNotifyGoalInserted();

    public interface OnFragmentInteractionListener {
        void onDataChangedByFragment();
        void onGoalChangedByFragment(Goal goal, int position);
    }
}