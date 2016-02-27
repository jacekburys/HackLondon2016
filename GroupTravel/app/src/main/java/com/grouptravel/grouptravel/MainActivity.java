
package com.grouptravel.grouptravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements DateRangePickerFragment.OnDateRangeSelectedListener {

    private Button buttonFree, buttonBusy;
    private CalendarPickerView calendarView;

    private Set<Date> dates;
    private Date _startDate;
    private Date _endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dates = new TreeSet<>();

        setContentView(R.layout.activity_main);
        buttonFree = (Button)findViewById(R.id.buttonFree);
        buttonBusy = (Button)findViewById(R.id.buttonBusy);
        calendarView = (CalendarPickerView)findViewById(R.id.calendarView);

        buttonFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRange();
            }
        });

        buttonBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRange();
            }
        });

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Date today = new Date();
        calendarView.init(today, nextYear.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        _startDate = today;
        _endDate = nextYear.getTime();
    }

    private void addRange() {
        dates.addAll(calendarView.getSelectedDates());
        updateCalendarView(calendarView.getSelectedDates().get(0));
    }

    private void removeRange() {
        dates.removeAll(calendarView.getSelectedDates());
        updateCalendarView(calendarView.getSelectedDates().get(0));
    }



    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {

        // TODO: replace with the call to the server
        //DateRange dateRange = new DateRange(startDay, startMonth, startYear, endDay, endMonth, endYear);
        //dateRange.log();

        Calendar calendar = new GregorianCalendar(startYear, startMonth, startDay);
        Date startDate = calendar.getTime();
        Calendar calendarEnd = new GregorianCalendar(endYear, endMonth, endDay);

        while(!calendar.after(calendarEnd)) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(calendar.getTime());

        updateCalendarView(startDate);
    }

    private void updateCalendarView(Date today) {
        calendarView.init(_startDate, _endDate)
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        calendarView.highlightDates(dates);
    }


    ///////////////////////

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getRange() {
        DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(MainActivity.this,false);
        dateRangePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
