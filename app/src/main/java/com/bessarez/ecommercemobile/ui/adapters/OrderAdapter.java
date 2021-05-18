package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductListener;
import com.bessarez.ecommercemobile.ui.models.CardOrder;
import com.bessarez.ecommercemobile.ui.models.CardOrderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    List<ListOrderItem> consolidatedList = new ArrayList<>();

    private OnProductListener mOnProductListener;

    public OrderAdapter(Context mContext, List<ListOrderItem> consolidatedList, OnProductListener onProductListener) {
        this.mContext = mContext;
        this.consolidatedList = consolidatedList;
        this.mOnProductListener = onProductListener;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_date, tv_total_amount, tv_order_id;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_date = itemView.findViewById(R.id.tv_date);
            this.tv_total_amount = itemView.findViewById(R.id.tv_total_amount);
            this.tv_order_id = itemView.findViewById(R.id.tv_order_id);
        }
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView iv_product;

        protected TextView tv_product_title;

        protected OnProductListener onProductListener;

        public OrderItemViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            this.iv_product = itemView.findViewById(R.id.iv_product);
            this.tv_product_title = itemView.findViewById(R.id.tv_product_title);
            this.onProductListener = onProductListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListOrderItem.TYPE_ORDER:
                View v1 = inflater.inflate(R.layout.card_order, parent, false);
                viewHolder = new OrderViewHolder(v1);
                break;
            case ListOrderItem.TYPE_ORDER_ITEM:
                View v2 = inflater.inflate(R.layout.card_order_product, parent, false);
                viewHolder = new OrderItemViewHolder(v2, mOnProductListener);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case ListOrderItem.TYPE_ORDER:

                CardOrder cardOrder = (CardOrder) consolidatedList.get(position);
                OrderViewHolder orderHolder = (OrderViewHolder) holder;

                orderHolder.tv_date.setText(cardOrder.getOrderedAt().toString());
                orderHolder.tv_total_amount.setText(String.valueOf(cardOrder.getTotalAmount()));
                orderHolder.tv_order_id.setText(String.valueOf(cardOrder.getId()));
                break;

            case ListOrderItem.TYPE_ORDER_ITEM:
                CardOrderItem cardOrderItem = (CardOrderItem) consolidatedList.get(position);
                OrderItemViewHolder orderItemHolder = (OrderItemViewHolder) holder;

                Picasso.get().load(cardOrderItem.getImageUrl()).into(orderItemHolder.iv_product);
                orderItemHolder.tv_product_title.setText(cardOrderItem.getName());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }
}
