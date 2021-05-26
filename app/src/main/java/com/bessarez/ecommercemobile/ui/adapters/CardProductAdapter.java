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
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.ui.models.CardProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardProductAdapter extends RecyclerView.Adapter<CardProductAdapter.ViewHolder> {
    private List<CardProduct> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public CardProductAdapter(List<CardProduct> mData, Context mContext, OnItemClickListener onItemClickListener) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mContext = mContext;
        this.mOnItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_product, parent, false);
        return new ViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView image;
        AppCompatTextView title, price;
        OnItemClickListener onItemClickListener;

        ViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);
            image = itemView.findViewById(R.id.iv_product);
            title = itemView.findViewById(R.id.tv_product_title);
            price = itemView.findViewById(R.id.tv_product_price);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
