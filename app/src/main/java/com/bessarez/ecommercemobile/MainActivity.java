package com.bessarez.ecommercemobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.ui.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_orders, R.id.nav_wish_list, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {

        super.onStart();
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "default");
        boolean isLogged;
        if (token.equals("default"))
            isLogged = false;
        else
            isLogged = true;

        setAppHeaderWelcomeText(isLogged, preferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private MainActivity getMainClass() {
        return this;
    }

    private void setAppHeaderWelcomeText(boolean isLogged, SharedPreferences preferences) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView usernameOrLogin = (TextView) headerView.findViewById(R.id.username_or_login);

        if (isLogged) {
            String firstName = preferences.getString("firstName", "default");
            usernameOrLogin.setText("Hello, " + firstName);
        } else {
            usernameOrLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getMainClass(), LoginActivity.class);
                    startActivity(myIntent);
                }
            });
            usernameOrLogin.setText("Hello, Log in :)");
        }
    }

}