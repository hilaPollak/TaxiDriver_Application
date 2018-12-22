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

public class MainActivity extends Activity {


    TextView textView;
    EditText emailText;
    EditText passwordText;
    Button registerButton;
    TextView textView2;
    SharedPreferences sharedpreferences;
    IDataBase dataBase;


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-12-20 19:54:47 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        textView = (TextView)findViewById( R.id.textView );
        emailText = (EditText)findViewById( R.id.editTextEmail );
        passwordText = (EditText)findViewById( R.id.editTextPassword );
        registerButton = (Button)findViewById( R.id.buttonRegister );
        textView2 = (TextView)findViewById( R.id.textViewSign );
        dataBase = FactoryDataBase.getDataBase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        sharedpreferences = getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        DataFromSharedPreferences();

    }


    protected void DataFromSharedPreferences(){
        if (sharedpreferences.contains("email"))
            emailText.setText(sharedpreferences.getString("email", ""));
        if (sharedpreferences.contains("password"))
            passwordText.setText(sharedpreferences.getString("password", ""));
    }


    protected void DataInSharedPreferences(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", emailText.getText().toString());
        editor.putString("password", passwordText.getText().toString());
        editor.commit();


    }


    protected void GosignUpActivity(View view)
    {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    protected void LoginButton(View view)
    {
        try {


            registerButton.setEnabled(false);
            IDataBase.Action action = new IDataBase.Action() {
                @Override
                public void onSuccess() {
                    registerButton.setEnabled(true);
                    DataInSharedPreferences();
                    Toast.makeText(getBaseContext(), "ההתחברות בוצעה בהצלחה", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception exception) {
                    registerButton.setEnabled(true);
                    Toast.makeText(getBaseContext(), exception.getMessage().toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(String status, double percent) {
                    if (percent != 100)
                        registerButton.setEnabled(false);
                }
            };
            dataBase.isValidDriverAuthentication(emailText.getText().toString(), passwordText.getText().toString(), action);
        }catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            registerButton.setEnabled(true);
        }
    }
}
