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

/**
 * this class represent the fragment of random fragment- creative.
 */
public class RandomDrive extends Fragment {

    /***
     * this class extend the asyncTask and do act in background
     * the act- calculate price and print
     */
    class SyncTaskCounter extends AsyncTask<Void, Double, Void> {
        @Override
        protected Void doInBackground(Void... voids) {//the act we do in background
            double price = 0;

            while (!isCancelled()) {//until we dont press stop and cancel the act
                price = price + 0.05;//add 0.05 every 1 second
                try {
                    Thread.sleep(1000);//whait 1 second
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
            super.onCancelled();//cancel the task
        }
    }

    //the objects of the screen
    private Chronometer chronometer;
    private Button start;
    private Button stop;
    private TextView price;
    SyncTaskCounter asyncTaskCount;

    /***
     * this func create the fragment
     * @param inflater support for general purpose decompression
     * @param container the container weput the fragment
     * @param savedInstanceState argument to save state
     * @return the view of fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_random_drive, container, false);//find view
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);//find chronometer view
        price = v.findViewById(R.id.see_price);//find price view
        getActivity().setTitle("Random Drive");//set title of fragment
        start = (Button) v.findViewById(R.id.start);//find button start view
        start.setOnClickListener(new View.OnClickListener() {//click listener
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());//set chronometer
                chronometer.start();//start chronometer
                asyncTaskCount = new SyncTaskCounter();//create new asyncTak
                asyncTaskCount.execute();//start asyncTask
            }
        });
        stop = (Button) v.findViewById(R.id.stop);//find stop button
        stop.setOnClickListener(new View.OnClickListener() {//click listener
            @Override
            public void onClick(View view) {
                chronometer.stop();//sto chronometer
                asyncTaskCount.cancel(true);//cancel asyncTask
            }
        });
        return v;//the view
    }

    /***
     * this function get price and print it
     * @param price1 the price to print
     */
    public void printPrice(double price1) {
        String p=String.format("%.2f",price1);
        price.setText("the price is: " + p + " shekel");
    }
}