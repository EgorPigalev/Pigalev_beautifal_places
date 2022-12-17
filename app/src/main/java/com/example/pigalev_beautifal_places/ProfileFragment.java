package com.example.pigalev_beautifal_places;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserModel userModel;
    Button deletePhoto, deleteUser, updateUser;
    ProgressBar progressBar;
    TextView login, countPlaces;
    ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void UpdatePicture()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar = v.findViewById(R.id.pbLoading);
        login = (TextView) v.findViewById(R.id.nameProfile);
        countPlaces = (TextView) v.findViewById(R.id.countPlaces);
        deleteUser = v.findViewById(R.id.btnDeleteProfile);
        deleteUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteUsers();
            }
        });
        deletePhoto = v.findViewById(R.id.btnDeletePhoto);
        deletePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deletePicture();
            }
        });
        image = v.findViewById(R.id.imageProfile);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                UpdatePicture();
            }
        });
        updateUser = v.findViewById(R.id.btnChangeLoginAndPassword);
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                UpdUserFragment fragment = new UpdUserFragment();
                ft.replace(R.id.UserProfilePerehod, fragment);
                ft.commit();
            }
        });
        callGetUser();
        callCount();
        return v;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Bitmap bitmap = null;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri selectedImage = result.getData().getData();
                        try
                        {
                            Context context = (Context)getActivity();
                            ContentResolver result1 = (ContentResolver)context.getContentResolver();
                            bitmap = MediaStore.Images.Media.getBitmap(result1, selectedImage);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        image.setImageBitmap(null);
                        image.setImageBitmap(bitmap);
                        deletePhoto.setVisibility(View.VISIBLE);
                        String varcharPicture = BitMapToString(bitmap);
                        userModel.setImage(varcharPicture);
                        callPUTDataMethod(Main.index);
                    }
                }
            });


    public String BitMapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void callGetUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<UserModel> call = retrofitAPI.getDATAUser(Main.index);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressBar.setVisibility(View.GONE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                userModel = new UserModel(0, response.body().getLogin(), response.body().getPassword(), response.body().getImage(), response.body().getId_role());
                login.setText(response.body().getLogin());
                if(response.body().getImage() == null)
                {
                    image.setImageResource(R.drawable.absence);
                    deletePhoto.setVisibility(View.GONE);
                }
                else
                {
                    Bitmap bitmap = StringToBitMap(response.body().getImage());
                    image.setImageBitmap(bitmap);
                    deletePhoto.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При выводе данных возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void callPUTDataMethod(int index) {

        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<UserModel> call = retrofitAPI.updateUser(index, userModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressBar.setVisibility(View.GONE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При обнавление фото возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При обнавление фото возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void callCount()
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Integer> call = retrofitAPI.getCountBeautifulPlaces(Main.index);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "При определение кол-ва добавленных мест возникла ошибка", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                    return;
                }
                String str = String.valueOf(response.body().intValue());
                countPlaces.setText(str);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "При определение кол-ва добавленных мест возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
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

    public void deletePicture()
    {
        image.setImageBitmap(null);
        image.setImageResource(R.drawable.absence);
        deletePhoto.setVisibility(View.GONE);
        userModel.setImage("null");
        callPUTDataMethod(Main.index);
    }
    public void deleteUsers()
    {
        callDeleteDataMethod();
    }

    private void callDeleteDataMethod() {

        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteUser(Main.index);
        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {
                progressBar.setVisibility(View.GONE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При удание пользователя возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getActivity(), Login.class);
                getActivity().startActivity(myIntent);
            }
            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                Toast.makeText(getActivity(), "При удаление пользователя возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}