package com.bessarez.ecommercemobile.ui;

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
import com.bessarez.ecommercemobile.models.CarouselImage;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiCarouselImages;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiProducts;
import com.bessarez.ecommercemobile.ui.adapters.CardProduct;
import com.bessarez.ecommercemobile.ui.adapters.CardProductAdapter;

import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements CardProductAdapter.OnProductListener {

    List<CardProduct> products;
    CardProductAdapter cardProductAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadSlider(view);
        loadRecycler(view);

        return view;
    }

    private void loadRecycler(View view) {
        products = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rv_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<ResponseApiProducts> call = getApiService().getProducts();
        call.enqueue(new Callback<ResponseApiProducts>() {
            @Override
            public void onResponse(Call<ResponseApiProducts> call, Response<ResponseApiProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ResponseApiProducts responseApiProducts = response.body();

                for (com.bessarez.ecommercemobile.models.Product product : responseApiProducts.getEmbeddedServices()) {
                    products.add(new CardProduct(product.getId(),product.getImageUrl(), product.getName(), String.valueOf(product.getPrice())));
                    cardProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseApiProducts> call, Throwable t) {

            }
        });

        cardProductAdapter = new CardProductAdapter(products, getContext(),this::onProductClick);
        recyclerView.setAdapter(cardProductAdapter);
    }

    private void loadSlider(View view) {
        ImageSlider imageSlider = view.findViewById(R.id.silder);
        List<SlideModel> slideModelList = new ArrayList<>();

        Call<ResponseApiCarouselImages> call = getApiService().getCarouselImages();
        call.enqueue(new Callback<ResponseApiCarouselImages>() {
            @Override
            public void onResponse(Call<ResponseApiCarouselImages> call, Response<ResponseApiCarouselImages> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ResponseApiCarouselImages responseApiCarouselImages = response.body();
                //fills carousel with images from api
                for (CarouselImage carouselImage : responseApiCarouselImages.getEmbeddedServices()
                ) {
                    slideModelList.add(new SlideModel(carouselImage.getImageUrl()));
                }
                imageSlider.setImageList(slideModelList, true);
            }

            @Override
            public void onFailure(Call<ResponseApiCarouselImages> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public void onProductClick(int position) {
        Long productId = products.get(position).getId();
        HomeFragmentDirections.ActionNavHomeToProductFragment action = HomeFragmentDirections.actionNavHomeToProductFragment(productId);
        Navigation.findNavController(getView()).navigate(action);
    }
}