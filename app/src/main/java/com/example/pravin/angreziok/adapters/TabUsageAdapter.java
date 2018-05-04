package com.example.pravin.angreziok.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.modalclasses.Usage;

import java.util.ArrayList;

/**
 * Created by Pravin on 04-May-18.
 */

public class TabUsageAdapter extends ArrayAdapter<Usage> {

    public TabUsageAdapter(Context context, ArrayList<Usage> listForAdapter) {
        super(context,0,listForAdapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Usage usage = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.usage_result_row, parent, false);
        }
        // Lookup view for data population
        TextView studentRank =convertView.findViewById(R.id.studentRank);
        TextView studentName =convertView.findViewById(R.id.studentName);
        TextView usageTime =convertView.findViewById(R.id.usageTime);
        // Populate the data into the template view using the data object

        studentRank.setText("#"+(position+1));
        studentName.setText(usage.studentName);
        usageTime.setText(usage.usageTime);
        // Return the completed view to render on screen
        return convertView;
    }
}