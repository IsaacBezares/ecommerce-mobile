package com.bessarez.ecommercemobile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bessarez.ecommercemobile.R;
import com.bessarez.ecommercemobile.interfaces.OnItemClickListener;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.RecentSearch;
import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.apimodels.ApiSuggestedProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiRecentSearches;
import com.bessarez.ecommercemobile.ui.adapters.SearchSuggestionAdapter;
import com.bessarez.ecommercemobile.ui.models.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.bessarez.ecommercemobile.connector.ApiClient.getApiService;

public class SearchFragment extends Fragment implements OnItemClickListener {

    private List<SearchSuggestion> recentSearches;
    private List<SearchSuggestion> searchSuggestions;
    private SearchSuggestionAdapter searchSuggestionAdapter;
    private SearchView searchView;

    private CompositeDisposable disposables = new CompositeDisposable();

    //callback methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        recentSearches = new ArrayList<>();
        searchSuggestions = new ArrayList<>();
        searchSuggestionAdapter = new SearchSuggestionAdapter(searchSuggestions, getContext(), this);

        RecyclerView recyclerView = view.findViewById(R.id.rv_recommended_searches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchSuggestionAdapter);

        if (isUserLoggedIn()) {
            getRecentSearches(getUserIdFromPreferences());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.nav_search);
        searchItem.expandActionView();

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Navigation.findNavController(getView()).navigateUp();
                return false;
            }
        });

        searchView = (SearchView) searchItem.getActionView();

        setSearchViewListener(searchView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    //search interface methods

    private void setSearchViewListener(SearchView searchView) {

        Observable<String> observableQueryText = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<String> emitter) throws Throwable {
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                SearchFragmentDirections.ActionNavSearchToNavSearchResult action = SearchFragmentDirections.actionNavSearchToNavSearchResult(query);
                                Navigation.findNavController(getView()).navigate(action);

                                if (isUserLoggedIn())
                                    postRecentSearch(getUserIdFromPreferences(), query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(newText);
                                }
                                return false;
                            }
                        });
                    }
                })
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io());

        observableQueryText.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                sendRequest(s);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void sendRequest(String query) {
        if (query.length() == 0) {
            getActivity().runOnUiThread(() -> {
                searchSuggestions.clear();
                searchSuggestions.addAll(recentSearches);
                searchSuggestionAdapter.notifyDataSetChanged();
            });
        }
        Call<ApiSuggestedProducts> call = getApiService().getSuggestedProducts(query);
        call.enqueue(new Callback<ApiSuggestedProducts>() {
            @Override
            public void onResponse(Call<ApiSuggestedProducts> call, Response<ApiSuggestedProducts> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo falló");
                    return;
                }
                searchSuggestions.clear();
                ApiSuggestedProducts suggestedProducts = response.body();
                if ((suggestedProducts.getEmbeddedServices() != null)) {
                    for (Product suggestedProduct : suggestedProducts.getSuggestionList()) {
                        searchSuggestions.add(new SearchSuggestion(suggestedProduct.getId(), suggestedProduct.getName(), 2));
                        searchSuggestionAdapter.notifyDataSetChanged();
                    }
                } else {
                    searchSuggestionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiSuggestedProducts> call, Throwable t) {
                Log.d(TAG, "onFailure: Algo falló");
            }
        });
    }

    private void getRecentSearches(Long userId) {
        Call<ApiRecentSearches> call = getApiService().getRecentSearches(userId);
        call.enqueue(new Callback<ApiRecentSearches>() {
            @Override
            public void onResponse(Call<ApiRecentSearches> call, Response<ApiRecentSearches> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo falló");
                    return;
                }

                ApiRecentSearches apiRecentSearches = response.body();

                if (apiRecentSearches.getEmbeddedServices() == null) {
                    return;
                }

                for (RecentSearch search : apiRecentSearches.getRecentSearchList()) {
                    recentSearches.add(new SearchSuggestion(null, search.getSearch(), 1));
                }
                searchSuggestions.addAll(recentSearches);
                searchSuggestionAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ApiRecentSearches> call, Throwable t) {
                Log.d(TAG, "onFailure: Algo falló");
            }
        });
    }

    private void postRecentSearch(Long userId, String query) {
        RecentSearch recentSearch = new RecentSearch();
        recentSearch.setSearch(query);
        recentSearch.setRegisteredUser(new RegisteredUser(userId));

        Call<RecentSearch> call = getApiService().postRecentSearch(recentSearch);
        call.enqueue(new Callback<RecentSearch>() {
            @Override
            public void onResponse(Call<RecentSearch> call, Response<RecentSearch> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Algo pasó");
                    return;
                }
            }

            @Override
            public void onFailure(Call<RecentSearch> call, Throwable t) {
                Log.d(TAG, "onFailure: Algo pasó");
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        if (v.getId() == R.id.ib_replace_text) {
            searchView.setQuery(searchSuggestions.get(position).getName(), false);
            //-1 for viewholder of recycler item
        } else {
            SearchSuggestion searchSuggestion = searchSuggestions.get(position);

            if (searchSuggestion.getType() == 1) {
                String query = searchSuggestion.getName();
                SearchFragmentDirections.ActionNavSearchToNavSearchResult action = SearchFragmentDirections.actionNavSearchToNavSearchResult(query);
                Navigation.findNavController(getView()).navigate(action);
            } else {
                Long productId = searchSuggestion.getId();
                SearchFragmentDirections.ActionNavSearchToNavProduct action = SearchFragmentDirections.actionNavSearchToNavProduct(productId);
                Navigation.findNavController(getView()).navigate(action);
            }
        }
    }

    private boolean isUserLoggedIn() {
        return getUserIdFromPreferences() != 0;
    }

    private Long getUserIdFromPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        return preferences.getLong("userId", 0);
    }


}