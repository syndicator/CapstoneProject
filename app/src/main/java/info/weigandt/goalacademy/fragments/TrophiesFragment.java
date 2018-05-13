package info.weigandt.goalacademy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.TrophyListAdapter;

import static info.weigandt.goalacademy.activities.MainActivity.sTrophyList;

/**
 * A fragment
 * Activities that contain this fragment must implement the..
 * to handle interaction events.
 * Use the {@link TrophiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrophiesFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rv_trophies) RecyclerView mRecyclerView;
    @BindView(R.id.trophies_loading_indicator)
    ProgressBar mTrophiesLoadingProgressBar;
    //private TrackListAdapter mAdapter;
    private RecyclerView.Adapter mAdapter;  // TODO is this sup  class enough?
    private RecyclerView.LayoutManager mLayoutManager;

    // private OnFragmentInteractionListener mListener; TODO keep only if...

    public TrophiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrophiesFragment newInstance(String param1, String param2) {
        TrophiesFragment fragment = new TrophiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trophies, container, false);
        ButterKnife.bind(this, view);
        initializeAdapter();
        return view;
    }

    private void initializeAdapter() {
        mAdapter = new TrophyListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        // Using a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Set only if rv doesn't change size // TODO check if this setting is right
        mRecyclerView.setHasFixedSize(true);
        // mAdapter.notifyDataSetChanged(); TODO: needed? (presumably not here)

    }

    public void hideLoadingIndicator()
    {
        mTrophiesLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /* TODO keep only if communication between Fragments and/or Activity is needed
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }
/*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null; TODO remove if no needed
    }
    */

    public void updateViewNotifyTrophyInserted() {
        mAdapter.notifyItemInserted(sTrophyList.size() - 1);
    }

    public void clearAdapter(int size) {
        mAdapter.notifyItemRangeRemoved(0, size);
    }

    /* TODO remove if not needed
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}