package info.weigandt.goalacademy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.TrackListAdapter;
import info.weigandt.goalacademy.classes.AlertDialogFactory;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.GoalHelper;
import info.weigandt.goalacademy.classes.WrapLinearLayoutManager;
import info.weigandt.goalacademy.enums.EventStateEnum;
import info.weigandt.goalacademy.enums.GoalStatusPseudoEnum;

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

    public void setDisplayedWeek(LocalDate displayedWeek) {
        mDisplayedWeek = displayedWeek;
        sYearWeekString = GoalHelper.convertDateToYearWeekString(mDisplayedWeek);   // TODO: optionally check helper class to superflous conversions
    }

    // private String mYearWeekString;
    private LocalDate mDisplayedWeek;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @BindView(R.id.rv_track)
    RecyclerView mRecyclerView;
    @BindView(R.id.button_week_current)
    Button mCurrentWeekButton;
    @BindView(R.id.button_week_increase)
    ImageButton mIncreaseWeekButton;
    @BindView(R.id.button_week_decrease)
    ImageButton mDecreaseWeekButton;

    /*@BindView(R.id.DELETE_BUTTON)
    Button mDeleteButton;
*/
    //private TrackListAdapter mAdapter;
    private RecyclerView.Adapter mAdapter;  // TODO is this sup  class enough?
    private RecyclerView.LayoutManager mLayoutManager;
    public static String sYearWeekString;


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
        // TODO debug!!!!!
        setDisplayedWeek(LocalDate.now());
        sYearWeekString = "2018-18";    // TODO used by whom?....
        // TODO end debug!!!!!
        initializeAdapter();
        /*
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseOperations.deleteTest();
            }
        });
        */

        // Set button_week_current to current week
        LocalDate nowLocalDate = LocalDate.now();
        mCurrentWeekButton.setText(getWeekNumberYearStringForView(nowLocalDate));
        mCurrentWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(getWeekFrom(mDisplayedWeek).equals(getWeekFrom(LocalDate.now())))) {
                    setDisplayedWeek(LocalDate.now());
                    mCurrentWeekButton.setText(getWeekNumberYearStringForView(mDisplayedWeek));
                    updateRecyclerView();
                }
            }
        });
        mIncreaseWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDisplayedWeek(mDisplayedWeek.plusWeeks(1));
                mCurrentWeekButton.setText(getWeekNumberYearStringForView(mDisplayedWeek));
                updateRecyclerView();
            }
        });
        mDecreaseWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDisplayedWeek(mDisplayedWeek.minusWeeks(1));
                mCurrentWeekButton.setText(getWeekNumberYearStringForView(mDisplayedWeek));
                updateRecyclerView();
            }
        });
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // updateRecyclerView();
    }

    private void updateRecyclerView() {
        mAdapter.notifyDataSetChanged();
    }

    private String getWeekNumberYearStringForView(LocalDate localDate) {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = localDate.get(weekOfYear);
        String format = "%1$02d"; // Two digits
        String formattedWeekNumber = (String.format(format, weekNumber));

        String year = String.valueOf(localDate.getYear());

        String yearWeekString = "Week " + formattedWeekNumber + " (" + year + ")";

        return yearWeekString;
    }

    private String getWeekFrom(LocalDate localDate) {
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return (String.valueOf(localDate.get(weekOfYear)));
    }

    private void initializeAdapter() {
        mAdapter = new TrackListAdapter(getContext(), new TrackListAdapter.TrackListAdapterListener() {
            @Override
            public void button_0_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 0);
            }

            @Override
            public void button_1_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 1);
            }

            @Override
            public void button_2_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 2);
            }

            @Override
            public void button_3_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 3);
            }

            @Override
            public void button_4_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 4);
            }

            @Override
            public void button_5_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 5);
            }

            @Override
            public void button_6_OnClick(View v, int position, EventStateEnum state) {
                processButtonClick(position, state, 6);
            }
        });  // TODO change signature later
        mRecyclerView.setAdapter(mAdapter);
        // Using a linear layout manager
        mLayoutManager = new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Set only if rv doesn't change size // TODO check if this setting is right
        mRecyclerView.setHasFixedSize(true);
        // mAdapter.notifyDataSetChanged(); TODO: needed? (presumably not here)

    }

    private void processButtonClick(int position, EventStateEnum state, int weekday) {
        Goal goal = sGoalList.get(position);
        Goal changedGoal;
        // DANGER: the state committed is the previous state BEFORE clicking.
        switch (state) {
            case PASS:
                changedGoal = GoalHelper.ChangeEventEntryInGoal(goal, EventStateEnum.FAIL, weekday, mDisplayedWeek);
                // this day was a fail! check if goal is failed also now
                calculateStatusOfGoal(changedGoal, EventStateEnum.FAIL, position);
                break;
            case FAIL:
                changedGoal = GoalHelper.ChangeEventEntryInGoal(goal, EventStateEnum.NEUTRAL, weekday, mDisplayedWeek);
                calculateStatusOfGoal(changedGoal, EventStateEnum.NEUTRAL, position);
                break;
            case NEUTRAL:
                changedGoal = GoalHelper.ChangeEventEntryInGoal(goal, EventStateEnum.PASS, weekday, mDisplayedWeek);
                // this day was a pass! check if goal is progressing now
                calculateStatusOfGoal(changedGoal, EventStateEnum.PASS, position);
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

    private void calculateStatusOfGoal(Goal goal, EventStateEnum eventState, int position) {
        //////// region PASSED  ////////////////////////
        if (eventState.equals(EventStateEnum.PASS)) {
            int award = GoalHelper.calculateAward(goal);
            if (award == GoalStatusPseudoEnum.GOLD_EARNED) {
                // Following line is not needed, because we delete the goal 'n turn it into a trophy,
                //  but may be useful in a future version
                goal.setStatus(GoalStatusPseudoEnum.GOLD_EARNED);
                // Invoke parent activity to handle the goal completion. We send the final award also.
                mFragmentInteractionListener.onGoalCompleted(goal, GoalStatusPseudoEnum.GOLD_EARNED_STRING);

            } else if (award == GoalStatusPseudoEnum.SILVER_EARNED) {
                goal.setStatus(GoalStatusPseudoEnum.SILVER_EARNED);
                // TODO inform the GoalsFragment about the changed status of this goal (on more streak, new award status!)
                mFragmentInteractionListener.onGoalChangedByFragment(goal);  // TODO maybe goal is not needed??

            } else if (award == GoalStatusPseudoEnum.BRONZE_EARNED) {
                goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                // TODO inform the GoalsFragment about the changed status of this goal (on more streak, new award status!)
                mFragmentInteractionListener.onGoalChangedByFragment(goal);  // TODO maybe goal is not needed??
            }
            else
            {
                // This a "normal" pass with an increase in streak only
                mFragmentInteractionListener.onGoalChangedByFragment(goal);  // TODO maybe goal ist not needed??
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
                    // TODO trigger a fail! dialog or so
                    // if totally failed, open an dialog with the option to confirm failure or to reset this button!!!



                    int i = 0; // TODO just for breakpoint
                } else if (criticalSum == 1) {
                    // If nearly failed == last day to complete the goal for this week, change the GUI to
                    //  critical for this item
                    // TODO complete this
                }
                // TODO ignoring critical things for now, DEBUG
                // TODO later, a onGoalFailed method will be needed to show fail dialog and remove the goal also,
                // TODO and maybe create a new trophy of bronze or silver



                mFragmentInteractionListener.onGoalChangedByFragment(goal);
                // END HANDLING THE CRITICAL STATES
            }
            else
            {
                // TODO will be a total fail in every case!!! But ask first if this setting is intentionally
                String awardName ="";
                int award = GoalHelper.calculateAward(goal);
                if (award == GoalStatusPseudoEnum.GOLD_EARNED) {
                    goal.setStatus(GoalStatusPseudoEnum.GOLD_EARNED);
                    awardName = GoalStatusPseudoEnum.GOLD_EARNED_STRING;

                } else if (award == GoalStatusPseudoEnum.SILVER_EARNED) {
                    goal.setStatus(GoalStatusPseudoEnum.SILVER_EARNED);
                    awardName = GoalStatusPseudoEnum.SILVER_EARNED_STRING;

                } else if (award == GoalStatusPseudoEnum.BRONZE_EARNED) {
                    goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                    awardName = GoalStatusPseudoEnum.BRONZE_EARNED_STRING;
                }
                else
                {
                    awardName = GoalStatusPseudoEnum.BEGINNER_STRING;
                }

                mFragmentInteractionListener.onGoalFailed(goal);

                AlertDialog alertDialog = AlertDialogFactory.createFailDialog(awardName, getActivity());
                alertDialog.show();

            }
            // if not totally failed, update List
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
}