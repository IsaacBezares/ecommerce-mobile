package com.bessarez.ecommercemobile.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.models.apimodels.ApiProducts;
import com.bessarez.ecommercemobile.ui.adapters.CardProductAdapter;
import com.bessarez.ecommercemobile.ui.models.CardProduct;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class SearchResultFragment extends Fragment implements OnItemClickListener {

    private ArrayList<CardProduct> resultProducts;
    private CardProductAdapter cardProductAdapter;

    private RelativeLayout loadingScreen;
    private RelativeLayout emptyScreen;
    private ScrollView loadedScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingScreen = view.findViewById(R.id.loading_layout);
        loadedScreen = view.findViewById(R.id.loaded_layout);
        emptyScreen = view.findViewById(R.id.empty_layout);

        resultProducts = new ArrayList<>();
        cardProductAdapter = new CardProductAdapter(resultProducts, getContext(), this);

        String query;
        if (getArguments() != null) {
            query = SearchResultFragmentArgs.fromBundle(getArguments()).getQuery();
        } else {
            return;
        }

        Call<ApiProducts> call = getApiService().getSearchResults(query);
        call.enqueue(new Callback<ApiProducts>() {
            @Override
            public void onResponse(Call<ApiProducts> call, Response<ApiProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiProducts apiProducts = response.body();

                if (apiProducts.getEmbedded() == null) {
                    loadingScreen.setVisibility(View.GONE);
                    emptyScreen.setVisibility(View.VISIBLE);
                }

                if (apiProducts.getEmbedded() != null) {
                    for (com.bessarez.ecommercemobile.models.Product product : apiProducts.getEmbeddedServices()) {
                        resultProducts.add(new CardProduct(
                                product.getId(),
                                product.getImageUrl(),
                                product.getName(),
                                String.valueOf(product.getPrice() / 100.0)
                        ));
                    }

                    cardProductAdapter.notifyDataSetChanged();

                    loadingScreen.setVisibility(View.GONE);
                    loadedScreen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiProducts> call, Throwable t) {

            }
        });

        loadRecycler(view);
    }

    private void loadRecycler(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.rv_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cardProductAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Long productId = resultProducts.get(position).getId();
        SearchResultFragmentDirections.ActionNavSearchResultToNavProduct action = SearchResultFragmentDirections.actionNavSearchResultToNavProduct(productId);
        Navigation.findNavController(getView()).navigate(action);
    }
}