package info.weigandt.goalacademy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.TrackListAdapter;
import info.weigandt.goalacademy.classes.AlertDialogFactory;
import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.classes.Constants;
import info.weigandt.goalacademy.classes.FirebaseOperations;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.GoalHelper;
import info.weigandt.goalacademy.classes.ThreeStatesButton;
import info.weigandt.goalacademy.classes.WrapLinearLayoutManager;
import info.weigandt.goalacademy.enums.EventStateEnum;
import info.weigandt.goalacademy.enums.GoalStatusPseudoEnum;

import static info.weigandt.goalacademy.activities.MainActivity.sAreGoalsLoadingFromFirebase;
import static info.weigandt.goalacademy.activities.MainActivity.sGoalList;
import static info.weigandt.goalacademy.activities.MainActivity.sTrackFragmentListState;

/**
 * A fragment
 * Activities that contain this fragment must implement the...
 * to handle interaction events.
 * Use the {@link TrackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackFragment extends BaseFragment {
    private OnFragmentInteractionListener mFragmentInteractionListener;
    private Parcelable mListState;
    private boolean mIsRestoredFromState = false;
    private LocalDate mDisplayedWeek;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static String sYearWeekString;
    public static long sLastClickTime;

    @BindView(R.id.rv_track)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_current_week)
    Button mCurrentWeekButton;
    @BindView(R.id.button_week_increase)
    ImageButton mIncreaseWeekButton;
    @BindView(R.id.button_week_decrease)
    ImageButton mDecreaseWeekButton;
    @BindView(R.id.fab_add)
    FloatingActionButton mFloatingActionButtonAdd;

    @BindView(R.id.track_loading_indicator)
    ProgressBar mTrackLoadingProgressBar;

    private long mLastClickTime;

    public TrackFragment() { // Required empty public constructor
    }

    public static TrackFragment newInstance() {
        TrackFragment fragment = new TrackFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreFromSavedInstanceState(savedInstanceState);
        }
        if (savedInstanceState == null) {
            setDisplayedWeek(LocalDate.now());
        }
    }

    public void hideLoadingIndicator()
    {
        mTrackLoadingProgressBar.setVisibility(View.INVISIBLE);
    }
    public void showLoadingIndicator()
    {
        mTrackLoadingProgressBar.setVisibility(View.VISIBLE);
    }


    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        String isoDate = savedInstanceState.getString(Constants.BUNDLE_ISO_DATE_DISPLAYED_WEEK);
        setDisplayedWeek(GoalHelper.convertFromIsoDate(isoDate)); // this also sets sYearWeekString
        mIsRestoredFromState = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        ButterKnife.bind(this, view);
        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomFragmentDialog();
            }
        });


        if (!mIsRestoredFromState) {
            mCurrentWeekButton.setText(GoalHelper.convertToGuiString(LocalDate.now()));
        }
        else
        {
            mCurrentWeekButton.setText(GoalHelper.convertToGuiString(mDisplayedWeek));
        }
        mCurrentWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSafeClick()) return;
                if (!(getWeekFrom(mDisplayedWeek).equals(getWeekFrom(LocalDate.now())))) {
                    setDisplayedWeek(LocalDate.now());
                    mCurrentWeekButton.setText(GoalHelper.convertToGuiString(mDisplayedWeek));
                    updateRecyclerView();
                }
            }
        });
        mIncreaseWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSafeClick()) return;
                setDisplayedWeek(mDisplayedWeek.plusWeeks(1));
                mCurrentWeekButton.setText(GoalHelper.convertToGuiString(mDisplayedWeek));
                updateRecyclerView();
            }
        });
        mDecreaseWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSafeClick()) return;
                setDisplayedWeek(mDisplayedWeek.minusWeeks(1));
                mCurrentWeekButton.setText(GoalHelper.convertToGuiString(mDisplayedWeek));
                updateRecyclerView();
            }
        });
        if (!mIsRestoredFromState && sAreGoalsLoadingFromFirebase) {
            showLoadingIndicator();
        }
        return view;
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
    private boolean isSafeClick() {
        // Mis-clicking prevention, using threshold of 100 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeAdapter();
        if (savedInstanceState != null) {
            // Restore the fragment's state here... hmmm TODO not sure about that man
            // TODO Restore anything thats needs a completed Activity OnCreate here!!!
        }
        mAdapter.notifyDataSetChanged(); // TODO: needed? (presumably not here)
        if (sAreGoalsLoadingFromFirebase)
        {
            mTrackLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    }


    public void setDisplayedWeek(LocalDate displayedWeek) {
        mDisplayedWeek = displayedWeek;
        sYearWeekString = GoalHelper.convertToFirebaseString(mDisplayedWeek);   // TODO: optionally check helper class to superflous conversions
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // updateRecyclerView();
    }

    public void updateRecyclerView() {
        mAdapter.notifyDataSetChanged();
    }

    private String getWeekFrom(LocalDate localDate) {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return (String.valueOf(localDate.get(weekOfYear)));
    }

    private void initializeAdapter() {

        mAdapter = new TrackListAdapter(getContext(), new TrackListAdapter.TrackListAdapterListener() {
            @Override
            public void button_0_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 0, threeStatesButton);
            }

            @Override
            public void button_1_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 1, threeStatesButton);
            }

            @Override
            public void button_2_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 2, threeStatesButton);
            }

            @Override
            public void button_3_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 3, threeStatesButton);
            }

            @Override
            public void button_4_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 4, threeStatesButton);
            }

            @Override
            public void button_5_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 5, threeStatesButton);
            }

            @Override
            public void button_6_OnClick(ThreeStatesButton threeStatesButton, int position) {
                EventStateEnum state = threeStatesButton.getState();
                processButtonClick(position, state, 6, threeStatesButton);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        // Using a linear layout manager
        mLayoutManager = new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Set only if rv doesn't change size // TODO check if this setting is right
        mRecyclerView.setHasFixedSize(true);
    }

    private void processButtonClick(int position, EventStateEnum state, int weekday, ImageButton imageButton) {
        Goal goal = sGoalList.get(position);
        Goal changedGoal;
        String description;
        // DANGER: the state committed is the previous state BEFORE clicking.
        switch (state) {
            case PASS:
                changedGoal = GoalHelper.ChangeEventEntryInGoal(goal, EventStateEnum.FAIL, weekday, mDisplayedWeek);
                // this day was a fail! check if goal is failed also now
                calculateStatusOfGoal(changedGoal, EventStateEnum.FAIL);

                description =  getResources().getString(R.string.description_three_states_button_fail);
                imageButton.setContentDescription(description);
                break;
            case FAIL:
                changedGoal = GoalHelper.ChangeEventEntryInGoal(goal, EventStateEnum.NEUTRAL, weekday, mDisplayedWeek);
                calculateStatusOfGoal(changedGoal, EventStateEnum.NEUTRAL);

                description =  getResources().getString(R.string.description_three_states_button_neutral);
                imageButton.setContentDescription(description);
                break;
            case NEUTRAL:
                changedGoal = GoalHelper.ChangeEventEntryInGoal(goal, EventStateEnum.PASS, weekday, mDisplayedWeek);
                // this day was a pass! check if goal is progressing now
                calculateStatusOfGoal(changedGoal, EventStateEnum.PASS);
                description =  getResources().getString(R.string.description_three_states_button_pass);
                imageButton.setContentDescription(description);
                break;
        }
        // mFragmentInteractionListener.onGoalChangedByFragment(goal, position);

        // TODO #1: Update the GUI!
        // TODO this might be needed to inform the other fragments of changes (e.g. new trophy, removed goal etc.)
        //mFragmentInteractionListener.onDataChangedByFragment();

        // GUI update is needed, because after returning to this week,
        //   old data might reside in the adapter which differs from sGoalList.... hmmm, really?
        // mRecyclerView.getAdapter().notifyItemChanged(position);
        // mRecyclerView.getAdapter().notifyItemRangeRemoved(0, sGoalList.size()-1);

    }

    @SuppressLint("ResourceType")
    private void calculateStatusOfGoal(Goal goal, EventStateEnum eventState) {
        //////// region PASSED  ////////////////////////
        if (eventState.equals(EventStateEnum.PASS)) {
            int award = GoalHelper.calculateAward(goal);
            if (award == GoalStatusPseudoEnum.GOLD_EARNED) {
                // Following line is not needed, because we delete the goal 'n turn it into a trophy,
                //  but may be useful in a future version
                goal.setStatus(GoalStatusPseudoEnum.GOLD_EARNED);
                // Invoke parent activity to handle the goal completion. We send the final award also.
                mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.GOLD_EARNED_STRING));

            } else if (award == GoalStatusPseudoEnum.SILVER_EARNED) {
                goal.setStatus(GoalStatusPseudoEnum.SILVER_EARNED);
                mFragmentInteractionListener.onGoalChangedByFragment(goal);

            } else if (award == GoalStatusPseudoEnum.BRONZE_EARNED) {
                goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                mFragmentInteractionListener.onGoalChangedByFragment(goal);
            } else {
                // This a "normal" pass with an increase in streak only
                mFragmentInteractionListener.onGoalChangedByFragment(goal);
            }
        }
        //////// endregion PASSED  //////////////////////

        //////// region FAILED /////////////////////////
        else if (eventState.equals(EventStateEnum.FAIL)) {
            // checking for near / total failure now
            // skip this block otherwise, if fixed days are set
            if (goal.getTimesPerWeek() != 0) {
                int passedDays = GoalHelper.calculateNumberOfPassesGivenWeek(goal, mDisplayedWeek);
                int failedDays = GoalHelper.calculateNumberOfFails(goal, mDisplayedWeek);
                int daysLeft = 7 - failedDays - passedDays;
                int criticalSum = daysLeft - goal.getTimesPerWeek() + passedDays;

                // HANDLING THE CRITICAL STATES
                if (criticalSum < 0) {
                    // Version 2: If totally failed, open an dialog with the option to confirm
                    // failure or to reset this event!
                    String awardName;
                    int award = GoalHelper.calculateFinalAward(goal);
                    if (award == GoalStatusPseudoEnum.GOLD_EARNED) {
                        awardName = getContext().getResources().getString(R.string.GOLD_EARNED_STRING);
                        goal.setStatus(GoalStatusPseudoEnum.GOLD_EARNED);
                        // Invoke parent activity to handle the goal completion. We send the final award also.
                        mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.GOLD_EARNED_STRING));

                    } else if (award == GoalStatusPseudoEnum.SILVER_EARNED) {
                        awardName = getContext().getResources().getString(R.string.SILVER_EARNED_STRING);
                        goal.setStatus(GoalStatusPseudoEnum.SILVER_EARNED);
                        // Invoke parent activity to handle the goal completion. We send the final award also.
                        mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.SILVER_EARNED_STRING));
                    } else if (award == GoalStatusPseudoEnum.BRONZE_EARNED) {
                        awardName = getContext().getResources().getString(R.string.BRONZE_EARNED_STRING);
                        goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                        // Invoke parent activity to handle the goal completion. We send the final award also.
                        mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.BRONZE_EARNED_STRING));
                    } else {
                        awardName = getContext().getResources().getString(R.string.BEGINNER_STRING);
                        mFragmentInteractionListener.onGoalFailed(goal);
                    }

                    AlertDialog alertDialog = AlertDialogFactory.createFailDialog(awardName, getActivity());
                    alertDialog.show();

                } else if (criticalSum == 1) {
                    // Version 2: If nearly failed == last day to complete the goal for this week,
                    // change the GUI to critical for this item
                } else {
                    // "Normal" fail
                    mFragmentInteractionListener.onGoalChangedByFragment(goal);
                }
                // END HANDLING THE CRITICAL STATES
            } else {
                // Will be a total fail in every case!
                // Version 2: Ask first to confirm fail on this day
                int award = GoalHelper.calculateFinalAward(goal);
                String awardName;
                if (award == GoalStatusPseudoEnum.GOLD_EARNED) {
                    awardName = getContext().getResources().getString(R.string.GOLD_EARNED_STRING);
                    goal.setStatus(GoalStatusPseudoEnum.GOLD_EARNED);
                    // Invoke parent activity to handle the goal completion. We send the final award also.
                    mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.GOLD_EARNED_STRING));
                } else if (award == GoalStatusPseudoEnum.SILVER_EARNED) {
                    awardName = getContext().getResources().getString(R.string.SILVER_EARNED_STRING);
                    goal.setStatus(GoalStatusPseudoEnum.SILVER_EARNED);
                    // Invoke parent activity to handle the goal completion. We send the final award also.
                    mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.SILVER_EARNED_STRING));
                } else if (award == GoalStatusPseudoEnum.BRONZE_EARNED) {
                    awardName = getContext().getResources().getString(R.string.BRONZE_EARNED_STRING);
                    goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                    // Invoke parent activity to handle the goal completion. We send the final award also.
                    mFragmentInteractionListener.onGoalCompleted(goal, getContext().getResources().getString(R.string.BRONZE_EARNED_STRING));
                } else {
                    // Failed without any award. No trophy generated.
                    awardName = getContext().getResources().getString(R.string.BEGINNER_STRING);
                    mFragmentInteractionListener.onGoalFailed(goal);
                }
                AlertDialog alertDialog = AlertDialogFactory.createFailDialog(awardName, getActivity());
                alertDialog.show();
            }
        }
        //////// endregion FAILED ////////////////////////

        //////// region NEUTRAL ////////////////////////
        else {
            mFragmentInteractionListener.onGoalChangedByFragment(goal);
        }
        //////// endregion NEUTRAL ////////////////////////
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseFragment.OnFragmentInteractionListener) {
            mFragmentInteractionListener = (BaseFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + Config.MUST_IMPLEMENT_LISTENER);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionListener = null;
    }

    public void updateViewNotifyGoalInserted() {
        mAdapter.notifyItemInserted(sGoalList.size() - 1);
    }

    public void updateViewNotifyGoalChanged(int position) {
        mAdapter.notifyItemChanged(position);   // TODO change method to keep an animation? see stackoverflow...
    }

    public void updateViewNotifyGoalRemoved() {
        mAdapter.notifyDataSetChanged();
    }

    public void clearAdapter(int size) {
        mAdapter.notifyItemRangeRemoved(0, size);
    }

    //=========================================================================
    //region Saving the state
    //=========================================================================
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save variables
        outState.putString(Constants.BUNDLE_ISO_DATE_DISPLAYED_WEEK,
                GoalHelper.convertToIsoDate(mDisplayedWeek));

        // Save the List state
        if (mRecyclerView != null)  // might be "out of view" when TrophyFragment is active
        {
            mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(Constants.BUNDLE_TRACK_RECYCLER_LAYOUT, mListState);

            // Saving the List state to main
            sTrackFragmentListState = mListState;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Retrieve list state and list/item positions. Update it in onResume!
        if (savedInstanceState != null) {
            mListState = sTrackFragmentListState;
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            mIsRestoredFromState = true;    // TODO check if actually needed
        }
    }
    //=========================================================================
    //endregion Saving the state
    //=========================================================================
}