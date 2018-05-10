package info.weigandt.goalacademy.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.BuildConfig;
import info.weigandt.goalacademy.R;
import info.weigandt.goalacademy.adapters.FixedTabsFragmentPagerAdapter;
import info.weigandt.goalacademy.classes.FirebaseOperations;
import info.weigandt.goalacademy.classes.Goal;
import info.weigandt.goalacademy.classes.Trophy;
import info.weigandt.goalacademy.data.Quote;
import info.weigandt.goalacademy.data.QuotesContract;
import info.weigandt.goalacademy.fragments.BaseFragment;
import info.weigandt.goalacademy.service.PullQuoteBroadcastReceiver;
import info.weigandt.goalacademy.service.PullQuoteIntentService;
import timber.log.Timber;

import static info.weigandt.goalacademy.classes.Constants.BUNDLE_GOAL_LIST;
import static info.weigandt.goalacademy.classes.Constants.BUNDLE_TROPHY_LIST;

public class MainActivity extends AppCompatActivity
        implements BaseFragment.OnFragmentInteractionListener,
        PullQuoteBroadcastReceiver.BroadcastReceiverListener, LoaderManager.LoaderCallbacks<Quote> {
    private static final int RC_SIGN_IN = 0;
    private boolean mIsRestoredFromState = false;
    public FixedTabsFragmentPagerAdapter mFixedTabsFragmentPagerAdapter;
    public static ArrayList<Goal> sGoalList;
    public static ArrayList<Trophy> sTrophyList;

    // Firebase related
    public static DatabaseReference sGoalsDatabaseReference;
    public static DatabaseReference sTrophiesDatabaseReference;
    public static FirebaseDatabase sFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // AsyncTaskLoader related
    public static final int QUOTE_FROM_CONTENT_PROVIDER_LOADER = 23;
    public static final String OPERATION_URL_EXTRA = "url_that_return_json_data";   // TODO change

    // IntentService related
    private PullQuoteBroadcastReceiver mPullQuoteBroadcastReceiver;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private ChildEventListener mGoalsEventListener;
    private ChildEventListener mTrophiesEventListener;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // RESTORING THE STATE
        if (savedInstanceState != null)
        {
            //restoreFromSavedInstanceState(savedInstanceState);
        }
        setContentView(R.layout.activity_main);
        // ThreeTen Android Backport: Includes Java8 java.time features to replace outdated Java7 time / date classes
        AndroidThreeTen.init(this);

        // Initialize Butterknife
        ButterKnife.bind(this);
        initializeLists();
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

        // Initialize Firebase components
        initializeFirebaseAuth();
        //loadQuote();
        // quote loading moved to: onSignedInInitialize (only if signed in!)
    }



    private void launchPullQuoteIntentService() {
        Intent pullQuoteIntent = new Intent(MainActivity.this, PullQuoteIntentService.class);
        registerBroadcastReceiver();
        startService(pullQuoteIntent);
    }

    private Quote getQuoteFromContentResolver() {
        Uri uri = QuotesContract.QuotesEntry.CONTENT_URI;
        Quote quote = new Quote();
        try {
            Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String text = cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesEntry.COLUMN_TEXT));
                String author = cursor.getString(cursor.getColumnIndex(QuotesContract.QuotesEntry.COLUMN_AUTHOR));
                quote.setQuoteText(text);
                quote.setQuoteAuthor(author);
                Timber.e("LOADED FROM CONTENT RESOLVER" + quote.getQuoteText());
                return quote;   // TODO careful here, we leave the cursor iteration on first iteration
            }
        } catch (Exception e) {
            Timber.e(e.getMessage());
            e.printStackTrace();
        }
        return quote;
    }

    private void loadQuote() {
        if (!isOnline())
        {
            startAsyncTaskLoader();
        }
        else
        {
            launchPullQuoteIntentService();
        }
    }

    private void startAsyncTaskLoader() {
        // TODO expand to use a member var for the loader if triggered several times
        // TODO  see https://medium.com/@sanjeevy133/an-idiots-guide-to-android-asynctaskloader-76f8bfb0a0c0
        // TODO  paragraph 6.
        getSupportLoaderManager().initLoader(QUOTE_FROM_CONTENT_PROVIDER_LOADER, null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(resultCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in.", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Sign in canceled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initializeFirebaseAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    mUserId = user.getUid();
                    onSignedInInitialize();
                    Toast.makeText(MainActivity.this, "You are now signed in to Goal Academy.", Toast.LENGTH_SHORT).show();
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
        initializeFirebaseDb();
        attachFirebaseDbListener();
         loadQuote();
    }

    private void onSignedOutCleanup() {
        clearAdapters();
        detachFirebaseDbListener();
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
        final int sizeGoalList = sGoalList.size();
        sGoalList.clear();
        final int sizeTrophyList = sTrophyList.size();
        sTrophyList.clear();
        mFixedTabsFragmentPagerAdapter.clearAdapters(sizeGoalList, sizeTrophyList);
    }

    private void initializeFirebaseDb() {
        sFirebaseDatabase = FirebaseDatabase.getInstance();
        sGoalsDatabaseReference = sFirebaseDatabase.getReference().child("goals").child(mUserId);
        sTrophiesDatabaseReference = sFirebaseDatabase.getReference().child("trophies").child(mUserId);
    }


    /**
     * This method is called after this activity has been paused or restarted
     * Only needed for favorites, because this list can change by adding/removing from favorites
     */
    @Override
    protected void onResume() {
        super.onResume();
        // This will trigger onSigninInitialize() if user is logged in
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        registerBroadcastReceiver();

         /*
        if (mSelectionMode == INT_MODE_FAVORITES)
        {
            getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER_ID, null, this);
            int i = 1;
        }
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachFirebaseDbListener();
        clearAdapters();
        unregisterBroadcastReceiver();
    }

    // region BroadcastReceiver

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
        /*
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(mPullQuoteBroadcastReceiver);
        */
    }

    // endregion BroadcastReceiver


    private void initializeLists() {
        sGoalList = new ArrayList<>();
        sTrophyList = new ArrayList<>();
    }

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
        LocalDate date = LocalDate.now();
        // see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        String isoDateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        trophy.setCompletionDate(isoDateString);
        trophy.setGoalName(goal.getName());
        trophy.setAward(award);
        FirebaseOperations.addTrophyToDatabase(trophy);
    }

    private void attachFirebaseDbListener() {
        if (mGoalsEventListener == null) {
            mGoalsEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Goal goal = dataSnapshot.getValue(Goal.class);
                    sGoalList.add(goal);
                    updateViewsNotifyGoalInserted();  // TODO check if fragment list not null in subclass tab....
                    // TODO replace with proper method (inserted instead of former general update)

                    // change the sGoalList now and then call:
                    // mAdapter.notifyItemInserted(mItems.size() - 1);
                    //  issues.remove(position);
                    //                    notifyItemRemoved(position);
                    //                    //this line below gives you the animation and also updates the
                    //                    //list items after the deleted item
                    //                    notifyItemRangeChanged(position, getItemCount());
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
                    // updateViewsNotifyGoalChanged(foundAtPosition);
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
                    Timber.e(databaseError.getMessage());
                    Timber.e(databaseError.getDetails());
                }

            };
            sTrophiesDatabaseReference.addChildEventListener(mTrophiesEventListener);
        }
    }

    private void updateGoalsFragmentNotifyGoalChanged(int foundAtPosition) {
        mFixedTabsFragmentPagerAdapter.updateGoalsFragmentNofifyGoalChanged(foundAtPosition);
    }

    /* TODO check this for processing. method has been moved to service
    @Override
    public void onPostApiCall(Quote quote) {
        if (quote != null) {
            if (mIsRestoredFromState == false)
            {
                saveRetrofitResponseToContentProvider(quote);
            }
            else
            {

            }
            //String quoteText = quote.getQuoteText();
            //String quoteAuthor = quote.getQuoteAuthor();

            // TODO move following line to broadcast receiver!
        } else {
            // showMainErrorMessage(); TODO handle error / log etc...
            int i = 0;
        }
    }
    */

    // region Content Provider

    private void saveRetrofitResponseToContentProvider(Quote quote) {
        // TODO implement. Old code below.
        /*
        JSONObject root = null;
        JSONArray resultsArray = null;
        try {
            ArrayList<MovieResult.Movie> moviesList = movieResult.getItems();
            mPosterList = new ArrayList<String>();
            mMovieObjects = new ArrayList<JSONObject>();

            for (MovieResult.Movie movie : moviesList) {

                String url = NetworkHelper.buildPosterUrl(movie.getMoviePoster()).toString();
                mPosterList.add(url);
                JSONObject json = new JSONObject();
                json.put(NetworkHelper.PROPERTY_POSTER_PATH, movie.getMoviePoster());
                json.put(NetworkHelper.PROPERTY_OVERVIEW, movie.getMoviePlot());
                json.put(NetworkHelper.PROPERTY_RELEASE, movie.getMovieRelease());
                json.put(NetworkHelper.PROPERTY_ID, movie.getId());
                json.put(NetworkHelper.PROPERTY_TITLE, movie.getMovieTitle());
                json.put(NetworkHelper.PROPERTY_RATING, movie.getMovieRating());
                mMovieObjects.add(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }
    // endregion Content Provider

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBroadcastReceived(String quoteText, String quoteAuthor) {
        // TODO handle view updating from here.
        updateViewQuoteLoaded(quoteText, quoteAuthor);
        Timber.e("BROADCAST received." + quoteText);
    }

    //================================================================================
    // region Cursor Loader and Callbacks for the QuotesContentProvider
    //================================================================================
    @SuppressLint("StaticFieldLeak")    // ...why u lint me?
    @NonNull
    @Override
    public Loader<Quote> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Quote>(this) {
            Quote cacheQuote;   // keep the quote here as long as already retrieved via HTTP

            @Override
            protected void onStartLoading() {
                // Think of this as AsyncTask onPreExecute() method,
                //  you can start a progress bar and at the end call forceLoad();
                if (cacheQuote!=null) {
                    // To skip loadInBackground call
                    deliverResult(cacheQuote);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(Quote quote) {    // Quick delivery if cached
                cacheQuote = quote;
                super.deliverResult(quote);
            }

            @Override
            public Quote loadInBackground() {
                // Think of this as AsyncTask doInBackground() method,
                //  here you will actually initiate Network call,
                //  or any work that need to be done on background
                return getQuoteFromContentResolver();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Quote> loader, Quote quote) {
        updateViewQuoteLoaded(quote.getQuoteText(), quote.getQuoteAuthor());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Quote> loader) {

    }
    //================================================================================
    // endregion Cursor Loader and Callbacks for the QuotesContentProvider
    //================================================================================


    //================================================================================
    // Saving / restoring the state
    //================================================================================

    private ArrayList<String> convertToStringArray(ArrayList<Goal> goalList)
    {
        ArrayList<String> stringList = new ArrayList<>();
        Gson gson = new Gson();
        for (Goal goal : goalList) {
            stringList.add(gson.toJson(goal));
        }
        return stringList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
/*
        outState.putParcelableArrayList(BUNDLE_GOAL_LIST,
                (ArrayList<? extends Parcelable>) sGoalList);
        outState.putParcelableArrayList(BUNDLE_TROPHY_LIST,
                (ArrayList<? extends Parcelable>) sTrophyList);

        // TODO Save list state <- fragment?
        //mListState = mTabLayout.onSaveInstanceState();
        //outState.putParcelable(LIST_STATE_KEY, mListState);
        */
        super.onSaveInstanceState(outState);
    }

    private ArrayList<JSONObject> convertToJsonArray(ArrayList<String> stringList)
    {
        ArrayList<JSONObject> jsonList= new ArrayList<>();
        for (String movie:stringList) {
            try {
                JSONObject json =new JSONObject(movie);
                jsonList.add(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonList;
    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        if (sFirebaseDatabase == null)
        {
            // initializeFirebaseDb(); // TODO check if needed
        }
        // recreate in onCreate:
        sGoalList  = savedInstanceState.getParcelableArrayList(BUNDLE_GOAL_LIST);
        sTrophyList  = savedInstanceState.getParcelableArrayList(BUNDLE_TROPHY_LIST);

        // TODO - in fragment? - Retrieve list state and list/item positions

        //mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        //mIsRestoredFromState = true;
    }

    //================================================================================
    // END Saving / restoring the state
    //================================================================================


}
