package com.grouptravel.grouptravel;

import android.util.Log;

import java.util.Date;

/**
 * Created by jacek on 28/02/16.
 */
public class DateRange implements Comparable<DateRange> {

    private int startDay, startMonth, startYear, endDay, endMonth, endYear;
    private Date startDate, endDate;

    public DateRange(int startDay, int startMonth, int startYear,
                     int endDay, int endMonth, int endYear) {
        this.startDay = startDay;
        this.startMonth = startMonth;
        this.startYear = startYear;

        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endMonth;

        startDate = new Date(startYear, startMonth, startDay);
        endDate = new Date(endYear, endMonth, endDay);
    }

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void log() {
        Log.e("range : ", "from: " + startDay + "-" + startMonth + "-" + startYear + " to : " + endDay + "-" + endMonth + "-" + endYear);
    }

    @Override
    public int compareTo(DateRange another) {
        return this.toString().compareTo(another.toString());
    }
}
