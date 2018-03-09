package com.example.pravin.angreziok.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pravin.angreziok.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartMenu extends AppCompatActivity {

    @BindView(R.id.nav_options)
    RecyclerView nav_options;

    ArrayList<String> planetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        ButterKnife.bind(this);
        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );
        CustomAdapter arrayAdapter= new CustomAdapter();
        nav_options.setLayoutManager(new LinearLayoutManager(this));
        nav_options.setAdapter(arrayAdapter);
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

        private LayoutInflater inflater;
        private Context context;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = inflater.inflate(R.layout.demo_item, parent, false);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_item, parent, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.serial_number.setText(planetList.get(position));
        }

        @Override
        public int getItemCount() {
            return planetList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView serial_number;

            public MyViewHolder(View itemView) {
                super(itemView);
                serial_number = (TextView)itemView.findViewById(R.id.text1);
            }
        }
    }
}
