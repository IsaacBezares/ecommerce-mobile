package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.bessarez.ecommercemobile.MainActivity;
import com.bessarez.ecommercemobile.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String email = preferences.getString("email", "default");

        if (email.equals("default")) {
            setPreferencesFromResource(R.xml.logged_out_preferences, rootKey);
        } else {
            setPreferencesFromResource(R.xml.logged_in_preferences, rootKey);
            Preference btnLogout = findPreference(getString(R.string.logout));
            btnLogout.setSummary(email);
            btnLogout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);
                    getActivity().finish();
                    return true;
                }
            });
        }
    }
}