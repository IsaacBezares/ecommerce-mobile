package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bessarez.ecommercemobile.CheckoutActivity;
import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.models.CartItem;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.UserViewedProduct;
import com.bessarez.ecommercemobile.models.WishProduct;
import com.bessarez.ecommercemobile.ui.dialogs.QuantityDialog;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class ProductFragment extends Fragment implements QuantityDialog.OnQuantityListener {

    private TextView tvProductTitle, tvProductPrice, tvProductStock, tvProductDescription;
    private ImageView ivProduct;
    private AppCompatButton btnQuantity, btnBuyNow, btnAddToCart, btnAddToWishList;

    private RelativeLayout loadingScreen;
    private NestedScrollView loadedScreen;

    private CartItem orderProduct;

    private boolean isDataLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null)
            return;

        isDataLoaded = false;

        orderProduct = new CartItem();

        Long productId = ProductFragmentArgs.fromBundle(getArguments()).getProductId();
        Long userId = getUserIdFromPreferences();

        if (userId != 0) addToRecentlyViewedProducts(userId, productId);

        getProduct(productId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.product_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_search) {
            navigateWithAction(ProductFragmentDirections.actionNavProductToNavSearch());
        } else if (item.getItemId() == R.id.nav_cart) {
            if (isUserLoggedIn()) {
                navigateWithAction(ProductFragmentDirections.actionNavProductToNavCart());
            } else {
                navigateWithAction(ProductFragmentDirections.actionNavProductToNavLogin());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataLoaded){
            setScreenVisibility(false,true);
        }
    }

    private void addToRecentlyViewedProducts(Long userId, Long productId) {

        UserViewedProduct userViewedProduct =
                new UserViewedProduct(new RegisteredUser(userId), new Product(productId));

        Call<UserViewedProduct> call = getApiService().addRecentlyViewedProduct(userViewedProduct);
        call.enqueue(new Callback<UserViewedProduct>() {
            @Override
            public void onResponse(Call<UserViewedProduct> call, Response<UserViewedProduct> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo falló");
                }
            }

            @Override
            public void onFailure(Call<UserViewedProduct> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getProduct(Long productId) {
        Call<Product> call = getApiService().getProduct(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NotNull Call<Product> call, @NotNull Response<Product> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                Product product = response.body();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(1);
                updateViewsData();

                isDataLoaded = true;

                setScreenVisibility(false,true);
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }

    private void bindViews(View view) {

        loadingScreen = view.findViewById(R.id.loading_layout);
        loadedScreen = view.findViewById(R.id.loaded_layout);

        tvProductTitle = view.findViewById(R.id.tv_product_title);
        tvProductPrice = view.findViewById(R.id.tv_product_price);
        tvProductStock = view.findViewById(R.id.tv_product_stock);
        tvProductDescription = view.findViewById(R.id.tv_product_description);
        ivProduct = view.findViewById(R.id.iv_product);
        btnQuantity = view.findViewById(R.id.btn_quantity);
        btnBuyNow = view.findViewById(R.id.btn_buy_now);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);
        btnAddToWishList = view.findViewById(R.id.btn_add_to_wish_list);

        btnBuyNow.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("productId", orderProduct.getProduct().getId());
                intent.putExtra("quantity", orderProduct.getQuantity());
                startActivity(intent);
            } else {
                navigateWithAction(ProductFragmentDirections.actionNavProductToNavLogin());
            }
        });

        btnQuantity.setText("1");
        btnQuantity.setOnClickListener(v -> {
            QuantityDialog dialog = new QuantityDialog(orderProduct.getProduct().getStock(), null);
            dialog.setTargetFragment(ProductFragment.this, 1);
            dialog.show(getParentFragmentManager(), QuantityDialog.TAG);
        });

        btnAddToCart.setOnClickListener(v -> {

            if (isUserLoggedIn()) {

                Call<CartItem> call = getApiService().addItemToCart(getUserIdFromPreferences(), orderProduct);
                call.enqueue(new Callback<CartItem>() {
                    @Override
                    public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "onResponse: Algo falló");
                        }

                        navigateWithAction(ProductFragmentDirections.actionNavProductToNavCart());
                    }

                    @Override
                    public void onFailure(Call<CartItem> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                navigateWithAction(ProductFragmentDirections.actionNavProductToNavLogin());
            }
        });

        btnAddToWishList.setOnClickListener(v -> {

            RegisteredUser user = new RegisteredUser(getUserIdFromPreferences());

            Product thisProduct = new Product(orderProduct.getProduct().getId());

            Call<Product> call = getApiService().addProductToWishList(new WishProduct(user, thisProduct));
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    navigateWithAction(ProductFragmentDirections.actionNavProductToNavWishList());
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Algo falló");
                        return;
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    navigateWithAction(ProductFragmentDirections.actionNavProductToNavWishList());
                    System.out.println("Algo falló");
                    t.printStackTrace();
                }
            });
        });
    }

    private void updateViewsData() {
        Product product = orderProduct.getProduct();

        tvProductTitle.setText(product.getName());
        tvProductPrice.setText("$" + product.getPrice() / 100.0);
        tvProductDescription.setText(product.getLongDesc());
        String availableStock = getString(R.string.available_stock);
        tvProductStock.setText(availableStock + " " + product.getStock());

        Picasso.get().load(Uri.parse(product.getImageUrl())).into(ivProduct);
    }

    @Override
    public void sendQuantity(Integer quantity, Integer position) {
        orderProduct.setQuantity(quantity);
        btnQuantity.setText(String.valueOf(quantity));
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

    private void setScreenVisibility(boolean loading, boolean loaded) {
        if (loading)
            loadingScreen.setVisibility(View.VISIBLE);
        else
            loadingScreen.setVisibility(View.GONE);

        if (loaded)
            loadedScreen.setVisibility(View.VISIBLE);
        else
            loadedScreen.setVisibility(View.GONE);
    }
}
