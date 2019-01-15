package com.example.hila.myfirstapplication.model.backend;

import android.content.Context;
import android.location.Location;

import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.entities.DriveStatus;
import com.example.hila.myfirstapplication.model.entities.Driver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * this interface represent the func of data manager
 */
public interface IDataBase {

    public interface Action {
        void onSuccess();//what to do when the action success

        void onFailure(Exception exception);//what to do if its fail

        void onProgress(String status, double percent);//what to do when thus in progress
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    void addDriver(Driver driverToAdd, Action action);//add driver to database

    void isValidDriverAuthentication(String emailForCheck, String passwordForCheck, Action action);//check if the driver valid in data base

    List<String> getDriversNames();

    List<Drive> getAvailableDrives();

    List<Drive> getEndedDrives();

    List<Drive> getMyDrives(Driver driver);

    List<Drive> getAvailableDrivesOfDestinationCity(String city);

    List<Drive> getAvailableDrivesOfMyLocation(Location location);

    // List<Drive> getDrivesOfDate(Date date);

    List<Drive> getDrivesOfPrice(double price);

    Driver getDriver(String email);

    void changeStatus(String driveID, Driver driver, final DriveStatus status,final Action action);

}
