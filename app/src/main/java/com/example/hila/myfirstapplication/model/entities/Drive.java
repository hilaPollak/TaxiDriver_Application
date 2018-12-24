package com.example.hila.myfirstapplication.model.entities;

import java.time.Clock;
/**
 this class represent the drive's characteristics
 */
public class Drive {
    private DriveStatus StatusOfRide;//available/ treatment/ ending
    private String StartAddress;//the address to pick up client
    private String EndAddress;//Destination address
    private Clock StartTime;//start of driving
    private Clock EndTime;//end of driving
    private String Name;//client's name
    private long PhoneNumber;//client's phone number
    private String Email;//client's email
    private String DriverName;//driver name


    /**
     this func build defult constructor
     */
    public Drive(){}

    /**
     this func build constructor
     */
    public Drive(DriveStatus statusOfRide, String startAddress, String endAddress, Clock startTime, Clock endTime, String name, long phoneNumber, String email) {
        StatusOfRide = statusOfRide;
        StartAddress = startAddress;
        EndAddress = endAddress;
        StartTime = startTime;
        EndTime = endTime;
        Name = name;
        PhoneNumber = phoneNumber;
        Email = email;
        DriverName="";
    }

    /**
     this func return the status of drive
     * @return   the status of drive
     */
    public DriveStatus getStatusOfRide() {
        return StatusOfRide;
    }
    /**
     this func input the ride's status
     @param  statusOfRide  the status of the drive
     @return   null
     */
    public void setStatusOfRide(DriveStatus statusOfRide) {
        StatusOfRide = statusOfRide;
    }

    /**
     this func return the address of the start
     * @return   the start address
     */
    public String getStartAddress() {
        return StartAddress;
    }
    /**
     this func input the ride's start address
     @param  startAddress  the start address of the ride
     @return   null
     */
    public void setStartAddress(String startAddress) {
        StartAddress = startAddress;
    }

    /**
     this func return the destination address
     * @return   the destination address
     */
    public String getEndAddress() {
        return EndAddress;
    }
    /**
     this func input the ride's end adress
     @param  endAddress  the end address of the ride
     @return   null
     */
    public void setEndAddress(String endAddress) {
        EndAddress = endAddress;
    }

    /**
     this func return start time of driving
     * @return   the start time of driving
     */
    public Clock getStartTime() {
        return StartTime;
    }
    /**
     this func input the ride's start time
     @param  startTime  the start time of the ride
     @return   null
     */
    public void setStartTime(Clock startTime) {
        StartTime = startTime;
    }

    /**
     this func return end time of driving
     * @return   the end time of driving
     */
    public Clock getEndTime() {
        return EndTime;
    }
    /**
     this func input the ride's end time
     @param  endTime  the end time of the ride
     @return   null
     */
    public void setEndTime(Clock endTime) {
        EndTime = endTime;
    }
    /**
     this func return the name of client
     * @return   the client's name
     */
    public String getName() {
        return Name;
    }
    /**
     this func input the client's name
     @param  name  the name of client
     @return   null
     */
    public void setName(String name) {
        Name = name;
    }
    /**
     this func return the phone num of client
     * @return   the client's phone number
     */
    public long getPhoneNumber() {
        return PhoneNumber;
    }
    /**
     this func input the client's phone number
     @param  phoneNumber  the phone number of client
     @return   null
     */
    public void setPhoneNumber(long phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    /**
     this func return the email of client
     * @return   the client's email
     */
    public String getEmail() {
        return Email;
    }
    /**
     this func input the client's email
     @param  email  the email of client
     @return   null
     */
    public void setEmail(String email) {
        Email = email;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String d) {
        DriverName = d;
    }
}
