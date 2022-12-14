package com.example.pigalev_beautifal_places;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlaceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlaceFragment newInstance(String param1, String param2) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button btnBack, btnAddPlace;
    Spinner TypeLocality, Country;
    TextView delete;
    ImageView mainImage;
    String varcharPicture;
    ProgressBar loadingPB;
    EditText etName, etLatitude, etLongitude, etMultiLine;

    public void UpdatePicture()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
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
                        mainImage.setImageBitmap(null);
                        mainImage.setImageBitmap(bitmap);
                        delete.setVisibility(View.VISIBLE);
                        varcharPicture = BitMapToString(bitmap);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);
        loadingPB = view.findViewById(R.id.pbLoading);
        delete = view.findViewById(R.id.tvDeletePicture);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePicture();
            }
        });
        mainImage = view.findViewById(R.id.ivMainPicture);
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePicture();
            }
        });
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                UserPlacesFragment fragment = new UserPlacesFragment();
                ft.replace(R.id.perehodAddPlace, fragment);
                ft.commit();
            }
        });
        etName = view.findViewById(R.id.etName);
        etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etName.setHint("");
            else
                etName.setHint("Наименование");
        });
        etLongitude = view.findViewById(R.id.etLongitude);
        etLongitude.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLongitude.setHint("");
            else
                etLongitude.setHint("Долгота");
        });
        etLatitude = view.findViewById(R.id.etLatitude);
        etLatitude.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLatitude.setHint("");
            else
                etLatitude.setHint("Широта");
        });
        etMultiLine = view.findViewById(R.id.etMultiLine);
        etMultiLine.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etMultiLine.setHint("");
            else
                etMultiLine.setHint("Описание туристического места");
        });
        btnAddPlace = view.findViewById(R.id.btnAddPlace);
        btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataBeautifulPlace(String.valueOf(etName.getText()), String.valueOf(etMultiLine.getText()), Main.index, Float.parseFloat(String.valueOf(etLatitude.getText())), Float.parseFloat(String.valueOf(etLongitude.getText())), varcharPicture, false);
            }
        });
        TypeLocality = view.findViewById(R.id.spTypeLocality);
        new GetTypeLocality().execute();
        Country = view.findViewById(R.id.spCountry);
        new GetAddress().execute();
        return view;
    }

    private class GetTypeLocality extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                loadingPB.setVisibility(View.VISIBLE);
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/TypeLocalitys");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONArray tempArray = new JSONArray(s);
                String[] str_array = new String[tempArray.length()];
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    str_array[i]=productJson.getString("type_locality");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, str_array);
                TypeLocality.setAdapter(adapter);
                loadingPB.setVisibility(View.INVISIBLE);
            }
            catch (Exception ignored)
            {

            }
        }
    }

    private class GetAddress extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/Addresses");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception exception)
            {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                JSONArray tempArray = new JSONArray(s);
                String[] str_array = new String[tempArray.length()];
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    str_array[i]=productJson.getString("country");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, str_array);
                Country.setAdapter(adapter);
                loadingPB.setVisibility(View.INVISIBLE);
            }
            catch (Exception ignored)
            {

            }
        }
    }
    public void DeletePicture()
    {
        mainImage.setImageBitmap(null);
        varcharPicture = null;
        mainImage.setImageResource(R.drawable.absence);
        delete.setVisibility(View.INVISIBLE);
    }

    private void postDataBeautifulPlace(String name, String description, int id_user, float latitude, float longitude, String main_image, boolean accepted) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        BeautifulPlacesModel modal = new BeautifulPlacesModel(name, description, id_user, 1, 1, latitude, longitude, main_image, accepted);

        String country = Country.getSelectedItem().toString();
        String typeLocality = TypeLocality.getSelectedItem().toString();

        Call<BeautifulPlacesModel> call = retrofitAPI.createBeautifulPlace(modal, "Россия", "Горы");

        call.enqueue(new Callback<BeautifulPlacesModel>() {
            @Override
            public void onResponse(Call<BeautifulPlacesModel> call, Response<BeautifulPlacesModel> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При добавление нового туристического места возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Туристическое место отправлено на проверку администратору системы", Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<BeautifulPlacesModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При добавление нового туристического места возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }
}