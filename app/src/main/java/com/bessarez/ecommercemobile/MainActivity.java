package com.bessarez.ecommercemobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);

        if (isLogged()) {
            SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

            TextView tvhello = header.findViewById(R.id.hello);
            tvhello.setText(getString(R.string.hello_comma) + " " + preferences.getString("firstName", ""));
            tvhello.setVisibility(View.VISIBLE);
        } else {
            TextView tvLogin = header.findViewById(R.id.login_to_account);
            Button btnLogin = header.findViewById(R.id.login);

            tvLogin.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);

            btnLogin.setOnClickListener(this);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_orders, R.id.nav_wish_list, R.id.nav_settings, R.id.nav_product, R.id.nav_search_result, R.id.nav_cart)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean isLogged(){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "default");
        return !token.equals("default");
    }

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigateUp();
        navController.navigate(R.id.nav_login);
    }
}