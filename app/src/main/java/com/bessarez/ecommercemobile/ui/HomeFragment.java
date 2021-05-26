package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.models.CarouselImage;
import com.bessarez.ecommercemobile.models.apimodels.ApiCarouselImages;
import com.bessarez.ecommercemobile.models.apimodels.ApiProducts;
import com.bessarez.ecommercemobile.ui.models.CardProduct;
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

public class HomeFragment extends Fragment implements OnItemClickListener {

    List<CardProduct> products;
    CardProductAdapter cardProductAdapter;
    Context mContext;


    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = view.getContext();

        setHasOptionsMenu(true);

        loadSlider(view);
        loadRecycler(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home_menu, menu);
    }

    private void loadRecycler(View view) {
        products = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rv_products);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<ApiProducts> call = getApiService().getProducts();
        call.enqueue(new Callback<ApiProducts>() {
            @Override
            public void onResponse(Call<ApiProducts> call, Response<ApiProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiProducts apiProducts = response.body();

                for (com.bessarez.ecommercemobile.models.Product product : apiProducts.getEmbeddedServices()) {
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
            public void onFailure(Call<ApiProducts> call, Throwable t) {

            }
        });

        cardProductAdapter = new CardProductAdapter(products, getContext(), this);
        recyclerView.setAdapter(cardProductAdapter);
    }

    private void loadSlider(View view) {
        ImageSlider imageSlider = view.findViewById(R.id.silder);
        List<SlideModel> slideModelList = new ArrayList<>();

        Call<ApiCarouselImages> call = getApiService().getCarouselImages();
        call.enqueue(new Callback<ApiCarouselImages>() {
            @Override
            public void onResponse(Call<ApiCarouselImages> call, Response<ApiCarouselImages> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Algo falló");
                    return;
                }

                ApiCarouselImages apiCarouselImages = response.body();
                //fills carousel with images from api
                for (CarouselImage carouselImage : apiCarouselImages.getEmbeddedServices()
                ) {
                    slideModelList.add(new SlideModel(carouselImage.getImageUrl()));
                }
                imageSlider.setImageList(slideModelList, true);
            }

            @Override
            public void onFailure(Call<ApiCarouselImages> call, Throwable t) {
                Log.e(TAG, "onFailure: Prob timeout, printStackTrace for more info (HomeFrag)");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Long productId = products.get(position).getId();
        HomeFragmentDirections.ActionNavHomeToProductFragment action = HomeFragmentDirections.actionNavHomeToProductFragment(productId);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_search){
            NavDirections action = HomeFragmentDirections.actionNavHomeToSearchFragment();
            Navigation.findNavController(getView()).navigate(action);
        } else if (item.getItemId() == R.id.nav_cart){
            NavDirections action = HomeFragmentDirections.actionNavHomeToCartFragment();
            Navigation.findNavController(getView()).navigate(action);
        }
        return super.onOptionsItemSelected(item);
    }
}