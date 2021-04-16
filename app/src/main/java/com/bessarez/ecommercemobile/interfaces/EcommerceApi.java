package com.bessarez.ecommercemobile.interfaces;

import com.bessarez.ecommercemobile.models.RegisteredUser;
import com.bessarez.ecommercemobile.models.apimodels.RequestLogin;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiCarouselImages;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiLogin;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiProductCategories;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiProducts;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiRegisteredUsers;
import com.bessarez.ecommercemobile.models.apimodels.ResponseApiUserViewedProducts;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EcommerceApi {

    @GET("/carousel_images")
    Call<ResponseApiCarouselImages> getCarouselImages();

    @GET("/product_categories")
    Call<ResponseApiProductCategories> getProductCategories();

    @GET("/products")
    Call<ResponseApiProducts> getProducts();

    @GET("/registered_users")
    Call<ResponseApiRegisteredUsers> getRegisteredUsers();

    @GET("/registered_users/email/{email}")
    Call<RegisteredUser> getRegisteredUserByEmail(
            @Path("email") String email,
            @Header("Authorization") String token);

    @GET("/user_viewed_products")
    Call<ResponseApiUserViewedProducts> getUserViewedProducts();

    @POST("/login")
    Call<ResponseApiLogin> login(@Body RequestLogin requestLogin);

}
