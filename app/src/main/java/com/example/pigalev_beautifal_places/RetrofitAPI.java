package com.example.pigalev_beautifal_places;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI
{
    @PUT("Users/{id}")
    Call<UserModel> updateUser(@Path("id") int id, @Body UserModel userModel);

    @DELETE("Users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @GET("Users")
    Call<Integer> Login(@Query("login") String login, @Query("password") String password);

    @GET("Users")
    Call<Integer> getCountBeautifulPlaces(@Query("id_user") int id_user);

    @GET("Users")
    Call<Boolean> examinationRegistration(@Query("login") String login);

    @POST("Users")
    Call<UserModel> createUser(@Body UserModel userModel);

    @GET("Users/{id}")
    Call<UserModel> getDATAUser(@Query("id") int id);

    @GET("BeautifulPlaces/{id}")
    Call<BeautifulPlacesModel> getDATABeautifulPlace(@Query("id") int id);

    @POST("BeautifulPlaces")
    Call<BeautifulPlacesModel> createBeautifulPlace(@Body BeautifulPlacesModel beautifulPlacesModel);
}
