
package com.grouptravel.grouptravel;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.facebook.login.LoginManager;

public class MainActivity extends ActionBarActivity {

    ActionBar.Tab Tab1, Tab2, Tab3;
    CalendarFragment calendarFragment = new CalendarFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    TravelFragment travelFragment = new TravelFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();

        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);

        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);

        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setText("When?");
        Tab2 = actionBar.newTab().setText("Who?");
        Tab3 = actionBar.newTab().setText("Where?");

        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(calendarFragment));
        Tab2.setTabListener(new TabListener(friendsFragment));
        Tab3.setTabListener(new TabListener(travelFragment));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);

        DataManager.getInstance().init(this, calendarFragment, friendsFragment, travelFragment);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginManager.getInstance().logOut();
    }

    ///////////////////////

    /*

    */


}
