package com.grouptravel.grouptravel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jacek on 27/02/16.
 */
public class FriendsFragment extends Fragment {

    MyCustomAdapter dataAdapter = null;

    ArrayList<Friend> friendsList;
    View rootView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list, container, false);
        textView = (TextView)rootView.findViewById(R.id.textIntersection);
        getFriends();
        return rootView;
    }

    private void setAdapter(ArrayList<Friend> friendsList) {
        //create an ArrayAdaptar from the String Array
        this.friendsList = friendsList;
        dataAdapter = new MyCustomAdapter(getContext(),
                R.layout.friend_item, friendsList);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private void getFriends(){

        final ArrayList<Friend> friends = new ArrayList<>();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try{
                            JSONArray arr = response.getJSONObject().getJSONArray("data");
                            for(int i=0; i<arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                Log.d("friend", obj.toString());
                                String name = obj.get("name").toString();
                                String id = obj.get("id").toString();
                                Log.d("friend added", name + " - - " + id);
                                Friend friend = new Friend(name, id);
                                friends.add(friend);
                            }

                            setAdapter(friends);
                            friendsList = friends;

                        } catch(JSONException e){

                        }
                    }
                }
        ).executeAsync();


    }

    public void updateIntersection(String intersection) {
        final String temp = intersection;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(temp);
            }
        });
    }

    public ArrayList<Friend> getFriendsList() {
        return friendsList;
    }


    private class MyCustomAdapter extends ArrayAdapter<Friend> {

        private ArrayList<Friend> friendsList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Friend> friendsList) {
            super(context, textViewResourceId, friendsList);
            this.friendsList = new ArrayList<Friend>();
            this.friendsList.addAll(friendsList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.friend_item, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Friend friend = (Friend) cb.getTag();
                        /*
                        Toast.makeText(getContext().getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                                */
                        friend.setSelected(cb.isChecked());
                        DataManager.getInstance().getIntersection();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Friend friend = friendsList.get(position);
            holder.code.setText(" (" +  friend.getId() + ")");
            holder.name.setText(friend.getName());
            holder.name.setChecked(friend.isSelected());
            holder.name.setTag(friend);

            return convertView;

        }




    }
}
