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
import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.entities.Driver;

public class SignUp extends Activity {


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
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-12-20 22:59:44 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        textView = (TextView)findViewById( R.id.textView );
        editTextName = (EditText)findViewById( R.id.editTextName );
        editTextLastName = (EditText)findViewById( R.id.editTextLastName );
        editTextID = (EditText)findViewById( R.id.editTextID );
        editTextPhone = (EditText)findViewById( R.id.editTextPhone );
        editTextEmail = (EditText)findViewById( R.id.editTextEmail );
        editTextCreditCard = (EditText)findViewById( R.id.editTextCreditCard );
        editTextPassword = (EditText)findViewById( R.id.editTextPassword );
        buttonSignUp = (Button)findViewById( R.id.buttonSignUp );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViews();
    }


    void signUpClick(View view){

        checkData();

    }



    protected Driver getDriver() {
        Driver driver = new Driver();

        driver.set_Id(editTextID.getText().toString());
        driver.setFirstName(editTextName.getText().toString());
        driver.setLastName(editTextLastName.getText().toString());
        driver.setPhoneNumber(editTextPhone.getText().toString());
        driver.setEmail(editTextEmail.getText().toString());
        driver.setCreditCard(editTextCreditCard.getText().toString());
        driver.setPassword(editTextPassword.getText().toString());

        return  driver;
    }

   void goToActivity2(){
       Intent intent = new Intent(this, MainActivity.class);//when the user click the button login the intent call to new activity
       startActivity(intent);//start the activity
    }

    protected  void addDriver(final Driver driver) {

            buttonSignUp.setEnabled(false);
            final IDataBase dataBase = FactoryDataBase.getDataBase();

            new AsyncTask<Context, Void, Void>() {

                @Override
                protected Void doInBackground(Context... contexts) {
                    try {
                        dataBase.addDriver(driver, new IDataBase.Action() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getBaseContext(), "ההרשמה בוצעה בהצלחה", Toast.LENGTH_LONG).show();
                                //    buttonSignUp.setEnabled(true);
                                goToActivity2();
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                Toast.makeText(getBaseContext(), "ההרשמה נכשלה", Toast.LENGTH_LONG).show();
                                // buttonSignUp.setEnabled(true);
                            }

                            @Override
                            public void onProgress(String status, double percent) {
                                //if (percent != 100)
                                // buttonSignUp.setEnabled(false);
                            }
                        });
                        return null;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }
            }.execute();

    }


    /***
     * thisfunc check the correct of email input
     * @param editText email text
     * @returnbtrue if currect input, false if null or uncorrect mail address
     */
    public boolean isEmail(EditText editText) {
        CharSequence email= editText.getText().toString();
        if (email == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    /***
     * thisfunc check the correct of name input
     * @param editText name text
     * @returnbtrue if currect input, false if null
     */
    public boolean isName(EditText editText) {
        CharSequence name= editText.getText().toString();
        return (!TextUtils.isEmpty(name));

    }

    /***
     * thisfunc check the correct of phone input
     * @param editText phone text
     * @returnbtrue if currect input, false if null or uncorrect phone's digits
     */
    public boolean isPhone(EditText editText) {
        CharSequence phone= editText.getText().toString();
        if (phone == null)
            return false;
        return (phone.length() ==10);

    }


    public boolean isCredit(EditText editText) {
        CharSequence credit= editText.getText().toString();
        if (credit == null)
            return false;
        return (credit.length() ==16);

    }

    /***
     * this func check the input of drive before she add to data base
     */
    void checkData(){

        if(!isName(editTextName))//check nae input
            editTextName.setError("name is reqired");
        else  if(!isName(editTextLastName))//check nae input
            editTextLastName.setError("last name is reqired");
        else if(!isPhone(editTextID))//check phone input
            editTextID.setError("Enter correct ID with 10 digits");
        else if(!isPhone(editTextPhone))//check phone input
            editTextPhone.setError("Enter correct phone number with 10 digits");
        else if(!isEmail(editTextEmail))//check email input
            editTextEmail.setError("Enter valid email");
        else if(!isCredit(editTextCreditCard))//check phone input
            editTextCreditCard.setError("Enter correct credit card with 16 digits");
        else if(!isName(editTextPassword))//check phone input
            editTextPassword.setError("password is reqired");

        else//input correct
        {
            Driver driver = getDriver();
            addDriver(driver);
        }

    }
}
