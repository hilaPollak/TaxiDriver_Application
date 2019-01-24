package com.example.hila.myfirstapplication.model.entities;

import android.content.ContentValues;

/**
 * This class represent the driver's characteristics
 */
public class Driver {
    private String FirstName;//first name of driver
    private String LastName;//last name of driver
    private String _Id;//id of driver
    private String PhoneNumber;// phone number of driver
    private String Email;// email of driver
    private String CreditCard;// credit card for commission
    private String password;//password for driver


    /**
     * this func build default constructor
     */
    public Driver() {
    }

    /**
     * this func build constructor
     */
    public Driver(String firstName, String lastName, String id, String phoneNumber, String email, String creditCard, String password1) {
        FirstName = firstName;
        LastName = lastName;
        _Id = id;
        PhoneNumber = phoneNumber;
        Email = email;
        CreditCard = creditCard;
        password = password1;
    }

    /**
     * this func return the first name of driver
     *
     * @return the driver's first name
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     * this func input the driver's first name
     *
     * @param firstName the first name of driver
     * @return null
     */
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    /**
     * this func return the last name of driver
     *
     * @return the driver's last name
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * this func input the driver's last name
     *
     * @param lastName the last name of driver
     * @return null
     */
    public void setLastName(String lastName) {
        LastName = lastName;
    }

    /**
     * this func return the id of driver
     *
     * @return the driver's id
     */
    public String get_Id() {
        return _Id;
    }

    /**
     * this func input the driver's id
     *
     * @param id the id driver
     * @return null
     */
    public void set_Id(String id) {
        _Id = id;
    }

    /**
     * this func return the phone number of driver
     *
     * @return the driver's phone number
     */
    public String getPhoneNumber() {
        return PhoneNumber;
    }

    /**
     * this func input the driver's phone number
     *
     * @param phoneNumber the phone number of driver
     * @return null
     */
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    /**
     * this func return the email of driver
     *
     * @return the driver's email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * this func input the driver's email
     *
     * @param email the email of driver
     * @return null
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     * this func return the credit card of driver
     *
     * @return the driver's credit card
     */
    public String getCreditCard() {
        return CreditCard;
    }

    /**
     * this func input the credit card of driver
     *
     * @param creditCard the credit card of driver
     * @return null
     */
    public void setCreditCard(String creditCard) {
        CreditCard = creditCard;
    }

    /***
     * his func return the password of driver
     * @return the driver's credit card
     */
    public String getPassword() {
        return password;
    }

    /***
     * this function set the driver password
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}