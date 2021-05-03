package com.bessarez.ecommercemobile.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductListener;
import com.bessarez.ecommercemobile.interfaces.OnSearchSuggestionListener;
import com.bessarez.ecommercemobile.ui.models.SearchSuggestion;

import java.util.List;

import static com.bessarez.ecommercemobile.ui.dialogs.ProductOrderQuantityDialog.TAG;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder> {

    private List<SearchSuggestion> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    private static int TYPE_RECENT_SEARCH = 1;
    private static int TYPE_SEARCH_SUGGESTION = 2;

    private OnSearchSuggestionListener mOnSearchSuggestionListener;

    public SearchSuggestionAdapter(List<SearchSuggestion> mList, Context mContext, OnSearchSuggestionListener mOnSearchSuggestionListener) {
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mOnSearchSuggestionListener = mOnSearchSuggestionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.card_recommended_search, parent, false);

        return new ViewHolder(v,mOnSearchSuggestionListener);
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

        TextView name;
        ImageButton ib_replace_text;
        OnSearchSuggestionListener onSearchSuggestionListener;

        public ViewHolder(@NonNull View itemView, OnSearchSuggestionListener onSearchSuggestionListener) {
            super(itemView);
            this.onSearchSuggestionListener = onSearchSuggestionListener;
            name = itemView.findViewById(R.id.tv_recommended_search);

            ib_replace_text = itemView.findViewById(R.id.ib_replace_text);
            ib_replace_text.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSearchSuggestionListener.onSuggestionClick(v,getAdapterPosition());
        }
    }
}
