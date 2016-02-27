
package com.grouptravel.grouptravel;



import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends ActionBarActivity {

    ActionBar.Tab Tab1, Tab2, Tab3;
    Fragment fragmentTab1 = new CalendarFragment();
    Fragment fragmentTab2 = new FriendsFragment();
    Fragment fragmentTab3 = new GroupsFragment();

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
        Tab1 = actionBar.newTab().setText("Tab1");
        Tab2 = actionBar.newTab().setText("Tab2");
        Tab3 = actionBar.newTab().setText("Tab3");

        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        Tab2.setTabListener(new TabListener(fragmentTab2));
        Tab3.setTabListener(new TabListener(fragmentTab3));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);

    }



    ///////////////////////

    /*
    private class DateRange implements Comparable<DateRange>{
        private int startDay, startMonth, startYear, endDay, endMonth, endYear;
        public DateRange(int startDay, int startMonth, int startYear,
                         int endDay, int endMonth, int endYear) {
            this.startDay = startDay;
            this.startMonth = startMonth;
            this.startYear = startYear;

            this.endDay = endDay;
            this.endMonth = endMonth;
            this.endYear = endMonth;
        }

        public void log() {
            Log.e("range : ","from: "+startDay+"-"+startMonth+"-"+startYear+" to : "+endDay+"-"+endMonth+"-"+endYear );
        }

        @Override
        public int compareTo(DateRange another) {
            if(this.hashCode() == another.hashCode()) return 0;
            return this.hashCode() < another.hashCode() ? -1 : 1;
        }
    }
    */


}
