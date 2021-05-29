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
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.models.OrderProduct;
import com.bessarez.ecommercemobile.models.UserOrder;
import com.bessarez.ecommercemobile.models.apimodels.ApiUserOrders;
import com.bessarez.ecommercemobile.ui.models.ListOrderItem;
import com.bessarez.ecommercemobile.ui.adapters.OrderAdapter;
import com.bessarez.ecommercemobile.ui.models.CardOrder;
import com.bessarez.ecommercemobile.ui.models.CardOrderItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class OrdersFragment extends Fragment implements OnItemClickListener {

    private List<ListOrderItem> consolidatedList = new ArrayList<>();

    private OrderAdapter orderAdapter;

    private RelativeLayout loadingScreen;
    private RelativeLayout emptyScreen;
    private NestedScrollView loadedScreen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isUserLoggedIn()) {
            navigateWithAction(OrdersFragmentDirections.actionNavOrdersToNavLogin());
        }

        loadingScreen = view.findViewById(R.id.loading_layout);
        loadedScreen = view.findViewById(R.id.loaded_layout);
        emptyScreen = view.findViewById(R.id.empty_layout);

        fillOrderList();
        loadRecycler(view);
    }

    private void loadRecycler(View view) {
        RecyclerView rv_orders = view.findViewById(R.id.rv_orders);
        rv_orders.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_orders.setLayoutManager(layoutManager);
        rv_orders.setAdapter(orderAdapter);
    }

    private void fillOrderList() {
        SharedPreferences preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        Long userId = preferences.getLong("userId", 0);

        if (userId == 0) return;

        orderAdapter = new OrderAdapter(getContext(), consolidatedList, this);

        Call<ApiUserOrders> userOrders = getApiService().getUserOrders(userId);
        userOrders.enqueue(new Callback<ApiUserOrders>() {
            @Override
            public void onResponse(Call<ApiUserOrders> call, Response<ApiUserOrders> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiUserOrders apiUserOrders = response.body();

                if (apiUserOrders.getEmbedded() == null) {
                    loadingScreen.setVisibility(View.GONE);
                    emptyScreen.setVisibility(View.VISIBLE);
                    return;
                }

                for (UserOrder order : apiUserOrders.getEmbeddedServices()) {
                    long orderId = order.getId();
                    LocalDate orderedAt = order.getOrderedAt();
                    double totalAmount = order.getOrderProducts().stream()
                            .map(this::calcIndividualAmountDouble)
                            .reduce(0.0, Double::sum);

                    consolidatedList.add(new CardOrder(orderId, orderedAt, totalAmount));

                    for (OrderProduct orderProduct : order.getOrderProducts()) {
                        long productId = orderProduct.getProduct().getId();
                        String imageUrl = orderProduct.getProduct().getImageUrl();
                        String name = orderProduct.getProduct().getName();

                        consolidatedList.add(new CardOrderItem(productId, imageUrl, name));
                    }

                }

                orderAdapter.notifyDataSetChanged();

                loadingScreen.setVisibility(View.GONE);
                loadedScreen.setVisibility(View.VISIBLE);
            }

            private double calcIndividualAmountDouble(OrderProduct orderProduct) {
                return orderProduct.getProduct().getPrice() * orderProduct.getQuantity() / 100.0;
            }

            @Override
            public void onFailure(Call<ApiUserOrders> call, Throwable t) {
                Log.d(TAG, "onFailure: Algo falló");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onProductClick: hizo algo");
        Long productId = ((CardOrderItem) consolidatedList.get(position)).getId();
        OrdersFragmentDirections.ActionNavOrdersToNavProduct action = OrdersFragmentDirections.actionNavOrdersToNavProduct(productId);
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