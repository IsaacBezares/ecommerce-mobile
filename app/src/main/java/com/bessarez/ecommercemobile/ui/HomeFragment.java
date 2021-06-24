package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnProductClickListener;
import com.bessarez.ecommercemobile.models.apimodels.ApiCarouselImages;
import com.bessarez.ecommercemobile.models.apimodels.ApiProducts;
import com.bessarez.ecommercemobile.ui.models.CardProduct;
import com.bessarez.ecommercemobile.ui.adapters.ProductAdapter;

import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements OnProductClickListener {

    private List<CardProduct> recentProducts;
    private ProductAdapter recentProductAdapter;

    private List<CardProduct> recommendedProducts;
    private ProductAdapter recommendedProductAdapter;

    private List<CardProduct> randomProducts;
    private ProductAdapter randomProductAdapter;

    private List<SlideModel> slideModelList;
    private ImageSlider imageSlider;

    private ConstraintLayout loadingScreen;
    private LinearLayout llRecent;
    private LinearLayout llRecommended;
    private LinearLayout llRandom;

    private boolean isDataLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDataLoaded = false;
        getCarouselImages();
        if (isUserLoggedIn()) {
            getRecentlyViewedProducts();
            getRecommendedProducts();
        } else {
            getRandomProducts();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        loadingScreen = view.findViewById(R.id.loading_layout);
        llRecent = view.findViewById(R.id.recent_products);
        llRecommended = view.findViewById(R.id.recommended_products);
        llRandom = view.findViewById(R.id.random_products);

        imageSlider = view.findViewById(R.id.slider);

        if (isUserLoggedIn()) {
            loadRecentProductsRecycler(view);
            loadRecommendedProductsRecycler(view);
        } else {
            loadRandomProductsRecycler(view);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_search) {
            navigateWithAction(HomeFragmentDirections.actionNavHomeToNavSearch());
        } else if (item.getItemId() == R.id.nav_cart) {
            if (isUserLoggedIn()) {
                navigateWithAction(HomeFragmentDirections.actionNavHomeToNavCart());
            } else {
                navigateWithAction(HomeFragmentDirections.actionNavHomeToNavLogin());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataLoaded) {
            if (isUserLoggedIn()) {
                if (!recentProducts.isEmpty()) {
                    setScreenVisibility(false,true,true,true,false);
                } else {
                    setScreenVisibility(false,true,false,false,true);
                }
            } else {
                if (!randomProducts.isEmpty()) {
                    setScreenVisibility(false,true,false,false,true);
                }
            }
        }
    }

    private void loadRecentProductsRecycler(View view) {
        recentProductAdapter = new ProductAdapter(recentProducts, getContext(), this);
        RecyclerView recentProductsRecycler = view.findViewById(R.id.rv_recent_products);
        recentProductsRecycler.setNestedScrollingEnabled(false);
        recentProductsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recentProductsRecycler.setAdapter(recentProductAdapter);
    }

    private void loadRecommendedProductsRecycler(View view) {
        recommendedProductAdapter = new ProductAdapter(recommendedProducts, getContext(), this);
        RecyclerView recommendedProductsRecycler = view.findViewById(R.id.rv_recommended_products);
        recommendedProductsRecycler.setNestedScrollingEnabled(false);
        recommendedProductsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendedProductsRecycler.setAdapter(recommendedProductAdapter);
    }

    private void loadRandomProductsRecycler(View view) {
        randomProductAdapter = new ProductAdapter(randomProducts, getContext(), this);
        RecyclerView randomProductsRecycler = view.findViewById(R.id.rv_random_products);
        randomProductsRecycler.setNestedScrollingEnabled(false);
        randomProductsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        randomProductsRecycler.setAdapter(randomProductAdapter);
    }

    private void getCarouselImages() {
        slideModelList = new ArrayList<>();
        Call<ApiCarouselImages> call = getApiService().getCarouselImages();
        call.enqueue(new Callback<ApiCarouselImages>() {
            @Override
            public void onResponse(Call<ApiCarouselImages> call, Response<ApiCarouselImages> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ApiCarouselImages apiCarouselImages = response.body();

                if (apiCarouselImages.getEmbedded() == null) {
                    return;
                }

                apiCarouselImages.getEmbeddedServices().stream()
                        .forEach(image -> slideModelList.add(new SlideModel(image.getImageUrl())));
                imageSlider.setImageList(slideModelList, true);

                isDataLoaded = true;
            }

            @Override
            public void onFailure(Call<ApiCarouselImages> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getRecentlyViewedProducts() {
        recentProducts = new ArrayList<>();
        Call<ApiProducts> call = getApiService().getRecentlyViewedProducts(getUserIdFromPreferences());
        call.enqueue(new Callback<ApiProducts>() {
            @Override
            public void onResponse(Call<ApiProducts> call, Response<ApiProducts> response) {
                if (!response.isSuccessful()) {
                    getRandomProducts();
                    loadRandomProductsRecycler(getView());
                    return;
                }

                ApiProducts apiProducts = response.body();

                if (apiProducts.getEmbedded() == null) return;

                apiProducts.getEmbeddedServices().stream()
                        .forEach(product ->
                                recentProducts.add(new CardProduct(
                                        product.getId(),
                                        product.getImageUrl(),
                                        product.getName(),
                                        String.valueOf(product.getPrice() / 100.0)
                                ))
                        );
                recentProductAdapter.notifyDataSetChanged();

                isDataLoaded = true;

                setScreenVisibility(false,true,true,true,false);
            }

            @Override
            public void onFailure(Call<ApiProducts> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getRecommendedProducts() {
        recommendedProducts = new ArrayList<>();
        Call<ApiProducts> call = getApiService().getRecommendedProducts(getUserIdFromPreferences());
        call.enqueue(new Callback<ApiProducts>() {
            @Override
            public void onResponse(Call<ApiProducts> call, Response<ApiProducts> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ApiProducts apiProducts = response.body();

                if (apiProducts.getEmbedded() == null) return;

                apiProducts.getEmbeddedServices().stream()
                        .forEach(product -> recommendedProducts.add(
                                new CardProduct(
                                        product.getId(),
                                        product.getImageUrl(),
                                        product.getName(),
                                        String.valueOf(product.getPrice() / 100.0)
                                )));

                isDataLoaded = true;

                recommendedProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ApiProducts> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getRandomProducts() {
        randomProducts = new ArrayList<>();
        Call<ApiProducts> call = getApiService().getRandomProducts(10);
        call.enqueue(new Callback<ApiProducts>() {
            @Override
            public void onResponse(Call<ApiProducts> call, Response<ApiProducts> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ApiProducts apiProducts = response.body();

                if (apiProducts.getEmbedded() == null) {
                    return;
                }

                apiProducts.getEmbeddedServices().stream()
                        .forEach(product -> randomProducts.add(
                                new CardProduct(
                                        product.getId(),
                                        product.getImageUrl(),
                                        product.getName(),
                                        String.valueOf(product.getPrice() / 100.0)
                                )
                        ));

                isDataLoaded = true;

                randomProductAdapter.notifyDataSetChanged();

                setScreenVisibility(false,true,false,false,true);

            }

            @Override
            public void onFailure(Call<ApiProducts> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onProductClick(View v, CardProduct cardProduct) {
        HomeFragmentDirections.ActionNavHomeToNavProduct action =
                HomeFragmentDirections.actionNavHomeToNavProduct(cardProduct.getId());
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

    private void setScreenVisibility(boolean loading, boolean slider, boolean recent, boolean recommended, boolean random) {
        if (loading)
            loadingScreen.setVisibility(View.VISIBLE);
        else
            loadingScreen.setVisibility(View.GONE);

        if (slider) {
            imageSlider.setVisibility(View.VISIBLE);
            imageSlider.setImageList(slideModelList, true);
        }
        else
            imageSlider.setVisibility(View.GONE);

        if (recent)
            llRecent.setVisibility(View.VISIBLE);
        else
            llRecent.setVisibility(View.GONE);

        if (recommended)
            llRecommended.setVisibility(View.VISIBLE);
        else
            llRecommended.setVisibility(View.GONE);

        if (random)
            llRandom.setVisibility(View.VISIBLE);
        else
            llRandom.setVisibility(View.GONE);
    }
}