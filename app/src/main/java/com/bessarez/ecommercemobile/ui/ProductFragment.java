package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bessarez.ecommercemobile.CheckoutActivity;
import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.models.OrderProduct;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.ProductCategory;
import com.bessarez.ecommercemobile.models.apimodels.ApiRegisteredUser;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProduct;
import com.bessarez.ecommercemobile.models.apimodels.ApiProduct;
import com.bessarez.ecommercemobile.ui.dialogs.ProductOrderQuantityDialog;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class ProductFragment extends Fragment implements ProductOrderQuantityDialog.OnQuantityListener {

    TextView tvProductTitle, tvProductPrice, tvProductStock, tvProductDescription;
    ImageView ivProduct;
    AppCompatButton btnQuantity, btnBuyNow, btnAddToCart, btnAddToWishList;

    OrderProduct orderProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        bindViews(view);
        orderProduct = new OrderProduct();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Long productId;
        if (getArguments() != null) {
            productId = ProductFragmentArgs.fromBundle(getArguments()).getProductId();
        } else {
            return;
        }

        Call<Product> call = getApiService().getProduct(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                Product product = response.body();
                orderProduct.setProduct(product);
                orderProduct.setQuantity(1);
                updateViewsData(product);

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
            }
        });
    }

    @Override
    public void sendQuantity(Integer quantity) {
        orderProduct.setQuantity(quantity);
        btnQuantity.setText(getString(R.string.quantity) + " " + quantity);
    }

    //Methods for readability

    private void bindViews(View view) {

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
            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
            intent.putExtra("productId", orderProduct.getProduct().getId());
            intent.putExtra("quantity", orderProduct.getQuantity());
            startActivity(intent);
        });

        btnQuantity.setText(getString(R.string.quantity) + " 1");
        btnQuantity.setOnClickListener(v -> {
            ProductOrderQuantityDialog dialog = new ProductOrderQuantityDialog(orderProduct.getProduct().getStock());
            dialog.setTargetFragment(ProductFragment.this, 1);
            dialog.show(getParentFragmentManager(), ProductOrderQuantityDialog.TAG);
        });

        btnAddToWishList.setOnClickListener(v -> {

            SharedPreferences preferences = view.getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
            Long userId = preferences.getLong("userId", 0);

            ApiRegisteredUser user = new ApiRegisteredUser(userId);
            user.setId(userId);

            ApiProduct thisProduct = new ApiProduct(orderProduct.getProduct().getId());

            Call<ApiWishProduct> call = getApiService().addProductToWishList(new ApiWishProduct(user, thisProduct));
            call.enqueue(new Callback<ApiWishProduct>() {
                @Override
                public void onResponse(Call<ApiWishProduct> call, Response<ApiWishProduct> response) {
                    Log.d(TAG, "onResponse: " + response.message());
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "Algo falló");
                        return;
                    }
                }

                @Override
                public void onFailure(Call<ApiWishProduct> call, Throwable t) {
                    System.out.println("Algo falló");
                    t.printStackTrace();
                }
            });
        });
    }

    private void updateViewsData(Product product) {
        tvProductTitle.setText(product.getName());
        tvProductPrice.setText("$" + product.getPrice()/100.0);
        tvProductDescription.setText(product.getLongDesc());
        String availableStock = getString(R.string.available_stock);
        tvProductStock.setText(availableStock + " " + product.getStock());

        Picasso.get().load(Uri.parse(product.getImageUrl())).into(ivProduct);
    }
}
