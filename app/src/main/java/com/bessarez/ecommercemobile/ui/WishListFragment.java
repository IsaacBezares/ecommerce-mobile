package com.bessarez.ecommercemobile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bessarez.ecommercemobile.R;

public class WishListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wish_list, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        return root;
    }
}