package com.bessarez.ecommercemobile.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.models.OrderProduct;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.ProductCategory;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiProduct;
import com.bessarez.ecommercemobile.ui.dialogs.ProductOrderQuantityDialog;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class ProductFragment extends Fragment implements ProductOrderQuantityDialog.OnQuantityListener {

    @Override
    public void sendQuantity(Integer quantity) {
        orderProduct.setQuantity(quantity);
        btnQuantity.setText(getString(R.string.quantity) + " " + quantity);
    }

    TextView tvProductTitle, tvProductPrice, tvProductStock;
    ImageView ivProduct;
    AppCompatButton btnQuantity, btnBuyNow, btnAddToCart;

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

        Call<ResponseApiProduct> call = getApiService().getProduct(productId);
        call.enqueue(new Callback<ResponseApiProduct>() {
            @Override
            public void onResponse(Call<ResponseApiProduct> call, Response<ResponseApiProduct> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ResponseApiProduct responseProduct = response.body();
                Product product = makeProduct(responseProduct);
                updateViewsData(product);

                orderProduct.setProduct(product);
                orderProduct.setQuantity(1);

            }

            @Override
            public void onFailure(Call<ResponseApiProduct> call, Throwable t) {
            }
        });
    }

    //Methods for readability

    private Product makeProduct(ResponseApiProduct product) {
        Long id = product.getId();
        String name = product.getName();
        String ean13 = product.getEan13();
        float price = product.getPrice();
        float productWeight = product.getProductWeight();
        String shortDesc = product.getShortDesc();
        String longDesc = product.getLongDesc();
        int stock = product.getStock();
        int quantity = product.getQuantity();
        String imageUrl = product.getImageUrl();
        ProductCategory productCategory = new ProductCategory(
                product.getProductCategory().getId(),
                product.getProductCategory().getCategory());

        return new Product(id, name, ean13, price, productWeight, shortDesc, longDesc, stock, quantity, imageUrl, productCategory);
    }

    private void bindViews(View view){
        tvProductTitle = view.findViewById(R.id.tv_product_title);
        tvProductPrice = view.findViewById(R.id.tv_product_price);
        tvProductStock = view.findViewById(R.id.tv_product_stock);
        ivProduct = view.findViewById(R.id.iv_product);
        btnQuantity = view.findViewById(R.id.btn_quantity);
        btnBuyNow = view.findViewById(R.id.btn_buy_now);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);

        btnQuantity.setText(getString(R.string.quantity) + " 1");
        btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductOrderQuantityDialog dialog = new ProductOrderQuantityDialog(orderProduct.getProduct().getStock());
                dialog.setTargetFragment(ProductFragment.this, 1);
                dialog.show(getParentFragmentManager(),ProductOrderQuantityDialog.TAG);
            }
        });
    }

    private void updateViewsData(Product product){
        tvProductTitle.setText(product.getName());
        tvProductPrice.setText("$" + product.getPrice());
        String availableStock = getString(R.string.available_stock);
        tvProductStock.setText(availableStock + " " + product.getStock());

        Picasso.get().load(Uri.parse(product.getImageUrl())).into(ivProduct);
    }
}