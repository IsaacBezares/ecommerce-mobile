package com.bessarez.ecommercemobile.ui;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.models.RegisteredUser;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup1Fragment extends Fragment {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_signup1, container, false);

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);

        AppCompatButton btnContinue = view.findViewById(R.id.btn_continue_signup);
        btnContinue.setOnClickListener(v -> {

            if (isFormFulfilled()){

                RegisteredUser newUser = new RegisteredUser();
                newUser.setFirstName(etFirstName.getText().toString());
                newUser.setLastName(etLastName.getText().toString());
                newUser.setPhone(etPhone.getText().toString());
                newUser.setEmail(etEmail.getText().toString());
                newUser.setPassword(etPassword.getText().toString());

                Call<RegisteredUser> call = getApiService().signup(newUser);
                call.enqueue(new Callback<RegisteredUser>() {
                    @Override
                    public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                        if (!response.isSuccessful()){
                            Log.d(TAG, "onResponse: Algo falló");
                            return;
                        }

                        NavDirections action = Signup1FragmentDirections.actionNavSignup1ToNavLogin();
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onFailure(Call<RegisteredUser> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                CharSequence text =  "You must fill all fields";
                Toast toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
                toast.show();
            }

        });

        return view;
    }

    private boolean isFormFulfilled(){
        return ! (etFirstName.getText().toString().isEmpty() || etLastName.getText().toString().isEmpty()
                || etPhone.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty()
                || etPassword.getText().toString().isEmpty());
    }
}