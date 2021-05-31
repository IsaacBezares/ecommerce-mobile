package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductClickListener;
import com.bessarez.ecommercemobile.ui.models.CardProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<CardProduct> productList;
    private LayoutInflater inflater;

    private OnProductClickListener onProductClickListener;

    public ProductAdapter(List<CardProduct> productList, Context context, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.inflater = LayoutInflater.from(context);
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_product, parent, false);
        return new ViewHolder(view, onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String textImg = productList.get(position).getImage();
        String textTitle = productList.get(position).getTitle();
        String textPrice = "$ " + productList.get(position).getPrice();

        Picasso.get().load(textImg).into(holder.image);
        holder.title.setText(textTitle);
        holder.price.setText(textPrice);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView image;
        AppCompatTextView title, price;
        OnProductClickListener onProductClickListener;

        ViewHolder(View itemView, OnProductClickListener onProductClickListener){
            super(itemView);
            image = itemView.findViewById(R.id.iv_product);
            title = itemView.findViewById(R.id.tv_product_title);
            price = itemView.findViewById(R.id.tv_product_price);
            this.onProductClickListener = onProductClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onProductClickListener.onProductClick(v, productList.get(getAdapterPosition()));
        }
    }
}
