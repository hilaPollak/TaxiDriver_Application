package com.example.hila.myfirstapplication.model.backend;

import android.content.Context;
import android.location.Location;

import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.entities.DriveStatus;
import com.example.hila.myfirstapplication.model.entities.Driver;

import java.util.List;

/***
 * This interface represent the func of data manager
 */
public interface IDataBase {
    /***
     * this interface implement how to act success or fail or in progress
     */
    public interface Action {
        void onSuccess();//what to do when the action success

        void onFailure(Exception exception);//what to do if its fail

        void onProgress(String status, double percent);//what to do when this in progress
    }

    /***
     * this interface implement how to act if data changed
     * @param <T> the class that notify about the change
     */
    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    /***
     *this func add driver to database
     * @param driverToAdd the driver we want add
     * @param action the action of method
     */
    void addDriver(Driver driverToAdd, Action action);

    /***
     * this function check if the driver valid in data base
     * @param emailForCheck the user input mail
     * @param passwordForCheck the user input password
     * @param action the action of method
     */
    void isValidDriverAuthentication(String emailForCheck, String passwordForCheck, Action action);

    /***
     * this func calculate list of driver's names
     * @return list of driver's names
     */
    List<String> getDriversNames();

    /***
     * this func calculate list of available drives
     * @return list of available drives
     */
    List<Drive> getAvailableDrives();

    /***
     * this func calculate list of ending drives
     * @return list of ending drives
     */
    List<Drive> getEndedDrives();

    /***
     * this func calculate drive list by driver name
     * @param driver the driver that call
     * @return drive list of the driver
     */
    List<Drive> getMyDrives(Driver driver);

    /***
     * this func calculate drive list of the driver date
     * @param date the date the driver choose
     * @return drive list of the driver date
     */
    List<Drive> getDrivesOfDate(String date);

    /***
     * this func calculate  available drive list of the driver's city choose
     * @param city the city driver choose
     * @return available drive list of the driver's city choose
     */
    List<Drive> getAvailableDrivesOfDestinationCity(String city);

    /***
     * this func calculate available drive list of the driver's location choose
     * @param location the location of driver
     * @return available drive list of the driver's location choose
     */
    List<Drive> getAvailableDrivesOfMyLocation(Location location);

    /***
     * this func calculate price by distance
     * @param price the  maximum price
     * @return drive list of the price
     */
    List<Drive> getDrivesOfPrice(double price);

    /***
     * this fuc find correct driver by email
     * @param email the email of driver
     * @return driver
     */
    Driver getDriver(String email);

    /***
     * This funciton change the status and the driver of the drive
     * @param drive the drive we want change
     * @param driver the driver that take the car
     * @param status the new status of drive
     * @param action  the action of method
     */
    void changeStatus(Drive drive, Driver driver, final DriveStatus status, final Action action);

    /***
     * This funciton change the distance of the drive by driver's address
     * @param location the driver's location
     * @param context the activity
     */
    void changeLocation(Location location, Context context);


}
