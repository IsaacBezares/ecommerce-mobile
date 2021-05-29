package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.models.apimodels.ApiProduct;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProducts;
import com.bessarez.ecommercemobile.ui.models.CardProduct;
import com.bessarez.ecommercemobile.ui.adapters.CardProductAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class WishListFragment extends Fragment implements OnItemClickListener {

    private List<CardProduct> products;
    private CardProductAdapter cardProductAdapter;

    private RelativeLayout loadingScreen;
    private RelativeLayout emptyScreen;
    private ScrollView loadedScreen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isUserLoggedIn()) {
            navigateWithAction(WishListFragmentDirections.actionNavWishListToNavLogin());
        }

        loadingScreen = view.findViewById(R.id.loading_layout);
        loadedScreen = view.findViewById(R.id.loaded_layout);
        emptyScreen = view.findViewById(R.id.empty_layout);

        products = new ArrayList<>();
        cardProductAdapter = new CardProductAdapter(products, getContext(), this::onItemClick);

        Call<ApiWishProducts> call = getApiService().getWishListProducts(getUserIdFromPreferences());
        call.enqueue(new Callback<ApiWishProducts>() {
            @Override
            public void onResponse(Call<ApiWishProducts> call, Response<ApiWishProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiWishProducts apiProducts = response.body();

                if (apiProducts.getEmbedded() == null) {
                    loadingScreen.setVisibility(View.GONE);
                    emptyScreen.setVisibility(View.VISIBLE);
                    return;
                }

                for (ApiProduct product : apiProducts.getEmbeddedServices()) {
                    products.add(new CardProduct(
                            product.getId(),
                            product.getImageUrl(),
                            product.getName(),
                            String.valueOf(product.getPrice() / 100.0)
                    ));
                    cardProductAdapter.notifyDataSetChanged();
                }

                loadingScreen.setVisibility(View.GONE);
                loadedScreen.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ApiWishProducts> call, Throwable t) {

            }
        });

        loadRecycler(view);
    }

    private void loadRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_wish_list_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cardProductAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Long productId = products.get(position).getId();
        WishListFragmentDirections.ActionNavWishListToNavProduct action = WishListFragmentDirections.actionNavWishListToNavProduct(productId);
        Navigation.findNavController(getView()).navigate(action);
    }

    private boolean isUserLoggedIn() {
        return getUserIdFromPreferences() != 0;
    }

    private Long getUserIdFromPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        return preferences.getLong("userId", 0);
    }

    private void navigateWithAction(NavDirections action) {
        Navigation.findNavController(getView()).navigate(action);
    }
}