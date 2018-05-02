package info.weigandt.goalacademy.fragments;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public abstract void updateView();

    public interface OnFragmentInteractionListener {
        void onDataChangedByFragment();
    }
}
