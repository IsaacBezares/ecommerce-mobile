package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.ui.models.SearchSuggestion;

import java.util.List;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder> {

    private List<SearchSuggestion> mList;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    public SearchSuggestionAdapter(List<SearchSuggestion> mList, Context mContext, OnItemClickListener mOnItemClickListener) {
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.card_recommended_search, parent, false);

        return new ViewHolder(v,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String textSuggestion = mList.get(position).getName();
        holder.name.setText(textSuggestion);
        if (mList.get(position).getType() == 1) {
            holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_history, 0, 0, 0);
        } else {
            holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView name;
        protected ImageButton ib_replace_text;
        protected OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onitemClickListener) {
            super(itemView);
            this.onItemClickListener = onitemClickListener;
            name = itemView.findViewById(R.id.tv_recommended_search);

            ib_replace_text = itemView.findViewById(R.id.ib_replace_text);
            ib_replace_text.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
