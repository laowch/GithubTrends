package com.laowch.githubtrends.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.laowch.githubtrends.Constants;
import com.laowch.githubtrends.R;
import com.laowch.githubtrends.model.Language;
import com.laowch.githubtrends.utils.AnalyticsHelper;
import com.laowch.githubtrends.utils.IntentUtils;
import com.laowch.githubtrends.utils.LanguageHelper;

public class MainActivity extends BaseActivity implements ActionBar.TabListener {

    final BroadcastReceiver languagesChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };


    ViewPager viewPager;

    DrawerLayout drawerLayout;

    NavigationView navigationView;

    LanguagesPagerAdapter mPagerAdapter;

    TabLayout tabLayout;

    String mTimeSpan = "daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner,
                toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(spinnerContainer, lp);

        SinceSpinnerAdapter spinnerAdapter = new SinceSpinnerAdapter();

        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // daily
                        mTimeSpan = "daily";
                        break;
                    case 1: // weekly
                        mTimeSpan = "weekly";
                        break;
                    case 2: // monthly
                        mTimeSpan = "monthly";
                        break;

                }

                for (int i = 0; i < mPagerAdapter.getCount(); i++) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(mPagerAdapter.getFragmentTag(R.id.pager, i));
                    if (fragment instanceof RepoListFragment) {
                        if (fragment.isAdded()) {
                            ((RepoListFragment) fragment).updateTimeSpan(mTimeSpan);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new LanguagesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_favos:
                        AnalyticsHelper.sendEvent("Main", "Navigation", "Favos", 0l);
                        IntentUtils.openFavos(getContext());
                        break;
                    case R.id.action_custom:
                        AnalyticsHelper.sendEvent("Main", "Navigation", "LangsCustom", 0l);
                        IntentUtils.openCustomLanguage(getContext());
                        break;
                    case R.id.action_add:
                        AnalyticsHelper.sendEvent("Main", "Navigation", "LangsAdd", 0l);
                        IntentUtils.openAddLanguage(getContext());
                        break;
                    case R.id.action_remove:
                        AnalyticsHelper.sendEvent("Main", "Navigation", "LangsRemove", 0l);
                        IntentUtils.openRemoveLanguage(getContext());
                        break;
                    case R.id.action_theme:
                        AnalyticsHelper.sendEvent("Main", "Navigation", "Theme", 0l);
                        ThemeDialog dialog = new ThemeDialog();
                        dialog.show(getSupportFragmentManager(), "theme");
                        break;
                    case R.id.action_about:
                        AnalyticsHelper.sendEvent("Main", "Navigation", "About", 0l);
                        IntentUtils.openAbout(getContext());
                        break;
                }

                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

        IntentFilter languageFilter = new IntentFilter(Constants.ACTION_SELECTED_LANGUAGES_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(languagesChangedReceiver, languageFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(languagesChangedReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void updateView() {
        mPagerAdapter.onLanguagesChange();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_custom:
                IntentUtils.openCustomLanguage(getContext());
                AnalyticsHelper.sendEvent("Main", "Menu", "LangsCustom", 0l);
                return true;
            case R.id.action_favos:
                AnalyticsHelper.sendEvent("Main", "Menu", "Favos", 0l);
                IntentUtils.openFavos(getContext());
                return true;
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                AnalyticsHelper.sendEvent("Main", "Menu", "Drawer", 0l);
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    public class SinceSpinnerAdapter extends BaseAdapter {

        final String[] timeSpanTextArray = new String[]{"Today", "This Week", "This Month"};

        @Override
        public int getCount() {
            return timeSpanTextArray.length;
        }

        @Override
        public String getItem(int position) {
            return timeSpanTextArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView != null ? convertView :
                    getLayoutInflater().inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText("Trends " + getItem(position));
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            View view = convertView != null ? convertView :
                    getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }
    }

    public class LanguagesPagerAdapter extends FragmentPagerAdapter {

        Language[] languagesArray;

        public LanguagesPagerAdapter(FragmentManager fm) {
            super(fm);
            languagesArray = LanguageHelper.getInstance().getSelectedLanguages();
        }

        public void onLanguagesChange() {
            languagesArray = LanguageHelper.getInstance().getSelectedLanguages();
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return RepoListFragment.newInstance(getContext(), languagesArray[position], mTimeSpan);
        }

        @Override
        public int getCount() {
            return languagesArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return languagesArray[position].name;
        }


        public String getFragmentTag(int viewPagerId, int fragmentPosition) {
            return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
        }
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}
