package com.example.hila.myfirstapplication.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.hila.myfirstapplication.model.backend.FactoryDataBase;
import com.example.hila.myfirstapplication.model.datasource.Firebase_DBManager;
import com.example.hila.myfirstapplication.model.entities.Drive;


import java.util.List;

/***
 * this class represent the service that turn on when the data in drive list change and child add
 * intent that run in background not deepened in activity
 * send to broadcast a flag every time we had a new drive
 */
public class NotificationService extends Service {

    private int lastCount = 0;
    Context context;
    Firebase_DBManager dbManager;

    /***
     * this class said what happen each time (and not once)  a client requests something from service
     */
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {//when we turn up the service
        dbManager = (Firebase_DBManager) FactoryDataBase.getDataBase();//set dbmanager
        context = getApplicationContext();//get context
        dbManager.notifyToDriveList(new Firebase_DBManager.NotifyDataChange<List<Drive>>() {//open listing to drive list
            @Override
            public void onDataChange(List<Drive> obj) {//add drive in list
                try {
                    Intent intent = new Intent(context, ToNotificationReceiver.class);//create new intent
                    sendBroadcast(intent);//send to broadcast the notification
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception exception) {
            }
        });
        return START_REDELIVER_INTENT;//said to system to continue the service even when the application close.
    }

    /***
     * the system calls this method to retrieve the IBinder only when the first client binds(connected)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
