package info.weigandt.goalacademy.fragments;

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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.GoalListAdapter;
import info.weigandt.goalacademy.classes.FirebaseOperations;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.WrapLinearLayoutManager;

import static info.weigandt.goalacademy.activities.MainActivity.sAreGoalsLoadingFromFirebase;
import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;

/**
 * A fragment
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends BaseFragment {
    @BindView(R.id.rv_goals) RecyclerView mRecyclerView;
    @BindView(R.id.fab_add) FloatingActionButton mFloatingActionButtonAdd;
    @BindView(R.id.adView) AdView mAdView;
    @BindView(R.id.tv_quote) TextView mQuoteTextView;
    @BindView(R.id.goals_loading_indicator)
    ProgressBar mGoalsLoadingProgressBar;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean mIsRestoredFromState;

    public GoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GoalsFragment.
     */
    public static GoalsFragment newInstance() {
        GoalsFragment fragment = new GoalsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsRestoredFromState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        ButterKnife.bind(this, view);
        initializeAdapter();
        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomFragmentDialog();
            }
        });
        initializeAdMob();
        if (!mIsRestoredFromState && sAreGoalsLoadingFromFirebase) {
            showLoadingIndicator();
        }
        return view;
    }

    public void updateViewNotifyGoalInserted() {
        mAdapter.notifyItemInserted(sGoalList.size() - 1);
    }

    public void updateViewNotifyGoalRemoved() {
        mAdapter.notifyDataSetChanged();
    }

    public void updateViewNotifyGoalChanged(int position) {
        mAdapter.notifyItemRangeChanged(position, sGoalList.size());
    }

    private void initializeAdMob()
    {
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getContext(), getResources().getString(R.string.sample_admob_app_ID_for_activity));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void hideLoadingIndicator()
    {
        mGoalsLoadingProgressBar.setVisibility(View.INVISIBLE);
    }
    public void showLoadingIndicator()
    {
        mGoalsLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void showCustomFragmentDialog() {
        FragmentManager fm = getFragmentManager();
        CustomDialogFragment customDialogFragment = CustomDialogFragment.newInstance(getString(R.string.NEW_GOAL_TEXT));
        customDialogFragment.setCustomDialogFragmentListener(new CustomDialogFragment.CustomDialogFragmentListener() {
            @Override
            public void onDialogPositiveClick(Goal goal) {
                FirebaseOperations.addGoalToDatabase(goal);
            }
        });
        customDialogFragment.show(fm, getString(R.string.FRAGMENT_EDIT_NAME));
        //new CustomDialogFragment().show(getFragmentManager(), "CustomDialogFragment");

    }

    private void initializeAdapter() {
        mAdapter = new GoalListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        // Using a linear layout manager
        mLayoutManager = new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Set only if rv doesn't change size
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (sAreGoalsLoadingFromFirebase)
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