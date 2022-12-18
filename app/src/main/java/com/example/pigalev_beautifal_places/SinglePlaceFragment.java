package com.example.pigalev_beautifal_places;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SinglePlaceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int id;
    String fragment;
    public SinglePlaceFragment(int id, String fragment) {
        // Required empty public constructor
        this.id = id;
        this.fragment = fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SinglePlaceFragment.
     */

    Button btnBack;
    ProgressBar pbLoading;
    TextView name, tvMultiLine, tvLatitude, tvLongitude, tvTypeLocality, tvCountry, tvAutor, countGrades;
    ImageView image, imageMap, imageLike, imFavorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_place, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        pbLoading = view.findViewById(R.id.pbLoading);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment.equals("AllPlace"))
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    AllPlacesFragment fragment = new AllPlacesFragment();
                    ft.replace(R.id.AllPlacesPerehod, fragment);
                    ft.commit();
                }
                if(fragment.equals("UserPlace"))
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    UserPlacesFragment fragment = new UserPlacesFragment();
                    ft.replace(R.id.perehodAddPlace, fragment);
                    ft.commit();
                }
                if(fragment.equals("FavoritePlace"))
                {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    FovoritesFragment fragment = new FovoritesFragment();
                    ft.replace(R.id.favoritePlacePerehod, fragment);
                    ft.commit();
                }
            }
        });
        countGrades = view.findViewById(R.id.tvCountLike);
        callCountLike();
        tvCountry = view.findViewById(R.id.tbAddress);
        tvTypeLocality = view.findViewById(R.id.tbTypeLocality);
        name = view.findViewById(R.id.tvName);
        tvAutor = view.findViewById(R.id.tvAutor);
        image = view.findViewById(R.id.ivMainPicture);
        tvMultiLine = view.findViewById(R.id.tvMultiLine);
        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);
        imageMap = view.findViewById(R.id.ivLocation);
        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:" + tvLatitude.getText().toString() + "," + tvLongitude.getText().toString()));
                startActivity(intent);
            }
        });
        imageLike = view.findViewById(R.id.ivLike);
        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageLike.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_like_not_click).getConstantState())
                {
                    imageLike.setImageResource(R.drawable.icon_like_click);
                    postGrades();
                }
                else
                {
                    imageLike.setImageResource(R.drawable.icon_like_not_click);
                    callDeleteGrades();
                }
            }
        });
        imFavorite = view.findViewById(R.id.ivFavorite);
        imFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imFavorite.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.icon_not_favorites).getConstantState())
                {
                    imFavorite.setImageResource(R.drawable.icon_favorites);
                    postFavorite();
                }
                else
                {
                    imFavorite.setImageResource(R.drawable.icon_not_favorites);
                    callDeleteFavorite();
                }
            }
        });
        callGetBeutifulPlace();
        callGrades();
        callFavorite();
        return view;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void callGrades()
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Boolean> call = retrofitAPI.getProverkaGrades(id, Main.index);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При проверки понравившегося места возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body().equals(false))
                {
                    imageLike.setImageResource(R.drawable.icon_like_not_click);

                }
                else
                {
                    imageLike.setImageResource(R.drawable.icon_like_click);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), "При проверки понравившегося места возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void callDeleteGrades() {

        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteGrades(id, Main.index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При снятие пометки нравится возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                callCountLike();
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(getActivity(), "При снятие пометки нравится возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void postGrades() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        FavoritesModel modal = new FavoritesModel(Main.index, id);

        Call<GradesModel> call = retrofitAPI.createGrades(modal);

        call.enqueue(new Callback<GradesModel>() {
            @Override
            public void onResponse(Call<GradesModel> call, Response<GradesModel> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При пометке нравится возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                pbLoading.setVisibility(View.INVISIBLE);
                callCountLike();
            }
            @Override
            public void onFailure(Call<GradesModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При пометке нравится возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void callCountLike()
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Integer> call = retrofitAPI.getCountGrades(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При определение количества лайков возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                countGrades.setText(response.body().toString());
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "При определение количества лайков возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void callFavorite()
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Boolean> call = retrofitAPI.getProverkaFavorite(id, Main.index);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При определение избранное возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body().equals(false))
                {
                    imFavorite.setImageResource(R.drawable.icon_not_favorites);

                }
                else
                {
                    imFavorite.setImageResource(R.drawable.icon_favorites);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), "При определение избранное возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void callDeleteFavorite() {

        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteFavorite(id, Main.index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При снятие пометки избранное возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(getActivity(), "При снятие пометки избранное возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void postFavorite() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        FavoritesModel modal = new FavoritesModel(Main.index, id);

        Call<FavoritesModel> call = retrofitAPI.createFavorite(modal);

        call.enqueue(new Callback<FavoritesModel>() {
            @Override
            public void onResponse(Call<FavoritesModel> call, Response<FavoritesModel> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При пометке избранное возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                pbLoading.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<FavoritesModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При пометке избранное возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void callGetBeutifulPlace()
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<BeautifulPlacesModel> call = retrofitAPI.getDATABeautifulPlace(id);
        call.enqueue(new Callback<BeautifulPlacesModel>() {
            @Override
            public void onResponse(Call<BeautifulPlacesModel> call, Response<BeautifulPlacesModel> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                name.setText(response.body().getName());
                if(response.body().getImage() == null)
                {
                    image.setImageResource(R.drawable.absence);
                }
                else
                {
                    Bitmap bitmap = StringToBitMap(response.body().getImage());
                    image.setImageBitmap(bitmap);
                }
                tvMultiLine.setText(response.body().getDescription());
                tvLatitude.setText(String.valueOf(response.body().getLatitude()));
                tvLongitude.setText(String.valueOf(response.body().getLongitude()));
                callGetTypeLocality(response.body().getId_type_locality());
                callGetAddress(response.body().getId_address());
                callGetUserAutor(response.body().getId_user());
            }
            @Override
            public void onFailure(Call<BeautifulPlacesModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При выводе данных возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void callGetTypeLocality(int id_type_locality)
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<TypeLocalityModel> call = retrofitAPI.getTypeLocality(id_type_locality);
        call.enqueue(new Callback<TypeLocalityModel>() {
            @Override
            public void onResponse(Call<TypeLocalityModel> call, Response<TypeLocalityModel> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При выводе типа достопримечательности возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvTypeLocality.setText(String.valueOf(response.body().getType_locality()));
            }
            @Override
            public void onFailure(Call<TypeLocalityModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При выводе типа достопримечательности возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void callGetAddress(int id_address)
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<AddressModel> call = retrofitAPI.getAddress(id_address);
        call.enqueue(new Callback<AddressModel>() {
            @Override
            public void onResponse(Call<AddressModel> call, Response<AddressModel> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При выводе страны расположения возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvCountry.setText("Страна: " + response.body().getAddress());
            }
            @Override
            public void onFailure(Call<AddressModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При выводе страны расположения возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void callGetUserAutor(int id)
    {
        pbLoading.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<UserModel> autor = retrofitAPI.getDATAUser(id);
        autor.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                pbLoading.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При опредление автора возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvAutor.setText("Автор: " + response.body().getLogin());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При опредление автора возникла ошибка", Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }
}