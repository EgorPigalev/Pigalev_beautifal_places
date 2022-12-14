package com.example.pigalev_beautifal_places;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    TextView name, tvMultiLine, tvLatitude, tvLongitude;
    ImageView image;

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
            }
        });
        name = view.findViewById(R.id.tvName);
        image = view.findViewById(R.id.ivMainPicture);
        tvMultiLine = view.findViewById(R.id.tvMultiLine);
        tvLatitude = view.findViewById(R.id.tvLatitude);
        tvLongitude = view.findViewById(R.id.tvLongitude);
        callGetBeutifulPlace();
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
            }
            @Override
            public void onFailure(Call<BeautifulPlacesModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При выводе данных возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }
}