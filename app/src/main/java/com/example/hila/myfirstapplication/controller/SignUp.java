package com.example.hila.myfirstapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hila.myfirstapplication.R;
import com.example.hila.myfirstapplication.model.backend.FactoryDataBase;
import com.example.hila.myfirstapplication.model.backend.IDataBase;
import com.example.hila.myfirstapplication.model.entities.Driver;

/**
 * this class represent the activity of driver sign up.
 */
public class SignUp extends Activity {

    //the objects of the screen
    private TextView textView;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextID;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextCreditCard;
    private EditText editTextPassword;
    private Button buttonSignUp;

    /**
     * This func make the view of the object in the activity when he called
     *
     * @param savedInstanceState represent the jump of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
    }

    /**
     * this function Find the Views in the layout<br />
     */
    private void findViews() {
        textView = (TextView) findViewById(R.id.textView);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextID = (EditText) findViewById(R.id.editTextID);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextCreditCard = (EditText) findViewById(R.id.editTextCreditCard);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

    }

    /**
     * this func call the system that add driver to databasa
     *
     * @param view the event that start the active
     */
    void signUpClick(View view) {

        checkData();

    }

    /***
     * this class create driver object
     * @return object by driver type
     */
    protected Driver getDriver() {
        Driver driver = new Driver();

        driver.set_Id(editTextID.getText().toString());//set id
        driver.setFirstName(editTextName.getText().toString());//set first name
        driver.setLastName(editTextLastName.getText().toString());//set last name
        driver.setPhoneNumber(editTextPhone.getText().toString());//set phone
        driver.setEmail(editTextEmail.getText().toString());//set mail
        driver.setCreditCard(editTextCreditCard.getText().toString());//set credit card
        driver.setPassword(editTextPassword.getText().toString());//set password

        return driver;
    }

    /***
     * after we find the details correct  the intent call to new login activity
     */
    void goToActivity2() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);//start the activity
    }

    /***
     * this func add drive to database and check the input
     * @param driver the object we want to add
     */
    protected void addDriver(final Driver driver) {

        buttonSignUp.setEnabled(false);
        final IDataBase dataBase = FactoryDataBase.getDataBase();
        //create new asyncTask to add driver for data base
        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... contexts) {
                try {
                    dataBase.addDriver(driver, new IDataBase.Action() {
                        @Override
                        public void onSuccess() {//if register success
                            Toast.makeText(getBaseContext(), "ההרשמה בוצעה בהצלחה", Toast.LENGTH_LONG).show();
                            goToActivity2();//to enter profile
                        }

                        @Override
                        public void onFailure(Exception exception) {//if register fail
                            Toast.makeText(getBaseContext(), "ההרשמה נכשלה", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onProgress(String status, double percent) {//on progress

                        }
                    });
                    return null;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }
        }.execute();//active the asyncTask
    }

    /***
     * thisfunc check the correct of email input
     * @param editText email text
     * @return true if currect input, false if null or uncorrected mail address
     */
    public boolean isEmail(EditText editText) {
        CharSequence email = editText.getText().toString();
        if (email == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /***
     * thisfunc check the correct of name input
     * @param editText name text
     * @return true if correct input, false if null
     */
    public boolean isName(EditText editText) {
        CharSequence name = editText.getText().toString();
        return (!TextUtils.isEmpty(name));
    }

    /***
     * thisfunc check the correct of phone input
     * @param editText phone text
     * @return true if correct input, false if null or uncorrect phone's digits
     */
    public boolean isPhone(EditText editText) {
        CharSequence phone = editText.getText().toString();
        if (phone == null)
            return false;
        return (phone.length() == 10);
    }

    /***
     * thisfunc check the correct of credit card input
     * @param editText credit text
     * @return true if correct input, false if null or uncorrect credit's digits
     */
    public boolean isCredit(EditText editText) {
        CharSequence credit = editText.getText().toString();
        if (credit == null)
            return false;
        return (credit.length() == 16);
    }

    /***
     * this func check the input of drive before she add to data base
     */
    void checkData() {

        if (!isName(editTextName))//check nae input
            editTextName.setError("name is reqired");
        else if (!isName(editTextLastName))//check nae input
            editTextLastName.setError("last name is reqired");
        else if (!isPhone(editTextID))//check phone input
            editTextID.setError("Enter correct ID with 10 digits");
        else if (!isPhone(editTextPhone))//check phone input
            editTextPhone.setError("Enter correct phone number with 10 digits");
        else if (!isEmail(editTextEmail))//check email input
            editTextEmail.setError("Enter valid email");
        else if (!isCredit(editTextCreditCard))//check phone input
            editTextCreditCard.setError("Enter correct credit card with 16 digits");
        else if (!isName(editTextPassword))//check phone input
            editTextPassword.setError("password is reqired");

        else//input correct
        {
            Driver driver = getDriver();
            addDriver(driver);
        }

    }
}
