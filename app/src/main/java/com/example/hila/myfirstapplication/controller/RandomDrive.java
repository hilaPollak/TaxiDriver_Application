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

    class SyncTaskCounter extends AsyncTask<Void, Double, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            double price = 0;

            while (!isCancelled()) {
                price = price + 0.05;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(price); // this instructs to call onProgressUpdate from UI thread.
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Double... price) {
            printPrice(price[0]);  // this is called on UI thread
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    private Chronometer chronometer;
    private Button start;
    private Button stop;
    private TextView price;
    SyncTaskCounter asyncTaskCount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_random_drive, container, false);
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        price = v.findViewById(R.id.see_price);

        getActivity().setTitle("Random Drive");


        start = (Button) v.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();


//                SyncTaskCounter asyncTaskCount;
//                asyncTaskCount = new SyncTaskCounter();
                asyncTaskCount = new SyncTaskCounter();
                asyncTaskCount.execute();
            }
        });
        stop = (Button) v.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                asyncTaskCount.cancel(true);

            }
        });
        return v;
    }

    public void printPrice(double price1) {
        price.setText("the price is: " + price1 + " shekel");

    }
}