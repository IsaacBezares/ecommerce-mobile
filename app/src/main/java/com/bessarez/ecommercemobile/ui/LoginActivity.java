package com.bessarez.ecommercemobile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bessarez.ecommercemobile.MainActivity;
import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.apimodels.RequestLogin;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiLogin;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton btnLogin;
    private EditText etEmail, etPassword;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                Call<ResponseApiLogin> call = getApiService().login(new RequestLogin(email, password));
                call.enqueue(new Callback<ResponseApiLogin>() {
                    @Override
                    public void onResponse(Call<ResponseApiLogin> call, Response<ResponseApiLogin> response) {
                        if (response.isSuccessful()) {
                            String token = response.body().getToken();
                            System.out.println(response.body().getToken());
                            if (token.split("\\.").length == 3) {
                                btnLogin.setEnabled(false);
                                btnLogin.setClickable(false);
                                savePreferences(token, email);
                                saveUserData();
                            }
                        } else {
                            CharSequence text =  "Inavlid credentials";
                            Toast toast = Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseApiLogin> call, Throwable t) {
                        System.out.println("Algo falló");
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private void savePreferences(String token, String email) {
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.putString("email", email);
        editor.commit();
    }

    private void saveUserData() {
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "default");
        String email = preferences.getString("email", "default");

        Call<RegisteredUser> call = getApiService().getRegisteredUserByEmail(email, token);
        call.enqueue(new Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                RegisteredUser loggedUser = response.body();
                String firstName = loggedUser.getFirstName();
                SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("firstName", firstName);
                editor.commit();
                loadMainActivity();
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
            }
        });
    }

    private void loadMainActivity() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myIntent);
        finish();
    }
}

/*

 */