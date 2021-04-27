package com.bessarez.ecommercemobile.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bessarez.ecommercemobile.R;

public class ProductOrderQuantityDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    public interface OnQuantityListener {
        void sendQuantity(Integer quantity);
    }

    public static String TAG = "ProductOrderQuantityDialog";

    public OnQuantityListener onQuantityListener;

    private ListView rvQuantityList;
    private int maxQuantity;

    public ProductOrderQuantityDialog(int stock) {
        super();
        this.maxQuantity = Math.min(stock, 12);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onQuantityListener = (OnQuantityListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement OnQuantityListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_product_order_quantity, null);

        builder.setView(view);

        loadList(view);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.product_order_quantity_width);
        int height = getResources().getDimensionPixelSize(R.dimen.product_order_quantity_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer quantity = (Integer) parent.getItemAtPosition(position);
        onQuantityListener.sendQuantity(quantity);
        getDialog().dismiss();
    }

    private void loadList(View view) {
        rvQuantityList = view.findViewById(R.id.lv_quantity_list);

        Integer[] quantities = new Integer[maxQuantity];
        for (int i = 0; i < maxQuantity; i++) {
            quantities[i] = i+1;
        }

        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, quantities);
        rvQuantityList.setAdapter(quantityAdapter);
        rvQuantityList.setOnItemClickListener(this);
    }
}
