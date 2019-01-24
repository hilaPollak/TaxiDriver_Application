package com.example.hila.myfirstapplication.model.entities;

import android.content.ContentValues;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.List;
import java.util.Locale;

/**
 * this class represent the drive's characteristics
 */
public class Drive {
    private DriveStatus StatusOfRide;//available/ treatment/ ending
    private String StartAddress;//the address to pick up client
    private String EndAddress;//Destination address
    private String StartTime;//start of driving
    private String Name;//client's name
    private String PhoneNumber;//client's phone number
    private String Email;//client's email
    private String DriverName;//driver name
    private String id;//the key of task in firebase
    private String Distance;//the distance between the start address to driver's location


    /**
     * this func build default constructor for firebase
     */
    public Drive(DriveStatus driveStatus, String startAddress1, String endAddress1, String startTime, String nameText, String phoneText, String emailText, String driverName) {
    }

    /***
     * constructor
     */
    public Drive(DriveStatus statusOfRide, String startAddress, String endAddress, String startTime, String name, String phoneNumber, String email) {
        StatusOfRide = statusOfRide;
        StartAddress = startAddress;
        EndAddress = endAddress;
        StartTime = startTime;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        DriverName = " a";

    }

    /***
     * default constructor
     */
    public Drive() {
    }

    /**
     * this func return the status of drive
     *
     * @return the status of drive
     */
    public DriveStatus getStatusOfRide() {
        return StatusOfRide;
    }

    /**
     * this func input the ride's status
     *
     * @param statusOfRide the status of the drive
     * @return null
     */
    public void setStatusOfRide(DriveStatus statusOfRide) {
        StatusOfRide = statusOfRide;
    }

    /**
     * this func return the address of the start
     *
     * @return the start address
     */
    public String getStartAddress() {
        return StartAddress;
    }

    /**
     * this func input the ride's start address
     *
     * @param startAddress the start address of the ride
     * @return null
     */
    public void setStartAddress(String startAddress) {
        StartAddress = startAddress;
    }

    /**
     * this func return the destination address
     *
     * @return the destination address
     */
    public String getEndAddress() {
        return EndAddress;
    }

    /**
     * this func input the ride's end adress
     *
     * @param endAddress the end address of the ride
     * @return null
     */
    public void setEndAddress(String endAddress) {
        EndAddress = endAddress;
    }

    /**
     * this func return start time of driving
     *
     * @return the start time of driving
     */
    public String getStartTime() {
        return StartTime;
    }


    /**
     * this func input the ride's start time
     *
     * @param startTime the start time of the ride
     * @return null
     */
    public void setStartTime(String startTime) {
        StartTime = startTime;
    }


    /**
     * this func return the name of client
     *
     * @return the client's name
     */
    public String getName() {
        return Name;
    }

    /**
     * this func input the client's name
     *
     * @param name the name of client
     * @return null
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * this func return the phone num of client
     *
     * @return the client's phone number
     */
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    /**
     * this func input the client's phone number
     *
     * @param phoneNumber the phone number of client
     * @return null
     */
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    /**
     * this func return the email of client
     *
     * @return the client's email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * this func input the client's email
     *
     * @param email the email of client
     * @return null
     */
    public void setEmail(String email) {
        Email = email;
    }


    /***
     * This function return the driver name
     * @return
     */
    public String getDriverName() {
        return DriverName;
    }

    /***
     *
     * @param d The drivers name
     */
    public void setDriverName(String d) {
        DriverName = d;
    }

    /***
     * This function return the ID of the driver in firebase store
     * @return the id value
     */
    public String getId() {
        return id;
    }

    /***
     * This function input the ID od the driver in firebase store
     * @param id the root of firebase task
     */
    public void setId(String id) {
        this.id = id;
    }

    /***
     * This funciton return the distance between the driver to drive
     * @return the distance
     */
    public String getDistance() {
        return Distance;
    }

    /***
     * This function input the distance between the driver to drive
     * @param distance the calculate distance we want set
     */
    public void setDistance(String distance) {
        Distance = distance;
    }

    /***
     * This function convert the location by string address
     * @param context the context of activity to calculate the geocoder
     * @return the convert location from address
     */
    public Location getLocation(Context context) throws Exception {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        Location locationA = null;//create new location
        if (gc.isPresent()) {
            List<Address> list = gc.getFromLocationName(getStartAddress(), 1);
            Address address = list.get(0);
            double lat = address.getLatitude();//set latitude
            double lng = address.getLongitude();//set longitude

            locationA = new Location("A");

            locationA.setLatitude(lat);
            locationA.setLongitude(lng);
        }
        return locationA;
    }

    /***
     * This function returns a string with all the details of the drive
     * @return
     */
    @Override
    public String toString() {
        return "Name: " + Name + "\n" + "Phone Number: " + PhoneNumber + "\n" + "Start Address: " + StartAddress + "\n"
                + "End Address: " + EndAddress + "\n" + "Start time: " + StartTime + "\n" + "Email: " + Email + "\n" + "Status of drive: " + StatusOfRide.toString() + "\n";

    }


}
