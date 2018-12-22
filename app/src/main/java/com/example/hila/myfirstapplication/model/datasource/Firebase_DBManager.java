package com.example.hila.myfirstapplication.model.datasource;

import android.support.annotation.NonNull;

import com.example.hila.myfirstapplication.model.backend.IDataBase;
import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.entities.Driver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/***
 * this class implement the data manager by firebase
 */
public class Firebase_DBManager implements IDataBase {
    //region Fields
    static DatabaseReference drivesRef;//.ref of drives
    static DatabaseReference driverRef;//ref of drivers
    static List<Drive> driveList;//list of drives
    //endregion
    //region initialization
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        drivesRef = database.getReference("Drives");//create root to drives
        driverRef = database.getReference("Drivers");//create root to drivers
        driveList = new ArrayList<>();
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
        Query query  = driverRef.orderByChild("email").equalTo(emailForCheck);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Driver checkDriver=dataSnapshot.getChildren().iterator().next().getValue(Driver.class);
                    if(checkDriver.getPassword().equals(passwordForCheck))
                        action.onSuccess();
                    else
                        action.onFailure(new Exception("password is uncorrect"));
                }
                else
                    action.onFailure(new Exception("user doesnt exist"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    //endregion*/
   /* public List<String> getDriversNames() {
        List<String> driversNames = new ArrayList<>();
        for (Driver driver : driverList)
            driversNames.add(driver.getFirstName() + " " + driver.getLastName());
        return driversNames;
    }*/
}
