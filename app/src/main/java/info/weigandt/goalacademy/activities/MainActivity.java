package info.weigandt.goalacademy.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.BuildConfig;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.FixedTabsFragmentPagerAdapter;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.Trophy;
import info.weigandt.goalacademy.fragments.BaseFragment;
import info.weigandt.goalacademy.fragments.GoalsFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener
{
    public FixedTabsFragmentPagerAdapter mFixedTabsFragmentPagerAdapter;
    public static ArrayList<Goal> sGoalList;
    public static ArrayList<Trophy> sTrophyList;
    // Firebase related
    public static FirebaseDatabase sFirebaseDatabase;
    public static DatabaseReference sDatabaseReference;   // TODO: Adjust to correct node. users?

    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tablayout) TabLayout mTabLayout;
    public static DatabaseReference sGoalsDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private ChildEventListener mGoalsEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ThreeTen Android Backport: Includes Java8 java.time features to replace outdated Java7 time / date classes
        AndroidThreeTen.init(this);

        // Initialize Butterknife
        ButterKnife.bind(this);
        fillGoalListWithDummyData();
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

    public void updateViews()
    {
        mFixedTabsFragmentPagerAdapter.updateViews();
    }

    private void fillTrophyListWithDummyData() {
        sTrophyList = new ArrayList<>();
        Trophy trophy = new Trophy();
        trophy.setGoalName("Go running every day!");
        trophy.setCompletionDate(LocalDateTime.now());
        sTrophyList.add(trophy);
        Trophy trophy2 = new Trophy();
        trophy.setGoalName("Drink water daily!");
        trophy2.setCompletionDate(LocalDateTime.of(1950,12,24, 10,30));
        sTrophyList.add(trophy2);
    }

    private void fillGoalListWithDummyData() {
        sGoalList = new ArrayList<>();
        Goal goal = new Goal();
        goal.setName("Go running every day!");
        sGoalList.add(goal);
        Goal goal2 = new Goal();
        goal2.setName("Drink water daily.");
        sGoalList.add(goal2);
    }

    @Override
    public void onDataChangedByFragment() {
        mFixedTabsFragmentPagerAdapter.updateViews();
    }
    private void initializeFirebase() {
        // initial loading of the goalList to be ready for adapter!
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        sGoalsDatabaseReference = mFirebaseDatabase.getReference().child("goals");   // TODO save under node of userID or so
        mGoalsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // TODO:  DataSnapshot has to be retrieved before already to initially fill the list
                Goal goal = dataSnapshot.getValue(Goal.class);
                // change the sGoalList now and then call:
                // mAdapter.notifyItemInserted(mItems.size() - 1);
                //  issues.remove(position);
                //                    notifyItemRemoved(position);
                //                    //this line below gives you the animation and also updates the
                //                    //list items after the deleted item
                //                    notifyItemRangeChanged(position, getItemCount());
                sGoalList.add(goal);
                /* only needed in fragment
                if (mFragmentInteractionListener != null)
                {
                    mFragmentInteractionListener.onDataChangedByFragment();
                }
                */
                updateViews();  // TODO check if fragment list not null in subclass tab....
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
        sGoalsDatabaseReference.addChildEventListener(mGoalsEventListener);
    }
}
