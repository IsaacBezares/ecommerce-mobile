package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.ui.models.CardCheckoutItem;

import java.util.List;

public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.CheckoutItemViewHolder> {

    private List<CardCheckoutItem> itemList;

    private Context context;

    public CheckoutItemAdapter(Context context, List<CardCheckoutItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    class CheckoutItemViewHolder extends RecyclerView.ViewHolder{

        protected TextView tvItemQuantity, tvProductName, tvProductCurrency, tvProductPrice;

        public CheckoutItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductCurrency = itemView.findViewById(R.id.tv_product_currency);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }

    @NonNull
    @Override
    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_checkout_item, parent, false);
        return new CheckoutItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position) {
        int quantity = itemList.get(position).getQuantity();
        String name = itemList.get(position).getName();
        String currency = itemList.get(position).getCurrency();
        Long price = itemList.get(position).getPrice();

        holder.tvItemQuantity.setText(String.valueOf(quantity));
        holder.tvProductName.setText(name);
        holder.tvProductCurrency.setText(currency);
        holder.tvProductPrice.setText(String.valueOf(price / 100.0));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
