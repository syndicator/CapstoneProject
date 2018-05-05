package info.weigandt.goalacademy.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.BuildConfig;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.FixedTabsFragmentPagerAdapter;
import info.weigandt.goalacademy.classes.FirebaseOperations;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.Trophy;
import info.weigandt.goalacademy.fragments.BaseFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {
    public FixedTabsFragmentPagerAdapter mFixedTabsFragmentPagerAdapter;
    public static ArrayList<Goal> sGoalList;
    public static ArrayList<Trophy> sTrophyList;
    // Firebase related
    public static DatabaseReference sGoalsDatabaseReference; // TODO: Adjust to correct node. -> include users
    public static DatabaseReference sTrophiesDatabaseReference;
    public static FirebaseDatabase sFirebaseDatabase;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private ChildEventListener mGoalsEventListener;
    private ChildEventListener mTrophiesEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ThreeTen Android Backport: Includes Java8 java.time features to replace outdated Java7 time / date classes
        AndroidThreeTen.init(this);

        // Initialize Butterknife
        ButterKnife.bind(this);
        fillGoalListWithDummyData(); // TODO remove this debug line
        fillTrophyListWithDummyData();
        // Initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Online-crash-reporting may be added here later on for release versions
        }
        // Create an adapter that knows which fragment should be shown on each page
        mFixedTabsFragmentPagerAdapter = new FixedTabsFragmentPagerAdapter(getSupportFragmentManager(), this);
        // Set the adapter onto the view pager
        mViewPager.setAdapter(mFixedTabsFragmentPagerAdapter);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        mTabLayout.setupWithViewPager(mViewPager);

        // TODO maybe needed at some point?
        //adapter.startUpdate(viewPager);
        //tab0 = (Fragment0) adapter.instantiateItem(viewPager, 0);
        //tab1 = (Fragment1) adapter.instantiateItem(viewPager, 1);
        //adapter.finishUpdate(viewPager);
        initializeFirebase();
    }

    public void updateViewsNotifyGoalInserted() {
        mFixedTabsFragmentPagerAdapter.updateViewNotifyGoalInserted();
    }

    private void fillTrophyListWithDummyData() {
        sTrophyList = new ArrayList<>();
        /*
        Trophy trophy = new Trophy();
        trophy.setGoalName("Go running every day!");
        trophy.setCompletionDate(LocalDateTime.now());
        sTrophyList.add(trophy);
        Trophy trophy2 = new Trophy();
        trophy.setGoalName("Drink water daily!");
        trophy2.setCompletionDate(LocalDateTime.of(1950, 12, 24, 10, 30));
        sTrophyList.add(trophy2);
        */
    }

    private void fillGoalListWithDummyData() {
        sGoalList = new ArrayList<>();
    }

    @Override
    public void onDataChangedByFragment() {
        mFixedTabsFragmentPagerAdapter.updateViewNotifyGoalInserted();
    }

    @Override
    public void onGoalChangedByFragment(Goal goal, int position) {
        FirebaseOperations.UpdateGoal(goal);
        // TODO this should only be called if a goal gets removed (completed, failed).
        // TODO  otherwise the buttons act weird (change due to button behaviour and due to view update)
        // mFixedTabsFragmentPagerAdapter.updateViewNotifyGoalUpdated(position);
    }

    @Override
    public void onGoalCompleted(Goal goal, int position, String award) {
        // Create Trophy first, then delete goal
        Trophy trophy = new Trophy();
        LocalDate date = LocalDate.now();
        // see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        String isoDateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        trophy.setCompletionDate(isoDateString);
        trophy.setGoalName(goal.getName());
        trophy.setAward(award);
    }

    // TODO change method name
    @Override
    public void onGoalProgressChanged(Goal goal, int position) {
        mFixedTabsFragmentPagerAdapter.updateViewsNotifyGoalUpdated(position);
    }

    private void initializeFirebase() {
        // initial loading of the goalList to be ready for adapter!
        sFirebaseDatabase = FirebaseDatabase.getInstance();
        sGoalsDatabaseReference = sFirebaseDatabase.getReference().child("goals");   // TODO save under node of userID or so
        sTrophiesDatabaseReference = sFirebaseDatabase.getReference().child("trophies");   // TODO save under node of userID or so
        mGoalsEventListener = new ChildEventListener() {

            // "New Goal"
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Goal goal = dataSnapshot.getValue(Goal.class);
                if (goal.getPushId() == null) {
                    goal.setPushId(dataSnapshot.getKey());
                }
                // change the sGoalList now and then call:
                // mAdapter.notifyItemInserted(mItems.size() - 1);
                //  issues.remove(position);
                //                    notifyItemRemoved(position);
                //                    //this line below gives you the animation and also updates the
                //                    //list items after the deleted item
                //                    notifyItemRangeChanged(position, getItemCount());
                sGoalList.add(goal);
                updateViewsNotifyGoalInserted();  // TODO check if fragment list not null in subclass tab....
                // TODO replace with proper method (inserted instead of former general update)
            }

            // Change Goal?
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        sGoalsDatabaseReference.addChildEventListener(mGoalsEventListener);

        //// Trophies
        mTrophiesEventListener = new ChildEventListener() {

            // "Add Trophy"
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Trophy trophy = dataSnapshot.getValue(Trophy.class);
                if (trophy.getPushId() == null) {
                    trophy.setPushId(dataSnapshot.getKey());
                }
                // change the sGoalList now and then call:
                // mAdapter.notifyItemInserted(mItems.size() - 1);
                //  issues.remove(position);
                //                    notifyItemRemoved(position);
                //                    //this line below gives you the animation and also updates the
                //                    //list items after the deleted item
                //                    notifyItemRangeChanged(position, getItemCount());
                sTrophyList.add(trophy);
                updateViewNotifyTrophyInserted();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };
        sTrophiesDatabaseReference.addChildEventListener(mTrophiesEventListener);
    }

    private void updateViewNotifyTrophyInserted() {
        mFixedTabsFragmentPagerAdapter.updateViewNotifyTrophyInserted();
    }
}
