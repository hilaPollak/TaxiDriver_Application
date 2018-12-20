package com.example.hila.myfirstapplication.controller;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hila.myfirstapplication.R;

public class MainActivity extends Activity {


    private TextView textView;
    private EditText editText;
    private EditText editText2;
    private Button button2;
    private TextView textView2;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-12-20 19:54:47 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        textView = (TextView)findViewById( R.id.textView );
        editText = (EditText)findViewById( R.id.editText );
        editText2 = (EditText)findViewById( R.id.editText2 );
        button2 = (Button)findViewById( R.id.button2 );
        textView2 = (TextView)findViewById( R.id.textView2 );


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }
    protected void GosignUpActivity(View view)
    {
        Toast.makeText(getBaseContext(), "check", Toast.LENGTH_LONG).show();
    }

    protected void LoginButton(View view)
    {
        Toast.makeText(getBaseContext(), "check", Toast.LENGTH_LONG).show();
    }
}
