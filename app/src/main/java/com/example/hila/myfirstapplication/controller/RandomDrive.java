package com.example.hila.myfirstapplication.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.hila.myfirstapplication.R;

public class RandomDrive extends Fragment {
    private Chronometer chronometer;
    private boolean running = false;
    private Button start;
    private Button stop;
    private TextView price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_random_drive, container, false);
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        price = v.findViewById(R.id.see_price);


        class SyncTaskCount extends AsyncTask<Void, Void, Integer> {
            @Override
            protected Integer doInBackground(Void... voids) {
                String chronoText = chronometer.getText().toString();
                String array[] = chronoText.split(":");
                int minute = Integer.parseInt(array[0]);
                int money;
                if (minute == 0)
                    money = 5;
                else
                    money = minute * 2;
                return money;
            }
        }


        start = (Button) v.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price.setVisibility(View.GONE);

                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                SyncTaskCount asyncTaskCount;
                asyncTaskCount = new SyncTaskCount();
                asyncTaskCount.execute();
            }
        });

        stop = (Button) v.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                chronometer.stop();
                SyncTaskCount asyncTaskExample;
                asyncTaskExample = new SyncTaskCount();
                asyncTaskExample.execute();
                int money = asyncTaskExample.doInBackground();
                price.setVisibility(View.VISIBLE);
                price.setText("the price is: " + money + " shekel");


            }
        });


        return v;
    }

}
