package com.example.hila.myfirstapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hila.myfirstapplication.R;
import com.example.hila.myfirstapplication.model.backend.FactoryDataBase;
import com.example.hila.myfirstapplication.model.backend.IDataBase;


/**
 * this class represent the login first activity
 */
public class MainActivity extends Activity {

    //the objects of the screen
    TextView textView;
    EditText emailText;
    EditText passwordText;
    Button registerButton;
    TextView textView2;
    SharedPreferences sharedpreferences;
    IDataBase dataBase;

    /**
     * this func create the activity
     *
     * @param savedInstanceState represent the jump of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Set the activity content from a layout resource
        findViews();

        sharedpreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        DataFromSharedPreferences();//keeps the details of the driver for next activities
    }

    /**
     * This func connect the objects by their id
     */
    private void findViews() {
        textView = (TextView) findViewById(R.id.textView);
        emailText = (EditText) findViewById(R.id.editTextEmail);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        textView2 = (TextView) findViewById(R.id.textViewSign);
        dataBase = FactoryDataBase.getDataBase();


    }

    /***
     * This function get the details of the driver when we connect back
     */
    protected void DataFromSharedPreferences() {
        if (sharedpreferences.contains("email"))//under mail ref
            emailText.setText(sharedpreferences.getString("email", ""));//put in text view the mail
        if (sharedpreferences.contains("password"))//under passwor ref
            passwordText.setText(sharedpreferences.getString("password", ""));//put in password view the password
    }

    /***
     * This function store the details of the driver to retrieve it in the next activities.
     */
    protected void DataInSharedPreferences() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", emailText.getText().toString());//store in mail ref
        editor.putString("password", passwordText.getText().toString());//store in password ref
        editor.commit();//to add the change
    }

    /***
     * This function calls the activity of sign up by intent
     * @param view the string that clicked
     */
    protected void GosignUpActivity(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUp.class);//create signUp intent
        startActivity(intent);//go to intent
    }

    /***
     * This function calls the profile activity by intent after we check th details
     */
    protected void GoProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), Profile.class);//create profile intent
        startActivity(intent);//go to intent
    }

    /***
     *this function check all the details the user input and if the check in firebase correct- call to open new activity
     * @param view the login buttom that active the function
     */
    protected void LoginButton(View view) {
        try {


            registerButton.setEnabled(false);
            IDataBase.Action action = new IDataBase.Action() {//how to act when you go to database
                @Override
                public void onSuccess() {//the user exist in system
                    registerButton.setEnabled(true);
                    DataInSharedPreferences();//save the data in share prefrences to the next enter
                    GoProfileActivity();//open profile activity
                }

                @Override
                public void onFailure(Exception exception) {// error with undefine nail or password
                    registerButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), exception.getMessage().toString(), Toast.LENGTH_LONG).show();//toast thr problem
                }

                @Override
                public void onProgress(String status, double percent) {//check in progress
                    if (percent != 100)
                        registerButton.setEnabled(false);
                }
            };
            dataBase.isValidDriverAuthentication(emailText.getText().toString(), passwordText.getText().toString(), action);//check if user valid
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            registerButton.setEnabled(true);

        }
    }
}

