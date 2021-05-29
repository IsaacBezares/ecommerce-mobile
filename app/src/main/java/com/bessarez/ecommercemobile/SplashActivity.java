package com.bessarez.ecommercemobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bessarez.ecommercemobile.ui.WarningFragment;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        setContentView(R.layout.splash_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WarningFragment.newInstance())
                    .commitNow();
        }
    }
}