package com.bessarez.ecommercemobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.models.Cart;
import com.bessarez.ecommercemobile.models.CartItem;
import com.bessarez.ecommercemobile.models.OrderProduct;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.UserOrder;
import com.bessarez.ecommercemobile.models.apimodels.PaymentIntentResponse;
import com.bessarez.ecommercemobile.ui.adapters.CheckoutItemAdapter;
import com.bessarez.ecommercemobile.ui.models.CardCheckoutItem;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class CheckoutActivity extends AppCompatActivity {
    private String paymentIntentClientSecret;
    private Stripe stripe;

    private List<CardCheckoutItem> checkoutList = new ArrayList<>();
    private CheckoutItemAdapter adapter;

    private boolean isCartCheckout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51InApwFWIMS4pcIQLflH6voYAt2BwAbSMP1l4M79Cu9o8MHYX3rSSVojSQXdry73gLvGCxhPeFeRIqBoO71VIp2H00dPfS7rHM")
        );

        loadRecycler();

        Intent mIntent = getIntent();
        long productId = mIntent.getLongExtra("productId", 0);
        int quantity = mIntent.getIntExtra("quantity", 0);

        //productId is passed from productFragment if "Buy now" button was pressed
        //So if product is not 0(null), proceed to checkOut Product
        //Otherwise take all cartProducts for checkOut

        if (productId == 0 || quantity == 0) {
            isCartCheckout = true;
            fillCheckoutListFromCart();
        } else {
            isCartCheckout = false;
            fillCheckoutListFromProduct(productId, quantity);
        }
    }

    private void fillCheckoutListFromProduct(long productId, int quantity) {

        Call<Product> call = getApiService().getProduct(productId);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo falló");
                    return;
                }

                Product product = response.body();
                checkoutList.add(new CardCheckoutItem(productId, quantity, product.getName(), null, product.getPrice()));

                adapter.notifyDataSetChanged();

                startCheckout();
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
    }

    private void fillCheckoutListFromCart() {

        Call<Cart> call = getApiService().getUserCart(getUserIdPreferences());
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo pasó");
                    return;
                }

                Cart cart = response.body();

                checkoutList = cart.getCartItems().stream()
                        .map(item -> new CardCheckoutItem(
                                item.getId(),
                                item.getQuantity(),
                                item.getProduct().getName(),
                                null,
                                item.getProduct().getPrice()))
                        .collect(Collectors.toList());
                adapter.notifyDataSetChanged();
                startCheckout();
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void startCheckout() {
        //convert to cartList for api
        List<CartItem> cartItems = checkoutList.stream()
                .map(cardItem -> new CartItem(cardItem.getQuantity(), new Product(cardItem.getId())))
                .collect(Collectors.toList());

        // Create a PaymentIntent by calling the server's endpoint.
        Call<PaymentIntentResponse> call = getApiService().createPaymentIntent(cartItems);
        call.enqueue(new PayCallback(getThis()));

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);

            }
        });
    }

    private CheckoutActivity getThis() {
        return this;
    }

    //Aqui puedes iniciar MainActivity
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private void loadRecycler() {
        adapter = new CheckoutItemAdapter(this, checkoutList);

        RecyclerView rvCheckoutItems = findViewById(R.id.rv_checkout_items);
        rvCheckoutItems.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCheckoutItems.setLayoutManager(layoutManager);
        rvCheckoutItems.setAdapter(adapter);
    }

    private void onPaymentSuccess(@NonNull final Response response) {

        TextView tv_currency = findViewById(R.id.tv_currency);
        TextView tv_totalAmount = findViewById(R.id.tv_total_amount);

        PaymentIntentResponse paymentIntent = (PaymentIntentResponse) response.body();
        paymentIntentClientSecret = paymentIntent.getClientSecret();

        checkoutList.stream()
                .forEach(item -> item.setCurrency(paymentIntent.getCurrency().toUpperCase()));

        adapter.notifyDataSetChanged();

        tv_currency.setText(paymentIntent.getCurrency().toUpperCase());
        tv_totalAmount.setText(String.valueOf(paymentIntent.getAmount() / 100.0));
    }


    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<CheckoutActivity> activityRef;

        PayCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + t.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivity> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Long userId = activity.getUserIdPreferences();

                List<OrderProduct> orderProducts = activity.checkoutList.stream()
                        .map(item -> new OrderProduct(item.getQuantity(), new Product(item.getId())))
                        .collect(Collectors.toList());

                UserOrder order = new UserOrder(
                        orderProducts,
                        new RegisteredUser(userId)
                );

                Call<UserOrder> call = getApiService().addOrder(order);
                call.enqueue(new Callback<UserOrder>() {
                    @Override
                    public void onResponse(Call<UserOrder> call, Response<UserOrder> response) {
                        if (!response.isSuccessful()){
                            Log.d(TAG, "onResponse: Algo falló");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserOrder> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                if (activity.isCartCheckout){
                    Call<Void> call2 = getApiService().removeAllCartItems(userId);
                    call2.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) Log.d(TAG, "onResponse: Algo falló");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

                activity.displayAlert(
                        "Payment completed",
                        paymentIntent.toString()
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }

    private Long getUserIdPreferences(){
        SharedPreferences preferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        return preferences.getLong("userId", 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        this.finish();
        return super.onSupportNavigateUp();

    }
}