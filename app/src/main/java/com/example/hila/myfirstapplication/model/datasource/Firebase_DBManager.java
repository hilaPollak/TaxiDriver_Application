package com.example.hila.myfirstapplication.model.datasource;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.example.hila.myfirstapplication.model.backend.IDataBase;
import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.entities.DriveStatus;
import com.example.hila.myfirstapplication.model.entities.Driver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/***
 * This class implement the data manager by firebase
 */
public class Firebase_DBManager implements IDataBase {

    static DatabaseReference drivesRef;//ref of drives
    static DatabaseReference driverRef;//ref of drivers
    static List<Drive> driveList;// the list of drive
    static List<Driver> driverList;//the list of driver

    /***
     * default constructor that active the listener
     */
    public Firebase_DBManager() {
        //call to drive list listener
        notifyToDriveList(new NotifyDataChange<List<Drive>>() {
            @Override
            public void onDataChange(List<Drive> obj) {

            }

            @Override
            public void onFailure(Exception exp) {

            }
        });
        //call to driver list listener
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
                               @Override
                               public void onDataChange(List<Driver> obj) {

                               }

                               @Override
                               public void onFailure(Exception exp) {

                               }
                           }
        );
    }

    /***
     * this function get the drive list
     * @return drive list
     */
    public static List<Drive> getDriveList() {
        return driveList;
    }

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        drivesRef = database.getReference("Drives");//root to drives
        driverRef = database.getReference("Drivers");//root to drivers

        driveList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    /***
     * This function add new driver to the system
     * @param driverToAdd is the driver we want to add
     * @param action  the action of method
     */
    @Override
    public void addDriver(Driver driverToAdd, final Action action) {
        Task<Void> task = driverRef.push().setValue(driverToAdd);//push- had spacial key

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {//add to database success
                action.onSuccess();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {//add to database fail
                action.onFailure(e);
            }
        });
        ;
    }

    /***
     * this function check if the driver valid in data base
     * @param emailForCheck the user input mail
     * @param passwordForCheck the user input password
     * @param action the action of method
     */
    @Override
    public void isValidDriverAuthentication(String emailForCheck, final String passwordForCheck, final Action action) {
        Query query = driverRef.orderByChild("email").equalTo(emailForCheck);//check if emails equals
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {//find equal mail
                    Driver checkDriver = dataSnapshot.getChildren().iterator().next().getValue(Driver.class);//get the driver
                    if (checkDriver.getPassword().equals(passwordForCheck))//checks if the password equals to the user's password
                        action.onSuccess();
                    else//password unequal
                        action.onFailure(new Exception("Password is uncorrect"));
                } else//not found equals mail
                    action.onFailure(new Exception("User doesn't exist"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /***
     * this interface implement how to act if data changed
     * @param <T> the class that notify about the change
     */
    public interface NotifyDataChange<T> {

        void onDataChange(T obj);

        void onFailure(Exception exp);
    }


    private static ChildEventListener driveRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener serviceListener;

    /**
     * this function listener to changes and update the driveList when the data change.
     *
     * @param notifyDataChange NotifyDataChange<List<Drive>>.
     */
    public static void notifyToDriveList(final NotifyDataChange<List<Drive>> notifyDataChange) {
        if (notifyDataChange != null) {//we had listener
            if (driveRefChildEventListener != null) {
                if (serviceListener != null) {//we had listener to service- when drive change
                    notifyDataChange.onFailure(new Exception("first unNotify DriveRequest list"));
                    return;
                } else {
                    serviceListener = new ChildEventListener() {//create new listener to service- when drive change
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            notifyDataChange.onDataChange(driveList);//notification
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    drivesRef.addChildEventListener(serviceListener);//listener to drive list
                    return;
                }
            }
            driveList.clear();
            driveRefChildEventListener = new ChildEventListener() {//create new event listener to change in drive list
                /**
                 * onChildAdded - add the new to the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    drive.setId(dataSnapshot.getKey());
                    driveList.add(drive);


                    notifyDataChange.onDataChange(driveList);
                }

                /**
                 * onChildChanged- onChildChanged update the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    for (int i = 0; i < driveList.size(); i++) {
                        if (driveList.get(i).equals(drive)) {
                            driveList.set(i, drive);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driveList);
                }

                /**
                 * onChildRemoved update the list
                 * @param dataSnapshot DataSnapshot
                 */
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    for (int i = 0; i < driveList.size(); i++) {
                        if (driveList.get(i).equals(drive)) {
                            driveList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driveList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            drivesRef.addChildEventListener(driveRefChildEventListener);
        }
    }

    /**
     * this function remove Event Listener to drive ref
     */
    public static void stopNotifyToDriveList() {
        if (driveRefChildEventListener != null) {
            drivesRef.removeEventListener(driveRefChildEventListener);
            driveRefChildEventListener = null;
        }
    }

    /**
     * this function listener to changes and update the driverList when the data change.
     *
     * @param notifyDataChange NotifyDataChange<List<Driver>>.
     */
    public static void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {//we had listener
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driverList.clear();
            driverRefChildEventListener = new ChildEventListener() {//create new event listener tochange in drive list
                /**
                 * onChildAdded - add the new to the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    driverList.add(driver);

                    notifyDataChange.onDataChange(driverList);
                }

                /**
                 * onChildChanged- onChildChanged update the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).equals(driver)) {
                            driverList.set(i, driver);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driverList);
                }

                /**
                 * onChildRemoved update the list
                 * @param dataSnapshot DataSnapshot
                 */
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).equals(driver)) {
                            driverList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driverList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            driverRef.addChildEventListener(driverRefChildEventListener);
        }
    }

    /**
     * this function remove Event Listener
     */
    public static void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            driverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }

    /***
     * this func calculate list of driver's names
     * @return list of driver's names
     */
    @Override
    public List<String> getDriversNames() {
        List<String> driversNames = new ArrayList<>();
        for (Driver driver : driverList)
            driversNames.add(driver.getFirstName() + " " + driver.getLastName());
        return driversNames;
    }

    /***
     * this func calculate list of available drives
     * @return list of available drives
     */
    @Override
    public List<Drive> getAvailableDrives() {
        List<Drive> availableDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getStatusOfRide().toString().equals(DriveStatus.AVAILABLE.toString()))
                availableDrives.add(drive);
        return availableDrives;
    }

    /***
     * this func calculate list of ending drives
     * @return list of ending drives
     */
    @Override
    public List<Drive> getEndedDrives() {
        List<Drive> endedDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getStatusOfRide().toString().equals(DriveStatus.ENDING.toString()))
                endedDrives.add(drive);
        return endedDrives;
    }

    /***
     * this func calculate drive list by driver name
     * @param driver the driver that call
     * @return drive list of the driver
     */
    @Override
    public List<Drive> getMyDrives(Driver driver) {
        List<Drive> myDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getDriverName().equals(driver.getFirstName()))
                myDrives.add(drive);
        return myDrives;
    }

    /***
     * this func calculate  available drive list of the driver's city choose
     * @param city the city driver choose
     * @return available drive list of the driver's city choose
     */
    @Override
    public List<Drive> getAvailableDrivesOfDestinationCity(String city) {
        List<Drive> availableDrivesOfDestinationCity = new ArrayList<>();
        for (Drive drive : driveList) {
            String string = drive.getStartAddress();
            String[] parts = string.split(", ");
            String part2 = parts[1]; // city
            if (part2.equals(city))
                availableDrivesOfDestinationCity.add(drive);
        }
        return availableDrivesOfDestinationCity;
    }

    /***
     * this func calculate available drive list of the driver's location choose
     * @param location the location of driver
     * @return available drive list of the driver's location choose
     */
    @Override
    public List<Drive> getAvailableDrivesOfMyLocation(Location location) {
        List<Drive> myDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (new Location(drive.getEndAddress()).distanceTo(location) < 2000)
                myDrives.add(drive);
        return myDrives;
    }

    /***
     * this func calculate drive list of the driver date
     * @param date the date the driver choose
     * @return drive list of the driver date
     */
    @Override
    public List<Drive> getDrivesOfDate(String date) {
        List<Drive> drivesOfDate = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getStartTime().equals(date))
                drivesOfDate.add(drive);
        return drivesOfDate;
    }

    /***
     * this func calculate price by distance
     * @param price the  maximum price
     * @return drive list of the price
     */
    @Override
    public List<Drive> getDrivesOfPrice(double price) {
        List<Drive> drivesOfPrice = new ArrayList<>();
        for (Drive drive : driveList)
            if (new Location(drive.getEndAddress()).distanceTo(new Location(drive.getStartAddress())) / 0 / 1000.0 * 5.0 > price)
                drivesOfPrice.add(drive);
        return drivesOfPrice;
    }

    /***
     * this fuc find correct driver by email
     * @param email the email of driver
     * @return driver
     */
    @Override
    public Driver getDriver(String email) {
        Driver d = new Driver();
        for (Driver driver : driverList)
            if (driver.getEmail().equals(email))
                d = driver;

        return d;
    }

    /***
     * This funciton change the status and the driver of the drive
     * @param drive the drive we want change
     * @param driver the driver that take the car
     * @param status the new status of drive
     * @param action  the action of method
     */
    @Override
    public void changeStatus(Drive drive, Driver driver, final DriveStatus status, final Action action) {
        drive.setDriverName(driver.getFirstName());
        drive.setStatusOfRide(status);
        drivesRef.child(drive.getId()).setValue(drive)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.onSuccess();
                    }
                });

    }

    /***
     * This funciton change the distance of the drive by driver's address
     * @param location the driver's location
     * @param context the activity
     */
    @Override
    public void changeLocation(Location location, Context context) {

        Location loc = new Location(LocationManager.GPS_PROVIDER);
        for (Drive drive : driveList) {
            try {
                loc = drive.getLocation(context);
            } catch (Exception e) {
                e.printStackTrace();
            }

            float d = loc.distanceTo(location) / 1000;//to km
            drive.setDistance(String.valueOf(d));
        }
    }


}
