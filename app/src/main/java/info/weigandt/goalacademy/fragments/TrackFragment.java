package info.weigandt.goalacademy.fragments;

import android.os.Bundle;
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
import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.enums.EventStateEnum;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.GoalHelper;
import info.weigandt.goalacademy.classes.WrapLinearLayoutManager;
import info.weigandt.goalacademy.enums.GoalStatusEnum;
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

                // TODO save the state to Firebase :)

                // we are about to change the setting of monday in the current week
                // so the element we want to change is some member of goal object:
                // private List<Integer> counterCompletedEvents;
                // for ease of processing, the counter should be accessible through the week number and the year
                // deriving from start date and the position is quite too complicated
                //

                // TODO remove these debug lines
                /*
                Goal.WeeklyEventCounter counter = new Goal.WeeklyEventCounter();
                counter.setWeekPassCounter(5);
                counter.setYearWeekString("2014-42");
                List<Goal.WeeklyEventCounter> counterList = new ArrayList<>();
                counterList.add(counter);
                goal.setWeeklyEventCounterList(counterList);

                LocalDate date = LocalDate.now();
                TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                int weekNumber = date.get(weekOfYear);
                Log.e("TEST", String.valueOf(weekNumber));
                */


                //////////////////////////////////////////////////////////

                // TODO process the change here.
                // TODO position gives the track which has been clicked on
                // TODO state shows the desired status on a specific day (here button_0 == monday)
                // TODO View v is WHAT here??? the button, or the surrounding item element????
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
                changedGoal = GoalHelper.ChangeEvent(goal, EventStateEnum.FAIL, weekday, mDisplayedWeek);
                // TODO this day was a fail! check if goal is failed also now
                calculateStatusOfGoal(changedGoal, EventStateEnum.FAIL);

                break;
            case FAIL:
                changedGoal = GoalHelper.ChangeEvent(goal, EventStateEnum.NEUTRAL, weekday, mDisplayedWeek);
                sGoalList.set(position, changedGoal);
                break;
            case NEUTRAL:
                changedGoal = GoalHelper.ChangeEvent(goal, EventStateEnum.PASS, weekday, mDisplayedWeek);
                // TODO this day was a pass! check if goal is progressing now
                calculateStatusOfGoal(changedGoal, EventStateEnum.PASS);

                sGoalList.set(position, changedGoal);
                break;
        }

        // TODO #0: Update FB!
        // TODO also add a ChildEventListener to listen to changes (in the list?)
        // TODO -> only one Goal has to be updated in FB!
        mFragmentInteractionListener.onGoalChangedByFragment(goal, position);

        // TODO #1: Update the GUI!
        // TODO this might be needed to inform the other fragments of changes (e.g. new trophy, removed goal etc.)
        //mFragmentInteractionListener.onDataChangedByFragment();


        // GUI update is needed, because after returning to this week,
        //   old data might reside in the adapter which differs from sGoalList.... hmmm, really?
        // mRecyclerView.getAdapter().notifyItemChanged(position);
        // mRecyclerView.getAdapter().notifyItemRangeRemoved(0, sGoalList.size()-1);


    }

    private int calculateStatusOfGoal(Goal goal, EventStateEnum eventState) {
        if (eventState.equals(EventStateEnum.PASS)) {
            int totalPasses = 0;
            for (Goal.WeeklyEventCounter weeklyEventCounter : goal.getWeeklyEventCounterList()) {
                totalPasses += GoalHelper.calculateNumberOfEvents(weeklyEventCounter.getWeekPassCounter());
            }
            if (totalPasses == Config.NUMBER_FOR_GOLD) {
                // Following line is not needed, because we delete the goal 'n turn it into a trophy,
                //  but may be useful in a future version
                goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                // TODO delete goal n create trophy
            } else if (totalPasses >= Config.NUMBER_FOR_SILVER) {
                goal.setStatus(GoalStatusPseudoEnum.SILVER_EARNED);
                // TODO call update on goals to show correct progress
            } else if (totalPasses >= Config.NUMBER_FOR_BRONZE) {
                goal.setStatus(GoalStatusPseudoEnum.BRONZE_EARNED);
                // TODO call update on goals to show correct progress
            }
        }
        else if (eventState.equals(EventStateEnum.FAIL)) {
            // TODO check for near / total fail now

            // determine number of possible passes this week
            // 7     =   daysLeft + failed days + passed days
            // daysLeft = 7 - failedDays - passedDays
            // if (daysLeft - neededPasses + passedDays) == -1 -> TOTAL FAIL
            // if (daysLeft - neededPasses + passedDays) == 0 -> NEAR FAIL
            // daysLeft = 7 - 3 - 3 = 1
            // neededPasses == 4
            // if (1 - 4 + 3) = 0   -> means Goal is in critical state (no more misses allowed)
            //
            if (goal.getTimesPerWeek() == 0)
            {
                // TODO skip this stuff!
            }
            else
            {
                int passedDays = GoalHelper.calculateNumberOfPasses(goal, mDisplayedWeek);
                int failedDays = GoalHelper.calculateNumberOfFails(goal, mDisplayedWeek);
                int daysLeft = 7 - failedDays - passedDays;
                int criticalSum = daysLeft - goal.getTimesPerWeek() + passedDays;
                if (criticalSum < 0)
                {
                    // TODO trigger a fail!
                    int i = 0; // TODO just for breakpoint
                }
            }



            // if totally failed, open an dialog with the option to confirm failure or to reset this button!!!

            // If nearly failed == last day to complete the goal for this week, change the GUI to
            //  critical for this item

            // if not totally failed, update List
            sGoalList.set(position, changedGoal);
        }



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
        mFragmentInteractionListener = null;    // TODO check if outdated
    }

    @Override
    public void updateViewNotifyGoalInserted() {
        mAdapter.notifyItemInserted(sGoalList.size() - 1);
    }

    @Override
    public void updateViewNotifyGoalChanged(int position) {
        // sGoalList.size() - 1 -> Last position
        mAdapter.notifyItemChanged(position);
        // TODO the view does not care for the status of the list yet.
        // TODO  so each reload will show the standard buttons again.
        // TODO change this now :D
    }
}