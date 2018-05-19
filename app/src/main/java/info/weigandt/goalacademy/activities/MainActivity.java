package info.weigandt.goalacademy.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.BuildConfig;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.FixedTabsFragmentPagerAdapter;
import info.weigandt.goalacademy.classes.Config;
import info.weigandt.goalacademy.classes.Constants;
import info.weigandt.goalacademy.classes.FirebaseOperations;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.GoalHelper;
import info.weigandt.goalacademy.classes.Trophy;
import info.weigandt.goalacademy.data.Quote;
import info.weigandt.goalacademy.data.QuotesContract;
import info.weigandt.goalacademy.fragments.BaseFragment;
import info.weigandt.goalacademy.loader.QuoteAsyncTaskLoader;
import info.weigandt.goalacademy.service.PullQuoteBroadcastReceiver;
import info.weigandt.goalacademy.service.PullQuoteIntentService;
import info.weigandt.goalacademy.widget.GoalAcademyWidgetProvider;
import info.weigandt.goalacademy.widget.WidgetData;
import timber.log.Timber;

import static info.weigandt.goalacademy.classes.Constants.BUNDLE_GOAL_LIST;
import static info.weigandt.goalacademy.classes.Constants.BUNDLE_TROPHY_LIST;

public class MainActivity extends AppCompatActivity
        implements BaseFragment.OnFragmentInteractionListener,
        PullQuoteBroadcastReceiver.BroadcastReceiverListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int RC_SIGN_IN = 0;
    private boolean mIsRestoredFromState = false;
    public FixedTabsFragmentPagerAdapter mFixedTabsFragmentPagerAdapter;
    public static ArrayList<Goal> sGoalList;
    public static ArrayList<Trophy> sTrophyList;
    public static boolean sAreGoalsLoadingFromFirebase = true;
    public static boolean sAreTrophiesLoadingFromFirebase = true;

    // Firebase related
    public static DatabaseReference sGoalsDatabaseReference;
    public static DatabaseReference sTrophiesDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // AsyncTaskLoader related
    public static final int QUOTE_FROM_CONTENT_PROVIDER_LOADER = 23;

    // IntentService related
    private PullQuoteBroadcastReceiver mPullQuoteBroadcastReceiver;

    // FragmentsListStates
    public static Parcelable sTrackFragmentListState;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private ChildEventListener mGoalsEventListener;
    private ChildEventListener mTrophiesEventListener;
    private String mUserId;
    private ArrayList<Goal> mGoalListSave;
    private ArrayList<Trophy> mTrophyListSave;
    private Parcelable mTrackFragmentListStateSave;
    private String test;
    private int mRestoredGoalListSize;
    private int mRestoredTrophyListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLists();
        if (savedInstanceState != null)
        {
            restoreFromSavedInstanceState(savedInstanceState);
        }
        setContentView(R.layout.activity_main);
        AndroidThreeTen.init(this);
        ButterKnife.bind(this);
        initializeTimber();
        initializeTabsAdapter();
        initializeFirebaseAuth();
    }

    private void initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // Online-crash-reporting may be added here later on for release versions
            Timber.plant(new Timber.DebugTree());   // Udacity Rubics asks to add logging
        }
    }

    private void initializeTabsAdapter() {
        // Create an adapter that knows which fragment should be shown on each page
        if (mFixedTabsFragmentPagerAdapter == null)
        {
            mFixedTabsFragmentPagerAdapter = new FixedTabsFragmentPagerAdapter(getSupportFragmentManager(), this, null);
        }
        mViewPager.setAdapter(mFixedTabsFragmentPagerAdapter);
        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void launchPullQuoteIntentService() {
        Intent pullQuoteIntent = new Intent(MainActivity.this, PullQuoteIntentService.class);
        startService(pullQuoteIntent);
    }

    private void loadQuote() {
        if (isOnline())
        {
            launchPullQuoteIntentService();
        }
        else
        {
            startAsyncTaskLoader();
        }
    }

    private void startAsyncTaskLoader() {
        getSupportLoaderManager().initLoader(QUOTE_FROM_CONTENT_PROVIDER_LOADER, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            if (resultCode == RESULT_OK) {
                //
            }
            else if (resultCode == RESULT_CANCELED)
            {
                finish();
            }
        }
    }

    private void initializeFirebaseAuth() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    mUserId = user.getUid();
                    onSignedInInitialize();
                } else {
                    // user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                                .build(),
                        RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialize() {
        if (!mIsRestoredFromState)  {
            showGoalsLoadingIndicators();
            showTrophiesLoadingIndicator();
            loadQuote();
        }
        initializeFirebaseListeners();
        checkForEmptyFirebaseDbs();
        attachFirebaseDbListeners();
    }

    private void checkForEmptyFirebaseDbs() {
        sGoalsDatabaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    sAreGoalsLoadingFromFirebase = false;
                    hideGoalsLoadingIndicators();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(getString(R.string.FIREBASE_ERROR_MESSAGE), databaseError.getMessage());
            }
        });
        sTrophiesDatabaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    sAreTrophiesLoadingFromFirebase = false;
                    hideTrophiesLoadingIndicator();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(getString(R.string.FIREBASE_ERROR_MESSAGE), databaseError.getMessage());
            }
        });
    }

    private void onSignedOutCleanup() {
        detachFirebaseDbListener();
        clearAdapters();
        sendBroadcastToWidget(true);  // Clear widgets also if intentionally logged out
    }

    private void detachFirebaseDbListener() {
        if (mGoalsEventListener != null) {
            sGoalsDatabaseReference.removeEventListener(mGoalsEventListener);
            mGoalsEventListener = null;
        }
        if (mTrophiesEventListener != null) {
            sTrophiesDatabaseReference.removeEventListener(mTrophiesEventListener);
            mTrophiesEventListener = null;
        }
    }

    private void clearAdapters() {
        int sizeGoalList = sGoalList.size();
        sGoalList.clear();
        int sizeTrophyList = sTrophyList.size();
        sTrophyList.clear();
        mFixedTabsFragmentPagerAdapter.clearAdapters(sizeGoalList, sizeTrophyList);
    }

    private void initializeFirebaseListeners() {
        sGoalsDatabaseReference = FirebaseOperations.getFirebase().getReference().child(Config.GOALS_NODE_NAME).child(mUserId);
        sTrophiesDatabaseReference = FirebaseOperations.getFirebase().getReference().child(Config.TROPHIES_NODE_NAME).child(mUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // This will trigger onSigninInitialize() if user is logged in
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sendBroadcastToWidget(true);
        preSaveForOrientationChange();
        removeFirebaseAuthStateListener();
        detachFirebaseDbListener();
        clearAdapters();
        unregisterBroadcastReceiver();
    }

    /**
     * This is needed to save the Lists to survive onPause cleanup before onSaveInstanceState
     */
    private void preSaveForOrientationChange() {
        mGoalListSave = new ArrayList<>(sGoalList);
        mTrophyListSave = new ArrayList<>(sTrophyList);
        mTrackFragmentListStateSave =  sTrackFragmentListState;
    }

    private void removeFirebaseAuthStateListener() {
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    private void initializeLists() {
        sGoalList = new ArrayList<>();
        sTrophyList = new ArrayList<>();
    }

    //================================================================================
    // region Broadcast Receiver
    //================================================================================
    private void registerBroadcastReceiver() {
        if  (mPullQuoteBroadcastReceiver == null)
        {
            mPullQuoteBroadcastReceiver = new PullQuoteBroadcastReceiver(this);
        }
        IntentFilter broadcastFilter = new IntentFilter(PullQuoteBroadcastReceiver.LOCAL_ACTION);
        broadcastFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mPullQuoteBroadcastReceiver, broadcastFilter);
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(mPullQuoteBroadcastReceiver);
    }

    //================================================================================
    // endregion Broadcast Receiver
    //================================================================================

    //// region updateViews ////

    public void updateViewsNotifyGoalInserted() {
        mFixedTabsFragmentPagerAdapter.updateViewsNotifyGoalInserted();
    }

    private void updateViewsNotifyGoalRemoved(int position) {
        mFixedTabsFragmentPagerAdapter.updateViewsNotifyGoalRemoved(position);
    }

    private void updateViewNotifyTrophyInserted() {
        mFixedTabsFragmentPagerAdapter.updateViewNotifyTrophyInserted();
    }

    public void updateViewsNotifyGoalChanged(int foundAtPosition) {
        mFixedTabsFragmentPagerAdapter.updateViewsNotifyGoalUpdated(foundAtPosition);
    }
    private void updateViewQuoteLoaded(String quoteText, String quoteAuthor) {
        if (quoteAuthor.equals("")) {
            quoteAuthor = Config.AUTHOR_FALLBACK_STRING;
        }
        mFixedTabsFragmentPagerAdapter.updateViewNotifyQuoteChanged(quoteText, quoteAuthor);
    }
    //// end region updateViews ////

    // region Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.sign_out_menu:
                // sign out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // endregion Menu
    @Override
    public void onGoalChangedByFragment(Goal goal) {
        FirebaseOperations.UpdateGoal(goal);
    }

    @Override
    public void onGoalFailed(Goal goal) {
        FirebaseOperations.removeGoalFromDatabase(goal);
    }

    @Override
    public void onGoalCompleted(Goal goal, String award) {
        // Create Trophy first, then delete goal
        createTrophyFromCompletedGoal(goal, award);
        FirebaseOperations.removeGoalFromDatabase(goal);
    }

    private void createTrophyFromCompletedGoal(Goal goal, String award) {
        Trophy trophy = new Trophy();
        // see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        String isoDateString = GoalHelper.convertToIsoDate(LocalDate.now());
        trophy.setCompletionDate(isoDateString);
        trophy.setGoalName(goal.getName());
        trophy.setAward(award);
        FirebaseOperations.addTrophyToDatabase(trophy);
    }

    private void attachFirebaseDbListeners() {
        if (mGoalsEventListener == null) {
            mGoalsEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    hideGoalsLoadingIndicators();
                    sAreGoalsLoadingFromFirebase = false;
                    Goal goal = dataSnapshot.getValue(Goal.class);
                    // This prevents this Listener to "overwrite" the restored List
                    if (mIsRestoredFromState && mRestoredGoalListSize > 0)
                    {
                        mRestoredGoalListSize -=1;
                    }
                    else
                    {
                        addGoal(goal);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Goal changedGoal = dataSnapshot.getValue(Goal.class);
                    int foundAtPosition = 0;
                    for (int i = 0; i < sGoalList.size(); i++) {
                        if (sGoalList.get(i).getPushId().equals(changedGoal.getPushId())) {
                            foundAtPosition = i;
                            break;
                        }
                    }
                    sGoalList.set(foundAtPosition, changedGoal);
                    updateGoalsFragmentNotifyGoalChanged(foundAtPosition);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Goal removedGoal = dataSnapshot.getValue(Goal.class);
                    int foundAtPosition = 0;
                    for (int i = 0; i < sGoalList.size(); i++) {
                        if (sGoalList.get(i).getPushId().equals(removedGoal.getPushId())) {
                            foundAtPosition = i;
                            break;
                        }
                    }
                    sGoalList.remove(foundAtPosition);
                    updateViewsNotifyGoalRemoved(foundAtPosition);
                    sendBroadcastToWidget(true);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.e(databaseError.getMessage());
                    Timber.e(databaseError.getDetails());
                }
            };
            sGoalsDatabaseReference.addChildEventListener(mGoalsEventListener);
        }
        if (mTrophiesEventListener == null) {
            mTrophiesEventListener = new ChildEventListener() {

                // "Add Trophy"
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Trophy trophy = dataSnapshot.getValue(Trophy.class);
                    sAreTrophiesLoadingFromFirebase = false;
                    if (trophy.getPushId() == null) {
                        trophy.setPushId(dataSnapshot.getKey());
                    }
                    // This prevents this Listener to "overwrite" the restored List
                    if (mIsRestoredFromState && mRestoredTrophyListSize > 0)
                    {
                        mRestoredTrophyListSize -=1;
                    }
                    else
                    {
                        sTrophyList.add(trophy);
                    }
                    updateViewNotifyTrophyInserted();
                    hideTrophiesLoadingIndicator();
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
                    Timber.e(getString(R.string.FIREBASE_ERROR_MESSAGE, databaseError.getMessage()));
                    Timber.e(getString(R.string.FIREBASE_ERROR_DETAILS, databaseError.getDetails()));
                }
            };
            sTrophiesDatabaseReference.addChildEventListener(mTrophiesEventListener);
        }
    }

    private void hideGoalsLoadingIndicators() {
        if (mFixedTabsFragmentPagerAdapter.trackFragment != null) {
            mFixedTabsFragmentPagerAdapter.trackFragment.hideLoadingIndicator();
        }
        if (mFixedTabsFragmentPagerAdapter.goalsFragment != null) {
            mFixedTabsFragmentPagerAdapter.goalsFragment.hideLoadingIndicator();
        }
    }
    private void showGoalsLoadingIndicators() {
        if (mFixedTabsFragmentPagerAdapter.trackFragment != null) {
            mFixedTabsFragmentPagerAdapter.trackFragment.showLoadingIndicator();
        }
        if (mFixedTabsFragmentPagerAdapter.goalsFragment != null) {
            mFixedTabsFragmentPagerAdapter.goalsFragment.showLoadingIndicator();
        }
    }

    private void hideTrophiesLoadingIndicator() {
        if (mFixedTabsFragmentPagerAdapter.trophiesFragment != null) {
            mFixedTabsFragmentPagerAdapter.trophiesFragment.hideLoadingIndicator();
        }
    }
    private void showTrophiesLoadingIndicator() {
        if (mFixedTabsFragmentPagerAdapter.trophiesFragment != null) {
            mFixedTabsFragmentPagerAdapter.trophiesFragment.showLoadingIndicator();
        }
    }

    /**
     * This method is needed because sGoalList might have been recreated by restoreFromInstanceState
     * @param goalToAdd
     */
    private void addGoal(Goal goalToAdd) {
        for (Goal goal : sGoalList)
        {
            if (goal.getPushId().equals(goalToAdd.getPushId()))
            {
                return;
            }
        }
        sGoalList.add(goalToAdd);
        updateViewsNotifyGoalInserted();
    }

    private void updateGoalsFragmentNotifyGoalChanged(int foundAtPosition) {
        mFixedTabsFragmentPagerAdapter.updateGoalsFragmentNotifyGoalChanged(foundAtPosition);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBroadcastReceived(String quoteText, String quoteAuthor) {
        updateViewQuoteLoaded(quoteText, quoteAuthor);
        if (BuildConfig.DEBUG) {
            Timber.d(getString(R.string.BROADCAST_RECEIVED), quoteText);
        }
    }

    //================================================================================
    // region Cursor Loader and Callbacks for the QuotesContentProvider
    //================================================================================
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new QuoteAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Quote quote = new Quote();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            try {
                String text = cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesEntry.COLUMN_TEXT));
                String author = cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesEntry.COLUMN_AUTHOR));
                updateViewQuoteLoaded(text, author);
                if (BuildConfig.DEBUG) {
                    Timber.d(getString(R.string.LOADED_FROM_CONTENT_RESOLVER), quote.getQuoteText());
                }
            }
            catch (Exception e) {
                Timber.e(getString(R.string.ERROR_PROCESSING_CURSOR), e.toString());
            }
            break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
    /**
     * The Widget cannot access the Firebase Realtime Database, because no authorization can be
     * provided from within the widget (saving the credentials is not an option).
     * So the Widget gets supplied with the needed data until the end of the week in the form of a
     * WidgetData object, which is stored in the the Shared Preferences.
     * This method should be called either to the end of Activity lifecycle (onStop?) or after
     * the data change, or in onResume() maybe
     * @param force
     */
    private void sendBroadcastToWidget(boolean force) {
        if (force || (sGoalList != null && sGoalList.size() > 0))
        {
            WidgetData widgetData = GoalHelper.calculateWidgetData();
            Gson gson = new Gson();
            String serializedWidgetData = gson.toJson(widgetData);
            Intent intent = new Intent(this, GoalAcademyWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            ComponentName name = new ComponentName(getApplicationContext(), GoalAcademyWidgetProvider.class);
            int[] ids = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(name);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            intent.putExtra(Constants.SERIALIZED_WIDGET_DATA, serializedWidgetData);
            sendBroadcast(intent);
        }
    }

    //================================================================================
    // endregion Cursor Loader and Callbacks for the QuotesContentProvider
    //================================================================================

    //================================================================================
    // Saving / restoring the state
    //================================================================================

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String test3 = test;
        outState.putParcelableArrayList(BUNDLE_GOAL_LIST,
                (ArrayList<? extends Parcelable>) mGoalListSave);
        outState.putParcelableArrayList(BUNDLE_TROPHY_LIST,
                (ArrayList<? extends Parcelable>) mTrophyListSave);
        outState.putParcelable(Constants.BUNDLE_TRACK_RECYCLER_LAYOUT, mTrackFragmentListStateSave);
    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        mIsRestoredFromState = true;
        sGoalList  = savedInstanceState.getParcelableArrayList(BUNDLE_GOAL_LIST);
        sTrophyList  = savedInstanceState.getParcelableArrayList(BUNDLE_TROPHY_LIST);
        sTrackFragmentListState = savedInstanceState.getParcelable(Constants.BUNDLE_TRACK_RECYCLER_LAYOUT);
        if (sGoalList != null) {
            mRestoredGoalListSize = sGoalList.size();
        }
        if (sTrophyList != null) {
            mRestoredTrophyListSize = sTrophyList.size();
        }
    }
    //================================================================================
    // END Saving / restoring the state
    //================================================================================
}
