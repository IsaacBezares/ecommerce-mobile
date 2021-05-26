package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bessarez.ecommercemobile.MainActivity;
import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.apimodels.ApiLogin;
import com.bessarez.ecommercemobile.models.apimodels.ApiToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class LoginFragment extends Fragment {

    private AppCompatButton btnLogin;
    private EditText etEmail, etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = view.findViewById(R.id.btnLogin);
        etEmail = view.findViewById(R.id.etEmailLogin);
        etPassword = view.findViewById(R.id.etPasswordLogin);
        TextView tvCreateAccount = view.findViewById(R.id.tvCreateAccount);

        btnLogin.setOnClickListener(v ->  login(etEmail.getText().toString(), etPassword.getText().toString()));

        tvCreateAccount.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionNavLoginToNavSignup1();
            Navigation.findNavController(view).navigate(action);
        });

        return view;
    }

    private void saveUserData(String token, String email) {
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
                Long userId = loggedUser.getId();
                SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token);
                editor.putString("email", email);
                editor.putString("firstName", firstName);
                editor.putLong("userId", userId);
                editor.commit();
                reloadActivity();
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void login(String email, String password){
        Call<ApiToken> call = getApiService().login(new ApiLogin(email, password));
        call.enqueue(new Callback<ApiToken>() {
            @Override
            public void onResponse(Call<ApiToken> call, Response<ApiToken> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    if (token.split("\\.").length == 3) {
                        btnLogin.setEnabled(false);
                        btnLogin.setClickable(false);
                        saveUserData(token, email);
                    }
                } else {
                    CharSequence text =  "Inavlid credentials";
                    Toast toast = Toast.makeText(getContext().getApplicationContext(),text,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ApiToken> call, Throwable t) {
                System.out.println("Algo falló");
                t.printStackTrace();
            }
        });
    }

    private void reloadActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}