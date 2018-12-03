package com.example.android.filmmein;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {
    /*********************************************
     * A class that simply shows details of the  *
     * app, accessible by menu from main activity*
     *********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
