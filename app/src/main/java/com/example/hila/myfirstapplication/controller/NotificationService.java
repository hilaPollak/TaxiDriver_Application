package com.example.hila.myfirstapplication.controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.hila.myfirstapplication.model.backend.FactoryDataBase;
import com.example.hila.myfirstapplication.model.datasource.Firebase_DBManager;
import com.example.hila.myfirstapplication.model.entities.Drive;


import java.util.List;

public class NotificationService extends Service {

    private int lastCount = 0;
    Context context;
    Firebase_DBManager dbManager;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        dbManager = (Firebase_DBManager) FactoryDataBase.getDataBase();
        context = getApplicationContext();
        dbManager.notifyToDriveList(new Firebase_DBManager.NotifyDataChange<List<Drive>>() {
            @Override
            public void onDataChange(List<Drive> obj) {
                try {
                    Intent intent = new Intent(context, ToNotificationReceiver.class);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception exception) {
            }
        });
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
