package com.bessarez.ecommercemobile.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductClickListener;
import com.bessarez.ecommercemobile.models.apimodels.ApiProducts;
import com.bessarez.ecommercemobile.ui.adapters.ProductAdapter;
import com.bessarez.ecommercemobile.ui.models.CardProduct;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class SearchResultFragment extends Fragment implements OnProductClickListener {

    private ArrayList<CardProduct> resultProducts;
    private ProductAdapter productAdapter;

    private RelativeLayout loadingScreen;
    private RelativeLayout emptyScreen;
    private NestedScrollView loadedScreen;

    private boolean isDataLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDataLoaded = false;
        getSearchResults();
    }

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

        loadRecycler(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataLoaded){
            if (!resultProducts.isEmpty()) {
                setScreenVisibility(false,true,false);
            } else {
                setScreenVisibility(false,false,true);
            }
        }
    }

    private void loadRecycler(View view) {
        productAdapter = new ProductAdapter(resultProducts, getContext(), this);
        RecyclerView recyclerView = view.findViewById(R.id.rv_recent_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);
    }

    private void getSearchResults() {

        String query;
        if (getArguments() != null) {
            query = SearchResultFragmentArgs.fromBundle(getArguments()).getQuery();
        } else {
            return;
        }

        resultProducts = new ArrayList<>();

        Call<ApiProducts> call = getApiService().getSearchResults(query);
        call.enqueue(new Callback<ApiProducts>() {
            @Override
            public void onResponse(Call<ApiProducts> call, Response<ApiProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiProducts apiProducts = response.body();

                isDataLoaded = true;

                if (apiProducts.getEmbedded() == null) {
                    setScreenVisibility(false,false,true);
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

                    productAdapter.notifyDataSetChanged();

                    setScreenVisibility(false,true,false);
                }
            }

            @Override
            public void onFailure(Call<ApiProducts> call, Throwable t) {

            }
        });
    }

    @Override
    public void onProductClick(View v, CardProduct cardProduct) {
        SearchResultFragmentDirections.ActionNavSearchResultToNavProduct action =
                SearchResultFragmentDirections.actionNavSearchResultToNavProduct(cardProduct.getId());
        Navigation.findNavController(getView()).navigate(action);
    }

    private void setScreenVisibility(boolean loading, boolean loaded, boolean empty) {
        if (loading)
            loadingScreen.setVisibility(View.VISIBLE);
        else
            loadingScreen.setVisibility(View.GONE);

        if (loaded)
            loadedScreen.setVisibility(View.VISIBLE);
        else
            loadedScreen.setVisibility(View.GONE);

        if (empty)
            emptyScreen.setVisibility(View.VISIBLE);
        else
            emptyScreen.setVisibility(View.GONE);
    }
}