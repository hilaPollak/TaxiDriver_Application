package com.example.hila.myfirstapplication.model.datasource;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.support.annotation.NonNull;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * this class implement the data manager by firebase
 */
public class Firebase_DBManager implements IDataBase {
    //region Fields
    static DatabaseReference drivesRef;//.ref of drives
    static DatabaseReference driverRef;//ref of drivers

    public Firebase_DBManager() {
        notifyToDriveList(new NotifyDataChange<List<Drive>>() {
            @Override
            public void onDataChange(List<Drive> obj) {

            }

            @Override
            public void onFailure(Exception exp) {

            }
        });
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

    public static List<Drive> getDriveList() {
        return driveList;
    }

    static List<Drive> driveList;
    static List<Driver> driverList;

    //endregion
    //region initialization
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        drivesRef = database.getReference("Drives");//create root to drives
        driverRef = database.getReference("Drivers");//create root to drivers


        driveList = new ArrayList<>();
        driverList = new ArrayList<>();
    }
    //endregion
    //region Methods


    @Override
    public void addDriver(Driver driverToAdd, final Action action) {
        Task<Void> task = driverRef.push().setValue(driverToAdd);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess();
            }
        });
    }

    @Override
    public void isValidDriverAuthentication(String emailForCheck, final String passwordForCheck, final Action action) {
        Query query = driverRef.orderByChild("email").equalTo(emailForCheck);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Driver checkDriver = dataSnapshot.getChildren().iterator().next().getValue(Driver.class);
                    if (checkDriver.getPassword().equals(passwordForCheck))
                        action.onSuccess();
                    else
                        action.onFailure(new Exception("Password is uncorrect"));
                } else
                    action.onFailure(new Exception("User doesn't exist"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    /**
     * interface NotifyDataChange. For update the list from the firebase.
     *
     * @param <T>
     */
    public interface NotifyDataChange<T> {
        //   void OnDataChanged(List<Drive> obj);

        void onDataChange(T obj);

        void onFailure(Exception exp);
    }


    private static ChildEventListener driveRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener serviceListener;


    /**
     * notifyToDriveList function. Notify when the data change.
     *
     * @param notifyDataChange NotifyDataChange<List<Drive>>.
     */
    public static void notifyToDriveList(final NotifyDataChange<List<Drive>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driveRefChildEventListener != null) {
                if (serviceListener != null) {
                    notifyDataChange.onFailure(new Exception("first unNotify ClientRequest list"));
                    return;
                } else {
                    serviceListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            notifyDataChange.onDataChange(driveList);
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
                    drivesRef.addChildEventListener(serviceListener);
                    return;
                }
            }
            driveList.clear();
            driveRefChildEventListener = new ChildEventListener() {
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
     * stopNotifyToDriveList remove Event Listener
     */
    public static void stopNotifyToDriveList() {
        if (driveRefChildEventListener != null) {
            drivesRef.removeEventListener(driveRefChildEventListener);
            driveRefChildEventListener = null;
        }
    }

    /**
     * notifyToDriverList function. Notify when the data change.
     *
     * @param notifyDataChange NotifyDataChange<List<Driver>>.
     */
    public static void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driverList.clear();
            driverRefChildEventListener = new ChildEventListener() {
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
     * stopNotifyToDriverList remove Event Listener
     */
    public static void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            driverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }


    @Override
    public List<String> getDriversNames() {
        List<String> driversNames = new ArrayList<>();
        for (Driver driver : driverList)
            driversNames.add(driver.getFirstName() + " " + driver.getLastName());
        return driversNames;
    }

    @Override
    public List<Drive> getAvailableDrives() {
        List<Drive> availableDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getStatusOfRide().toString().equals(DriveStatus.AVAILABLE.toString()))
                availableDrives.add(drive);
        return availableDrives;
    }

    @Override
    public List<Drive> getEndedDrives() {
        List<Drive> endedDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getStatusOfRide().toString().equals(DriveStatus.ENDING.toString()))
                endedDrives.add(drive);
        return endedDrives;
    }

    @Override
    public List<Drive> getMyDrives(Driver driver) {
        List<Drive> myDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getDriverName().equals(driver.getFirstName()))
                myDrives.add(drive);
        return myDrives;
    }

    public String getCityFromLocation(Location location) {

        return null;
    }

    @Override
    public List<Drive> getAvailableDrivesOfDestinationCity(String city) {
        List<Drive> availableDrivesOfDestinationCity = new ArrayList<>();
        for (Drive drive : driveList)
            if (getCityFromLocation(new Location(drive.getEndAddress())).matches(city))
                availableDrivesOfDestinationCity.add(drive);
        return availableDrivesOfDestinationCity;
    }

    @Override
    public List<Drive> getAvailableDrivesOfMyLocation(Location location) {
        List<Drive> myDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (new Location(drive.getEndAddress()).distanceTo(location) < 2000)
                myDrives.add(drive);
        return myDrives;
    }

    // @Override
  /*  public List<Drive> getDrivesOfDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = simpleDateFormat.format(date);
        String dateSourceStr, dateDesStr;
        List<Drive> drivesOfDate = new ArrayList<>();
        for (Drive drive : driveList) {
            //dateSourceStr = simpleDateFormat.format(drive.getStartTime());
           // dateDesStr = simpleDateFormat.format(drive.getEndTime());
            if (dateStr.matches(dateSourceStr) || dateStr.matches(dateDesStr))
                drivesOfDate.add(drive);
        }
        return drivesOfDate;
    }*/

    @Override
    public List<Drive> getDrivesOfPrice(double price) {
        List<Drive> drivesOfPrice = new ArrayList<>();
        for (Drive drive : driveList)
            if (new Location(drive.getEndAddress()).distanceTo(new Location(drive.getStartAddress())) / 0 / 1000.0 * 5.0 > price)
                drivesOfPrice.add(drive);
        return drivesOfPrice;
    }

    @Override
    public Driver getDriver(String email) {
        Driver d = new Driver();
        for (Driver driver : driverList)
            if (driver.getEmail().equals(email))
                d = driver;

        return d;
    }

    @Override
    public void changeStatus(Drive drive, Driver driver, final DriveStatus status, final Action action) {
//        drivesRef.child(driveID).child("driverName").setValue(driver.getFirstName());
//        drivesRef.child(driveID).child("statusOfRide").setValue(status)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        action.onSuccess();
//                    }
//                });
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
  ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<String> getDistance() {
        List<String> distance = new ArrayList<String>();
        distance.add("all");
        for (Drive drive : driveList)
            if (drive.getStatusOfRide().toString().equals(DriveStatus.AVAILABLE.toString()))
                distance.add(drive.getEmail());
        return distance;
    }

    @Override
    public List<Drive> getDrivesByDistance(String loc) {
        List<Drive> locationDrives = new ArrayList<>();
        for (Drive drive : driveList)
            if (drive.getStatusOfRide().toString().equals(DriveStatus.AVAILABLE.toString()))
                if (drive.getEmail().equals(loc))
                    locationDrives.add(drive);
        return locationDrives;
    }

}
