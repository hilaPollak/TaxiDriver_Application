package com.example.hila.myfirstapplication.model.entities;

/**
 this class represent the driver's characteristics
 */
public class Driver {
    private String FirstName;//first name of driver
    private String LastName;//last name of driver
    private long Id;//id of driver
    private long PhoneNumber;// phone number of driver
    private String Email;// email of driver
    private long CreditCard;// credit card for commission

    /**
     this func build defult constructor
     */
    public Driver(){}

    /**
     this func build constructor
     */
    public Driver(String firstName, String lastName, long id, long phoneNumber, String email, long creditCard) {
        FirstName = firstName;
        LastName = lastName;
        Id = id;
        PhoneNumber = phoneNumber;
        Email = email;
        CreditCard = creditCard;
    }

    /**
     this func return the first name of driver
     * @return   the driver's first name
     */
    public String getFirstName() {
        return FirstName;
    }
    /**
     this func input the driver's first name
     @param  firstName  the first name of driver
     @return   null
     */
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    /**
     this func return the last name of driver
     * @return   the driver's last name
     */
    public String getLastName() {
        return LastName;
    }
    /**
     this func input the driver's last name
     @param  lastName  the last name of driver
     @return   null
     */
    public void setLastName(String lastName) {
        LastName = lastName;
    }
    /**
     this func return the id of driver
     * @return   the driver's id
     */
    public long getId() {
        return Id;
    }
    /**
     this func input the driver's id
     @param  id  the id driver
     @return   null
     */
    public void setId(long id) {
        Id = id;
    }
    /**
     this func return the phone number of driver
     * @return   the driver's phone number
     */
    public long getPhoneNumber() {
        return PhoneNumber;
    }
    /**
     this func input the driver's phone number
     @param  phoneNumber  the phone number of driver
     @return   null
     */
    public void setPhoneNumber(long phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    /**
     this func return the email of driver
     * @return   the driver's email
     */
    public String getEmail() {
        return Email;
    }
    /**
     this func input the driver's email
     @param  email  the email of driver
     @return   null
     */
    public void setEmail(String email) {
        Email = email;
    }
    /**
     this func return the credit card of driver
     * @return   the driver's credit card
     */
    public long getCreditCard() {
        return CreditCard;
    }
    /**
     this func input the credit card of driver
     @param  creditCard  the credit card of driver
     @return   null
     */
    public void setCreditCard(long creditCard) {
        CreditCard = creditCard;
    }
}
