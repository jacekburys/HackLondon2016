package com.grouptravel.grouptravel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jacek on 27/02/16.
 */
public class CalendarFragment extends Fragment {


    private Button buttonFree, buttonBusy;
    private CalendarPickerView calendarView;

    private Set<Date> dates;
    private Date _startDate;
    private Date _endDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_layout, container, false);


        dates = new TreeSet<>();

        buttonFree = (Button)rootView.findViewById(R.id.buttonFree);
        buttonBusy = (Button)rootView.findViewById(R.id.buttonBusy);
        calendarView = (CalendarPickerView)rootView.findViewById(R.id.calendarView);

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

        return rootView;
    }



    private void addRange() {
        dates.addAll(calendarView.getSelectedDates());
        updateCalendarView(calendarView.getSelectedDates().get(0));
    }

    private void removeRange() {
        dates.removeAll(calendarView.getSelectedDates());
        updateCalendarView(calendarView.getSelectedDates().get(0));
    }

    private void updateCalendarView(Date today) {
        calendarView.init(_startDate, _endDate)
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        calendarView.highlightDates(dates);
    }
}
