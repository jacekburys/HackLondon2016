package com.grouptravel.grouptravel;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;


public class DataManager {

    private static final DataManager instance = new DataManager();

    private Context context;
    private RequestQueue requestQueue;
    private CalendarFragment calendarFragment;
    private FriendsFragment friendsFragment;
    private TravelFragment travelFragment;

    public static DataManager getInstance() {
        return instance;
    }

    private HashMap<String, String> countryMap;

    private DataManager() {
        countryMap = new HashMap<>();
        countryMap.put("RO", "Romania");
        countryMap.put("PL", "Poland");
        countryMap.put("US", "United States");
        countryMap.put("GER", "Germany");
        countryMap.put("FR", "France");
    }

    public void init(Context context, CalendarFragment calendarFragment,
                     FriendsFragment friendsFragment, TravelFragment travelFragment) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
        this.calendarFragment = calendarFragment;
        this.friendsFragment = friendsFragment;
        this.travelFragment = travelFragment;
    }

    public void getDateRanges(String userId) {
        final ArrayList<DateRange> list = new ArrayList<>();

        final String URL = "https://group-travel.herokuapp.com/api/get/user/" + userId;
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ranges", response.toString());
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            JSONArray arr;
                            if(response.has("value")){
                                arr = response.getJSONObject("value").getJSONObject("user").getJSONArray("free_dates");
                            }else{
                                arr = response.getJSONObject("user").getJSONArray("free_dates");

                            }
                            for(int i=0; i<arr.length(); i++){
                                String start = arr.getJSONArray(i).getString(0);
                                String end   = arr.getJSONArray(i).getString(1);
                                Date endDate = new Date();
                                Date startDate= new Date();
                                try {
                                    endDate = dateFormat.parse(end);
                                    startDate = dateFormat.parse(start);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                list.add(new DateRange(startDate, endDate));

                            }
                        } catch (JSONException e){
                        }


                        calendarFragment.updateCalendarView(list);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        requestQueue.add(req);
    }

    public void updateDatesOnServer(String userId, Set<Date> dates) {
        if(dates.isEmpty()) return;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ArrayList<Date> arr = new ArrayList<>(dates);

        Collections.sort(arr);

        for(Date d : arr) {
            Log.d("sorted", d.toString());
        }

        GregorianCalendar start = null;
        GregorianCalendar end = null;

        ArrayList<String> strings = new ArrayList<>();

        GregorianCalendar c1 = null;
        GregorianCalendar c2 = null;
        for(int i = 1; i<arr.size(); i++) {
            c1 = new GregorianCalendar();
            c2 = new GregorianCalendar();
            Date d1 = arr.get(i-1);
            Date d2 = arr.get(i);
            c1.setTime(d1);
            c2.setTime(d2);

            if(start == null) {
                start = c1;
                end = c2;
                if(!isNextDay(c1, c2)) {
                    end = c1;
                    strings.add(dateFormat.format(start.getTime()));
                    strings.add(dateFormat.format(end.getTime()));
                    //Log.d("date ranges sta", dateFormat.format(start.getTime()));
                    //Log.d("date ranges end", dateFormat.format(end.getTime()));

                    start = null;
                    end = null;
                }
            } else if(!isNextDay(c1, c2)) {
                end = c1;
                strings.add(dateFormat.format(start.getTime()));
                strings.add(dateFormat.format(end.getTime()));
                //Log.d("date ranges sta", dateFormat.format(start.getTime()));
                //Log.d("date ranges end", dateFormat.format(end.getTime()));

                start = null;
                end = null;
            } else {
                end = c2;
            }

        }
        if(start != null && end != null){
            strings.add(dateFormat.format(start.getTime()));
            strings.add(dateFormat.format(end.getTime()));
            //Log.d("date ranges sta", dateFormat.format(start.getTime()));
            //Log.d("date ranges end", dateFormat.format(end.getTime()));
        }


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for(int i=0; i<strings.size(); i+=2) {
            String s1 = strings.get(i);
            String s2 = strings.get(i+1);

            stringBuilder.append("[\"" + s1 + "\",\"" + s2 + "\"]");
            if(i < strings.size() - 2){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");

        final String URL = "https://group-travel.herokuapp.com/api/update/user/" + userId + "/" + stringBuilder.toString();
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("update", "success");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        Log.d("updatelink", URL);

        requestQueue.add(req);
    }

    private boolean isNextDay(GregorianCalendar date, GregorianCalendar next) {
        GregorianCalendar temp = new GregorianCalendar();
        temp.setTime(date.getTime());
        temp.add(GregorianCalendar.DATE, 1);
        return temp.equals(next);
    }

    public void getIntersection(){

        StringBuilder sb = new StringBuilder();

        ArrayList<Friend> friends = friendsFragment.getFriendsList();

        sb.append("[");

        sb.append("\"" + Profile.getCurrentProfile().getId() + "\"");
        if(!friends.isEmpty()){
            sb.append(",");
        }

        for(int i=0; i<friends.size(); i++) {
            sb.append("\"" + friends.get(i).getId() + "\"");
            if(i < friends.size() - 1){
                sb.append(",");
            }
        }
        sb.append("]");


        final String URL = "https://group-travel.herokuapp.com/api/get/intersection/" + sb.toString();
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("update", "success");
                        Log.d("intersection", response.toString());
                        String start = "";
                        String end = "";
                        try{
                            start = response.getString("start");
                            end = response.getString("end");
                        }catch(Exception e){

                        }

                        friendsFragment.updateIntersection("You are all free on:\n" + start + " - " + end);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        Log.d("updatelink", URL);
        requestQueue.add(req);
    }




    public void getFlight(){

        StringBuilder sb = new StringBuilder();

        final String URL = "https://group-travel.herokuapp.com/api/get/price";
        // pass second argument as "null" for GET requests
        JsonObjectRequest req = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("update", "success");
                        Log.d("price", response.toString());
                        String min = "";
                        String country = "";
                        try{
                            min = response.getString("min");
                            country = response.getString("country");
                        }catch(Exception e){

                        }

                        travelFragment.updateFlight(countryMap.get(country) + " - " + min + " GBP");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        Log.d("updatelink", URL);
        requestQueue.add(req);
    }
}
