package info.weigandt.goalacademy;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import butterknife.BindView;
import butterknife.ButterKnife;
import info.weigandt.goalacademy.adapters.FixedTabsFragmentPagerAdapter;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    FixedTabsFragmentPagerAdapter mFixedTabsFragmentPagerAdapter;
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tablayout) TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Butterknife
        ButterKnife.bind(this);

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

    }
}
