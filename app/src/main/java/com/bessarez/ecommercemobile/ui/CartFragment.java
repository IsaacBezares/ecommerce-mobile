package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bessarez.ecommercemobile.CheckoutActivity;
import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.models.Cart;
import com.bessarez.ecommercemobile.models.CartItem;
import com.bessarez.ecommercemobile.ui.adapters.CartItemAdapter;
import com.bessarez.ecommercemobile.ui.dialogs.QuantityDialog;
import com.bessarez.ecommercemobile.ui.models.CardCartItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class CartFragment extends Fragment implements OnItemClickListener, QuantityDialog.OnQuantityListener {

    private List<CardCartItem> cartItems;
    private CartItemAdapter adapter;

    private TextView tvTotalPrice;

    private RelativeLayout loadingScreen;
    private RelativeLayout emptyScreen;
    private NestedScrollView loadedScreen;

    private boolean isDataLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDataLoaded = false;
        if (isUserLoggedIn()) {
            getCart();
        } else {
            navigateWithAction(CartFragmentDirections.actionNavCartToNavLogin());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadingScreen = view.findViewById(R.id.loading_layout);
        loadedScreen = view.findViewById(R.id.loaded_layout);
        emptyScreen = view.findViewById(R.id.empty_layout);

        tvTotalPrice = view.findViewById(R.id.tv_total_price);

        AppCompatButton btnCheckout = view.findViewById(R.id.btn_checkout);
        btnCheckout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CheckoutActivity.class)));

        loadRecycler(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataLoaded) {
            if (!cartItems.isEmpty()) {
                setScreenVisibility(false,true,false);
            } else {
                setScreenVisibility(false,false,true);
            }
        }
    }

    private void loadRecycler(View view) {
        cartItems = new ArrayList<>();
        adapter = new CartItemAdapter(cartItems, getContext(), this);
        RecyclerView rvCart = view.findViewById(R.id.rv_cart_items);
        rvCart.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCart.setLayoutManager(layoutManager);
        rvCart.setAdapter(adapter);
    }

    private void getCart() {

        SharedPreferences preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);

        if (userId == 0) return;

        Call<Cart> call = getApiService().getUserCart(userId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo falló");
                    return;
                }

                Cart cart = response.body();
                cart.getCartItems().stream()
                        .map(item -> new CardCartItem(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getProduct().getImageUrl(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getProduct().getPrice() / 100.0,
                                item.getProduct().getStock()
                        ))
                        .forEach(cartItems::add);

                isDataLoaded = true;

                if (cartItems.size() > 0){
                    updateTotalPrice();
                    adapter.notifyDataSetChanged();

                    setScreenVisibility(false,true,false);
                } else {
                    setScreenVisibility(false,false,true);
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_delete_item:
                long itemId = cartItems.remove(position).getId();
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, cartItems.size());

                updateTotalPrice();

                Call<Void> call = getApiService().removeCartItem(itemId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "onResponse: Algo falló");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                if (cartItems.size() <= 0){
                    loadedScreen.setVisibility(View.GONE);
                    emptyScreen.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_quantity:
                QuantityDialog dialog = new QuantityDialog(cartItems.get(position).getStock(), position);
                dialog.setTargetFragment(this, 1);
                dialog.show(getParentFragmentManager(), QuantityDialog.TAG);
                break;
            default:
                long productId = cartItems.get(position).getProductId();
                CartFragmentDirections.ActionNavCartToNavProduct action = CartFragmentDirections.actionNavCartToNavProduct(productId);
                Navigation.findNavController(getView()).navigate(action);
        }
    }

    @Override
    public void sendQuantity(Integer quantity, Integer position) {
        CardCartItem item = cartItems.get(position);

        item.setQuantity(quantity);
        adapter.notifyDataSetChanged();

        updateTotalPrice();

        Call<CartItem> call = getApiService().updateCartItemQuantity(item.getId(), quantity);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo falló");
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateTotalPrice() {
        double totalPrice = cartItems.stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0.0, Double::sum);

        tvTotalPrice.setText(String.valueOf(totalPrice));
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