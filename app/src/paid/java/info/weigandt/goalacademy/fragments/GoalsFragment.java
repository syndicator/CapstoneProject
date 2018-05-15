package info.weigandt.goalacademy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.GoalListAdapter;
import info.weigandt.goalacademy.classes.FirebaseOperations;
import info.weigandt.goalacademy.classes.Goal;

import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;
import static info.weigandt.goalacademy.activities.MainActivity.sIsLoadingFromFirebase;

/**
 * A fragment
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Firebase related
    /* moved to mainActivity
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mGoalsDatabaseReference;   // TODO: Adjust to correct node. users?
    private ChildEventListener mGoalsEventListener;
    */

    @BindView(R.id.rv_goals) RecyclerView mRecyclerView;
    @BindView(R.id.fab_add) FloatingActionButton mFloatingActionButtonAdd;
    @BindView(R.id.tv_quote) TextView mQuoteTextView;
    @BindView(R.id.goals_loading_indicator)
    ProgressBar mGoalsLoadingProgressBar;

    //private TrackListAdapter mAdapter;
    private RecyclerView.Adapter mAdapter;  // TODO is this sup  class enough?
    private RecyclerView.LayoutManager mLayoutManager;

    // Fragment Interaction Interface

    /*
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    private OnFragmentInteractionListener mFragmentInteractionListener;

    public GoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalsFragment newInstance(String param1, String param2) {
        GoalsFragment fragment = new GoalsFragment();
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
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        ButterKnife.bind(this, view);


        //initializeFirebase();
        //loadFirebaseData();
        initializeAdapter();


        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomFragmentDialog();
            }
        });
        return view;
    }

    public void updateViewNotifyGoalInserted() {
        mAdapter.notifyItemInserted(sGoalList.size() - 1);
        //  issues.remove(position);
        //                    notifyItemRemoved(position);
        //                    //this line below gives you the animation and also updates the
        //                    //list items after the deleted item
        //                    notifyItemRangeChanged(position, getItemCount());
    }

    public void updateViewNotifyGoalRemoved() {
        mAdapter.notifyDataSetChanged();
    }

    public void updateViewNotifyGoalChanged(int position) {
        mAdapter.notifyItemRangeChanged(position, sGoalList.size());
    }

    public void hideLoadingIndicator()
    {
        mGoalsLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showCustomFragmentDialog() {
        FragmentManager fm = getFragmentManager();
        CustomDialogFragment customDialogFragment = CustomDialogFragment.newInstance("New goal TODO check code"); // TODO enter resource string here
        customDialogFragment.setCustomDialogFragmentListener(new CustomDialogFragment.CustomDialogFragmentListener() {
            @Override
            public void onDialogPositiveClick(Goal goal) {
                // goalList.add(goal); // TODO enter proper data processing here (update view also)
                FirebaseOperations.addGoalToDatabase(goal);
            }
        });
        customDialogFragment.show(fm, "fragment_edit_name");
        //new CustomDialogFragment().show(getFragmentManager(), "CustomDialogFragment");

    }

    private void initializeAdapter() {
        mAdapter = new GoalListAdapter(getContext());
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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionListener = null; // TODO remove if no needed
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (sIsLoadingFromFirebase)
        {
            mGoalsLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void clearAdapter(int size) {
        mAdapter.notifyItemRangeRemoved(0, size);
    }

    public void updateViewQuoteChanged(String quoteText, String quoteAuthor) {
        String quote = quoteText + "\\n     - " + quoteAuthor;
        quote = quote.replace("\\n", System.getProperty("line.separator"));

        mQuoteTextView.setText(quote);
    }
}