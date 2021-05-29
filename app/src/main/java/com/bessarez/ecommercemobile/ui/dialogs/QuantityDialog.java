package com.bessarez.ecommercemobile.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bessarez.ecommercemobile.R;

public class QuantityDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    public interface OnQuantityListener {
        void sendQuantity(Integer quantity, Integer position);
    }

    public static String TAG = "QuantityDialog";

    public OnQuantityListener onQuantityListener;

    private ListView lvQuantityList;
    private Integer maxQuantity;
    private Integer position;

    /**
     *
     * @param stock Stock of given item
     * @param position Used if dialog is opened from recycler item to update quantity of given item
     */

    public QuantityDialog(Integer stock, Integer position) {
        super();
        this.maxQuantity = Math.min(stock, 12);
        this.position = position;
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
        View view = inflater.inflate(R.layout.dialog_quantity, null);

        builder.setView(view);

        loadList(view);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.quantity_dialog_width);
        int height = maxHeightOrListHeight();
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer quantity = (Integer) parent.getItemAtPosition(position);
        onQuantityListener.sendQuantity(quantity, this.position);
        Log.d(TAG, "view height: " + view.getHeight());
        Log.d(TAG, "adapterview height: " + parent.getHeight());
        getDialog().dismiss();
    }

    private void loadList(View view) {
        lvQuantityList = view.findViewById(R.id.lv_quantity_list);

        Integer[] quantities = new Integer[maxQuantity];
        for (int i = 0; i < maxQuantity; i++) {
            quantities[i] = i+1;
        }

        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, quantities);
        lvQuantityList.setAdapter(quantityAdapter);
        lvQuantityList.setOnItemClickListener(this);
    }

    /**
     * Hardcoded: giving 200 for every item and 40 for header
     */
    private int maxHeightOrListHeight(){
        int maxHeight = getResources().getDimensionPixelSize(R.dimen.quantity_dialog_height);
        int listViewHeight = 200 * maxQuantity + 40;
        return Math.min(maxHeight,listViewHeight);
    }
}
