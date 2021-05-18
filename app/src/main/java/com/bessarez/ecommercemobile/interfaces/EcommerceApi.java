package com.bessarez.ecommercemobile.interfaces;

import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.apimodels.ApiLogin;
import com.bessarez.ecommercemobile.models.apimodels.ApiProductsSearch;
import com.bessarez.ecommercemobile.models.apimodels.ApiSuggestedProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiUserOrders;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProduct;
import com.bessarez.ecommercemobile.models.apimodels.ApiCarouselImages;
import com.bessarez.ecommercemobile.models.apimodels.ApiToken;
import com.bessarez.ecommercemobile.models.apimodels.ApiProductCategories;
import com.bessarez.ecommercemobile.models.apimodels.ApiProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiRegisteredUsers;
import com.bessarez.ecommercemobile.models.apimodels.ApiUserViewedProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiProduct;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiRecentSearches;
import com.bessarez.ecommercemobile.models.apimodels.EphemeralKeyRequest;
import com.bessarez.ecommercemobile.models.apimodels.EphemeralKeyResponse;
import com.bessarez.ecommercemobile.models.apimodels.PaymentIntentRequest;
import com.bessarez.ecommercemobile.models.apimodels.PaymentIntentResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EcommerceApi {

    @GET("/carousel_images")
    Call<ApiCarouselImages> getCarouselImages();

    @GET("/product_categories")
    Call<ApiProductCategories> getProductCategories();

    @GET("/products")
    Call<ApiProducts> getProducts();

    @GET("/products/{id}")
    Call<ApiProduct> getProduct(
            @Path("id") Long id
    );

    @GET("/registered_users")
    Call<ApiRegisteredUsers> getRegisteredUsers();

    @GET("/registered_users/email/{email}")
    Call<RegisteredUser> getRegisteredUserByEmail(
            @Path("email") String email,
            @Header("Authorization") String token);

    @GET("/user_viewed_products")
    Call<ApiUserViewedProducts> getUserViewedProducts();

    @POST("/login")
    Call<ApiToken> login(@Body ApiLogin apiLogin);

    @POST("/wish_products")
    Call<ApiWishProduct> addProductToWishList(@Body ApiWishProduct apiWishProduct);

    @GET("/wish_products/user/{id}")
    Call<ApiWishProducts> getWishListProducts(@Path("id") Long id);

    @GET("/suggested_products/{query}")
    Call<ApiSuggestedProducts> getSuggestedProducts(@Path("query") String query);

    @GET("/recent_searches/{userId}")
    Call<ApiRecentSearches> getRecentSearches(@Path("userId") Long userId);

    @GET("/products/search/{query}")
    Call<ApiProducts> getSearchResults(@Path("query") String query);

    @POST("/ephemeral_keys")
    Call<EphemeralKeyResponse> createEphemeralKey(@Body EphemeralKeyRequest eKeyRequest);

    @POST("/create_payment_intent")
    Call<PaymentIntentResponse> createPaymentIntent();

    @GET("/user_orders/user/{userId}")
    Call<ApiUserOrders> getUserOrders(@Path("userId") Long userId);
}
