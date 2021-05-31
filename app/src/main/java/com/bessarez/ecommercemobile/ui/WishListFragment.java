package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductClickListener;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProducts;
import com.bessarez.ecommercemobile.ui.models.CardProduct;
import com.bessarez.ecommercemobile.ui.adapters.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class WishListFragment extends Fragment implements OnProductClickListener {

    private List<CardProduct> products;
    private ProductAdapter productAdapter;

    private RelativeLayout loadingScreen;
    private RelativeLayout emptyScreen;
    private NestedScrollView loadedScreen;

    private boolean isDataLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isUserLoggedIn()) {
            isDataLoaded = false;
            getWishList();
        } else {
            navigateWithAction(WishListFragmentDirections.actionNavWishListToNavLogin());
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
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
        if (isDataLoaded) {
            if (!products.isEmpty()) {
                setScreenVisibility(false, true, false);
            } else {
                setScreenVisibility(false, false, true);
            }
        }
    }

    private void loadRecycler(View view) {
        productAdapter = new ProductAdapter(products, getContext(), this);
        RecyclerView recyclerView = view.findViewById(R.id.rv_wish_list_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(productAdapter);
    }

    private void getWishList() {

        products = new ArrayList<>();

        Call<ApiWishProducts> call = getApiService().getWishListProducts(getUserIdFromPreferences());
        call.enqueue(new Callback<ApiWishProducts>() {
            @Override
            public void onResponse(Call<ApiWishProducts> call, Response<ApiWishProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiWishProducts apiProducts = response.body();

                isDataLoaded = true;

                if (apiProducts.getEmbedded() == null) {
                    setScreenVisibility(false,false,true);
                    return;
                }

                for (Product product : apiProducts.getEmbeddedServices()) {
                    products.add(new CardProduct(
                            product.getId(),
                            product.getImageUrl(),
                            product.getName(),
                            String.valueOf(product.getPrice() / 100.0)
                    ));
                    productAdapter.notifyDataSetChanged();
                }

                setScreenVisibility(false,true,false);
            }

            @Override
            public void onFailure(Call<ApiWishProducts> call, Throwable t) {

            }
        });
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

    @Override
    public void onProductClick(View v, CardProduct cardProduct) {
        WishListFragmentDirections.ActionNavWishListToNavProduct action =
                WishListFragmentDirections.actionNavWishListToNavProduct(cardProduct.getId());
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