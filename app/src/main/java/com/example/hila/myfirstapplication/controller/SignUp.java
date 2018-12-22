package com.example.hila.myfirstapplication.controller;

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

        Driver driver = getDriver();
        addDriver(driver);

    }



    protected Driver getDriver() {
        Driver driver = new Driver();

        driver.set_Id(Integer.parseInt(editTextID.getText().toString()));
        driver.setFirstName(editTextName.getText().toString());
        driver.setLastName(editTextLastName.getText().toString());
        driver.setPhoneNumber(Integer.parseInt(editTextPhone.getText().toString()));
        driver.setEmail(editTextEmail.getText().toString());
        driver.setCreditCard(Integer.parseInt(editTextCreditCard.getText().toString()));
        driver.setPassword(editTextPassword.getText().toString());

        return  driver;
    }
    protected  void addDriver(Driver driver) {
        try {
            buttonSignUp.setEnabled(false);
            IDataBase dataBase = FactoryDataBase.getDataBase();
            dataBase.addDriver(driver, new IDataBase.Action() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getBaseContext(), "ההרשמה בוצעה בהצלחה", Toast.LENGTH_LONG).show();
                    buttonSignUp.setEnabled(true);
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "ההרשמה נכשלה", Toast.LENGTH_LONG).show();
                    buttonSignUp.setEnabled(true);
                }

                @Override
                public void onProgress(String status, double percent) {
                    if( percent != 100)
                        buttonSignUp.setEnabled(false);
                }
            });
        } catch (Exception e){
            Toast.makeText(getBaseContext(), "Error \n", Toast.LENGTH_LONG).show();
            buttonSignUp.setEnabled(true);
        }


    }

}
