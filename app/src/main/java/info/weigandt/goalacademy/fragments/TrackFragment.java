package info.weigandt.goalacademy.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.TrackListAdapter;
import info.weigandt.goalacademy.classes.ThreeStatesButton;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;

/**
 * A fragment
 * Activities that contain this fragment must implement the...
 * to handle interaction events.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mFragmentInteractionListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rv_track)
    RecyclerView mRecyclerView;
    //private TrackListAdapter mAdapter;
    private RecyclerView.Adapter mAdapter;  // TODO is this sup  class enough?
    private RecyclerView.LayoutManager mLayoutManager;


    // private OnFragmentInteractionListener mListener; TODO keep only if...

    public TrackFragment() {
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
    public static TrackFragment newInstance(String param1, String param2) {
        TrackFragment fragment = new TrackFragment();
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
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        ButterKnife.bind(this, view);
        initializeAdapter();
        return view;
    }

    private void initializeAdapter() {
        mAdapter = new TrackListAdapter(getContext(), new TrackListAdapter.TrackListAdapterListener() {
            @Override
            public void button_0_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

                // TODO save the state to Firebase :)

                // we are about to change the setting of monday in the current week
                // so the element we want to change is some member of goal object:
                // private List<Integer> counterCompletedEvents;
                // for ease of processing, the counter should be accessible through the week number and the year
                // deriving from start date and the position is quite too complicated


                // TODO process the change here.
                // TODO position gives the track which has been clicked on
                // TODO state shows the desired status on a specific day (here button_0 == monday)
                // TODO View v is WHAT here??? the button, or the surrounding item element????
            }

            @Override
            public void button_1_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

            }

            @Override
            public void button_2_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

            }

            @Override
            public void button_3_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

            }

            @Override
            public void button_4_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

            }

            @Override
            public void button_5_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

            }

            @Override
            public void button_6_OnClick(View v, int position, ThreeStatesButton.StatesEnum state) {

            }
        });  // TODO change signature later
        mRecyclerView.setAdapter(mAdapter);
        // Using a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Set only if rv doesn't change size // TODO check if this setting is right
        mRecyclerView.setHasFixedSize(true);
        // mAdapter.notifyDataSetChanged(); TODO: needed? (presumably not here)

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void updateView() {
        mAdapter.notifyItemInserted(sGoalList.size() - 1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseFragment.OnFragmentInteractionListener) {
            mFragmentInteractionListener = (BaseFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionListener = null;    // TODO check if outdated
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