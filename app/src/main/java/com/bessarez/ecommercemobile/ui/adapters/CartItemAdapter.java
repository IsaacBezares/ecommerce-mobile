package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.ui.models.CardCartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CardCartItem> itemList;
    private Context context;

    private OnItemClickListener listener;

    public CartItemAdapter(List<CardCartItem> itemList, Context context, OnItemClickListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView ivProduct;
        protected TextView tvTitle, tvPrice, tvDelete;
        protected AppCompatButton btnQuantity;

        protected OnItemClickListener onItemClickListener;

        public CartItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvTitle = itemView.findViewById(R.id.tv_product_title);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvDelete = itemView.findViewById(R.id.tv_delete_item);
            btnQuantity = itemView.findViewById(R.id.btn_quantity);

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
            tvDelete.setOnClickListener(this);
            btnQuantity.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_cart_item, parent, false);
        return new CartItemViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        String imageUrl = itemList.get(position).getImageUrl();
        String title = itemList.get(position).getTitle();
        int quantity = itemList.get(position).getQuantity();
        double price = itemList.get(position).getPrice();

        Picasso.get().load(imageUrl).into(holder.ivProduct);
        holder.tvTitle.setText(title);
        holder.btnQuantity.setText(String.valueOf(quantity));
        holder.tvPrice.setText(String.valueOf(price));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
