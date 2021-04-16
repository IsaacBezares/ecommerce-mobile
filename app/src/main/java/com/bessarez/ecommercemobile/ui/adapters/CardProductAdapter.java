package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardProductAdapter extends RecyclerView.Adapter<CardProductAdapter.ViewHolder> {
    private List<CardProduct> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    public CardProductAdapter(List<CardProduct> mData, Context mContext) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CardProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_product, parent, false);
        return new CardProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardProductAdapter.ViewHolder holder, int position) {
        String textImg = mData.get(position).getImage();
        String textTitle = mData.get(position).getTitle();
        String textPrice = "$ " + mData.get(position).getPrice();

        Picasso.get().load(textImg).into(holder.image);
        holder.title.setText(textTitle);
        holder.price.setText(textPrice);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView image;
        AppCompatTextView title, price;

        ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.iv_product);
            title = itemView.findViewById(R.id.tv_product_title);
            price = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
