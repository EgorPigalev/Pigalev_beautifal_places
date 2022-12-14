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
    Call<UserModel> Login(@Query("login") String login, @Query("password") String password);

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

    @GET("TypeLocalitys/{id}")
    Call<TypeLocalityModel> getTypeLocality(@Query("id") int id);

    @GET("Addresses/{id}")
    Call<AddressModel> getAddress(@Query("id") int id);

    @POST("BeautifulPlaces")
    Call<BeautifulPlacesModel> createBeautifulPlace(@Body BeautifulPlacesModel beautifulPlacesModel);

    @GET("Favorites/proverkaLike")
    Call<Boolean> getProverkaFavorite(@Query("id_beautifulPlace") int id_beautifulPlace, @Query("id_user") int id_user);

    @POST("Favorites")
    Call<FavoritesModel> createFavorite(@Body FavoritesModel favoritesModel);

    @DELETE("Favorites/deleteFavorite")
    Call<Void> deleteFavorite(@Query("id_beautifulPlace") int id_beautifulPlace, @Query("id_user") int id_user);

    @GET("Grades/proverkaGrades")
    Call<Boolean> getProverkaGrades(@Query("id_beautifulPlace") int id_beautifulPlace, @Query("id_user") int id_user);

    @GET("Grades/countGrades")
    Call<Integer> getCountGrades(@Query("id_beautifulPlace") int id_beautifulPlace);

    @POST("Grades")
    Call<GradesModel> createGrades(@Body FavoritesModel favoritesModel);

    @DELETE("Grades/deleteGrades")
    Call<Void> deleteGrades(@Query("id_beautifulPlace") int id_beautifulPlace, @Query("id_user") int id_user);

    @PUT("BeautifulPlaces")
    Call<BeautifulPlacesModel> updateBeautifulPlace(@Query("id") int id, @Body BeautifulPlacesModel beautifulPlacesModel);

    @DELETE("BeautifulPlaces/{id}")
    Call<Void> deleteBeautifulPlaces(@Path("id") int id);
}
