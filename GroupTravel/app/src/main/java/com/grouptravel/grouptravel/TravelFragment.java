package com.grouptravel.grouptravel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jacek on 27/02/16.
 */
public class TravelFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.empty, container, false);
        return rootView;
    }


    private class MyCustomAdapter extends ArrayAdapter<Group> {

        private ArrayList<Group> groupsList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Group> groupsList) {
            super(context, textViewResourceId, groupsList);
            this.groupsList = new ArrayList<Group>();
            this.groupsList.addAll(groupsList);
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
                convertView = vi.inflate(R.layout.group_item, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Group group = (Group) cb.getTag();
                        Toast.makeText(getContext().getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        group.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Group group = groupsList.get(position);
            holder.code.setText(" (" +  group.getId() + ")");
            holder.name.setText(group.getName());
            holder.name.setChecked(group.isSelected());
            holder.name.setTag(group);

            return convertView;

        }

    }
}
