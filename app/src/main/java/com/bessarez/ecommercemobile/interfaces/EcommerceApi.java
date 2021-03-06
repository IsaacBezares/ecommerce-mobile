package com.bessarez.ecommercemobile.interfaces;

import com.bessarez.ecommercemobile.models.Cart;
import com.bessarez.ecommercemobile.models.CartItem;
import com.bessarez.ecommercemobile.models.Product;
import com.bessarez.ecommercemobile.models.RecentSearch;
import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.UserOrder;
import com.bessarez.ecommercemobile.models.UserViewedProduct;
import com.bessarez.ecommercemobile.models.WishProduct;
import com.bessarez.ecommercemobile.models.apimodels.ApiLogin;
import com.bessarez.ecommercemobile.models.apimodels.ApiSuggestedProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiUserOrders;
import com.bessarez.ecommercemobile.models.apimodels.ApiCarouselImages;
import com.bessarez.ecommercemobile.models.apimodels.ApiToken;
import com.bessarez.ecommercemobile.models.apimodels.ApiProductCategories;
import com.bessarez.ecommercemobile.models.apimodels.ApiProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiUserViewedProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiWishProducts;
import com.bessarez.ecommercemobile.models.apimodels.ApiRecentSearches;
import com.bessarez.ecommercemobile.models.apimodels.PaymentIntentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EcommerceApi {

    @POST("/unidle")
    Call<Void> unidle();

    @GET("/carousel_images")
    Call<ApiCarouselImages> getCarouselImages();

    @GET("/product_categories")
    Call<ApiProductCategories> getProductCategories();

    @GET("/products/random/{quantity}")
    Call<ApiProducts> getRandomProducts(@Path("quantity") int quantity);

    @GET("/products/{id}")
    Call<Product> getProduct(@Path("id") Long id);

    @GET("/registered_users/email/{email}")
    Call<RegisteredUser> getRegisteredUserByEmail(
            @Path("email") String email,
            @Header("Authorization") String token);

    @POST("/login")
    Call<ApiToken> login(@Body ApiLogin apiLogin);

    @POST("/registered_users")
    Call<RegisteredUser> signup(@Body RegisteredUser newUser);

    @POST("/wish_products")
    Call<Product> addProductToWishList(@Body WishProduct wishProduct);

    @GET("/wish_products/user/{id}")
    Call<ApiWishProducts> getWishListProducts(@Path("id") Long id);

    @GET("/suggested_products/{query}")
    Call<ApiSuggestedProducts> getSuggestedProducts(@Path("query") String query);

    @GET("/recent_searches/user/{userId}")
    Call<ApiRecentSearches> getRecentSearches(@Path("userId") Long userId);

    @POST("recent_searches")
    Call<RecentSearch> postRecentSearch(@Body RecentSearch recentSearch);

    @GET("/products/search/{query}")
    Call<ApiProducts> getSearchResults(@Path("query") String query);

    @PUT("/products/{id}/purchase/{quantity}")
    Call<Product> purchase(@Path("id") Long id, @Path("quantity") int quantity);

    @POST("/create_payment_intent")
    Call<PaymentIntentResponse> createPaymentIntent(@Body List<CartItem> checkoutList);

    @GET("/user_orders/user/{userId}")
    Call<ApiUserOrders> getUserOrders(@Path("userId") Long userId);

    @POST("/user_orders")
    Call<UserOrder> addOrder(@Body UserOrder userOrder);

    @GET("/carts/registered_users/{userId}")
    Call<Cart> getUserCart(@Path("userId") Long userId);

    @POST("/carts/registered_users/{userId}")
    Call<CartItem> addItemToCart(@Path("userId") Long userId, @Body CartItem cartItem);

    @DELETE("/carts/registered_users/{userId}")
    Call<Void> removeAllCartItems(@Path("userId") Long userId);

    @DELETE("/carts/cart_item/{cartItemId}")
    Call<Void> removeCartItem(@Path("cartItemId") Long cartItemId);

    @PUT("/carts/cart_item/{cartItemId}/quantity/{quantity}")
    Call<CartItem> updateCartItemQuantity(@Path("cartItemId") Long cartItemId, @Path("quantity") int quantity);

    @GET("/user_viewed_products/user/{userId}")
    Call<ApiProducts> getRecentlyViewedProducts(@Path("userId") Long userId);

    @POST("/user_viewed_products")
    Call<UserViewedProduct> addRecentlyViewedProduct(@Body UserViewedProduct userViewedProduct);

    @GET("/recommended_products/user/{userId}")
    Call<ApiProducts> getRecommendedProducts(@Path("userId") Long userId);
}
