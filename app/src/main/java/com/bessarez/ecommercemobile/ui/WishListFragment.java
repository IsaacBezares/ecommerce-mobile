package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductListener;
import com.bessarez.ecommercemobile.models.apimodels.ApiProduct;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProduct;
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

public class WishListFragment extends Fragment implements OnProductListener {

    List<CardProduct> products;
    CardProductAdapter cardProductAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        loadRecycler(view);

        return view;
    }

    private void loadRecycler(View view) {
        SharedPreferences preferences = view.getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);

        products = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.rv_wish_list_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<ApiWishProducts> call = getApiService().getWishListProducts(userId);
        call.enqueue(new Callback<ApiWishProducts>() {
            @Override
            public void onResponse(Call<ApiWishProducts> call, Response<ApiWishProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiWishProducts apiProducts = response.body();

                if (apiProducts.getEmbedded() == null) return;

                for (ApiProduct product : apiProducts.getEmbeddedServices()) {
                    products.add(new CardProduct(
                            product.getId(),
                            product.getImageUrl(),
                            product.getName(),
                            String.valueOf(product.getPrice() / 100.0)
                    ));
                    cardProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiWishProducts> call, Throwable t) {

            }
        });

        cardProductAdapter = new CardProductAdapter(products, getContext(), this::onProductClick);
        recyclerView.setAdapter(cardProductAdapter);
    }

    @Override
    public void onProductClick(int position) {
        Long productId = products.get(position).getId();
        WishListFragmentDirections.ActionNavWishListToNavProduct action = WishListFragmentDirections.actionNavWishListToNavProduct(productId);
        Navigation.findNavController(getView()).navigate(action);
    }
}