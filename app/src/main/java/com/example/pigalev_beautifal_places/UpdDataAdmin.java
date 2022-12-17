package com.example.pigalev_beautifal_places;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
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

public class UpdDataAdmin extends AppCompatActivity {

    Button btnBack;
    Spinner TypeLocality, Country;
    TextView delete;
    ImageView mainImage;
    String varcharPicture;
    ProgressBar loadingPB;
    EditText etName, etLatitude, etLongitude, etMultiLine;
    int index;

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
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upd_data_admin);
        Bundle arguments = getIntent().getExtras();
        index = arguments.getInt("key");
        loadingPB = findViewById(R.id.pbLoading);
        TypeLocality = findViewById(R.id.spTypeLocality);
        new GetTypeLocality().execute();
        Country = findViewById(R.id.spCountry);
        new GetAddress().execute();
        delete = findViewById(R.id.tvDeletePicture);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePicture();
            }
        });
        mainImage = findViewById(R.id.ivMainPicture);
        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePicture();
            }
        });
        btnBack = (Button)findViewById(R.id.btnBack);
        etName = findViewById(R.id.etName);
        etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etName.setHint("");
            else
                etName.setHint("Наименование");
        });
        etLongitude = findViewById(R.id.etLongitude);
        etLongitude.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLongitude.setHint("");
            else
                etLongitude.setHint("Долгота");
        });
        etLatitude = findViewById(R.id.etLatitude);
        etLatitude.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLatitude.setHint("");
            else
                etLatitude.setHint("Широта");
        });
        etMultiLine = findViewById(R.id.etMultiLine);
        etMultiLine.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etMultiLine.setHint("");
            else
                etMultiLine.setHint("Описание туристического места");
        });
        callGetDataMethod();
    }

    String[][] type_localityArray;
    String[][] countryArray;

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
                type_localityArray = new String[tempArray.length()][2];
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    str_array[i]=productJson.getString("type_locality");
                    type_localityArray[i][0] = productJson.getString("id_type_locality");
                    type_localityArray[i][1] = productJson.getString("type_locality");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdDataAdmin.this, android.R.layout.simple_spinner_item, str_array);
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
                countryArray = new String[tempArray.length()][2];
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    str_array[i]=productJson.getString("country");
                    countryArray[i][0] = productJson.getString("id_address");
                    countryArray[i][1] = productJson.getString("country");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdDataAdmin.this, android.R.layout.simple_spinner_item, str_array);
                Country.setAdapter(adapter);
                loadingPB.setVisibility(View.INVISIBLE);
            }
            catch (Exception ignored)
            {

            }
        }
    }

    public void callGetDataMethod()
    {
        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<BeautifulPlacesModel> call = retrofitAPI.getDATABeautifulPlace(index);
        call.enqueue(new Callback<BeautifulPlacesModel>() {
            @Override
            public void onResponse(Call<BeautifulPlacesModel> call, Response<BeautifulPlacesModel> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(UpdDataAdmin.this, "При выводе данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                etName.setText(response.body().getName());
                etMultiLine.setText(response.body().getDescription());
                etLatitude.setText(String.valueOf(response.body().getLatitude()));
                etLongitude.setText(String.valueOf(response.body().getLongitude()));
                TypeLocality.setSelection(getPositionTypeLocality(response.body().getId_type_locality()));
                Country.setSelection(getPositionCountry(response.body().getId_address()));
                if(response.body().getImage() == null)
                {
                    mainImage.setImageResource(R.drawable.absence);
                    delete.setVisibility(View.INVISIBLE);
                }
                else
                {
                    varcharPicture = response.body().getImage();
                    Bitmap bitmap = StringToBitMap(response.body().getImage());
                    mainImage.setImageBitmap(bitmap);
                    delete.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<BeautifulPlacesModel> call, Throwable t) {
                Toast.makeText(UpdDataAdmin.this, "При выводе данных возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
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

    public void DeletePicture()
    {
        mainImage.setImageBitmap(null);
        varcharPicture = null;
        mainImage.setImageResource(R.drawable.absence);
        delete.setVisibility(View.INVISIBLE);
    }

    public void btnBack(View view)
    {
        startActivity(new Intent(this, FuncAdmin.class));
    }

    public void updData(View view)
    {
        if(etName.getText().toString().equals(""))
        {
            Toast.makeText(UpdDataAdmin.this, "Поле наименование не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etMultiLine.getText().toString().equals(""))
        {
            Toast.makeText(UpdDataAdmin.this, "Поле описание не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etLatitude.getText().toString().equals(""))
        {
            Toast.makeText(UpdDataAdmin.this, "Поле широта не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etLongitude.getText().toString().equals(""))
        {
            Toast.makeText(UpdDataAdmin.this, "Поле долгота не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        callPUTDataMethod(etName.getText().toString(), etMultiLine.getText().toString(), 0, getIdCountry(Country.getSelectedItem().toString()), getIdTypeLocality(TypeLocality.getSelectedItem().toString()), Float.valueOf(etLatitude.getText().toString()), Float.valueOf(etLongitude.getText().toString()), varcharPicture, true);
    }

    private void callPUTDataMethod(String name, String discription, int id_user, int id_address, int id_type_locality, float latitude, float longitude, String image, boolean accepted) {

        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        BeautifulPlacesModel modal = new BeautifulPlacesModel(name, discription, id_user, id_address, id_type_locality, latitude, longitude, image, accepted);
        Call<BeautifulPlacesModel> call = retrofitAPI.updateBeautifulPlace(index, modal);
        call.enqueue(new Callback<BeautifulPlacesModel>() {
            @Override
            public void onResponse(Call<BeautifulPlacesModel> call, Response<BeautifulPlacesModel> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(UpdDataAdmin.this, "При изменение данных возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(UpdDataAdmin.this, "Данные изменены", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BeautifulPlacesModel> call, Throwable t) {
                Toast.makeText(UpdDataAdmin.this, "При изменение записи возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void deleteData(View view)
    {
        callDeleteDataMethod();
    }

    private void callDeleteDataMethod() {

        loadingPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call call = retrofitAPI.deleteBeautifulPlaces(index);
        call.enqueue(new Callback<BeautifulPlacesModel>() {
            @Override
            public void onResponse(Call<BeautifulPlacesModel> call, Response<BeautifulPlacesModel> response) {
                loadingPB.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(UpdDataAdmin.this, "При удание возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(UpdDataAdmin.this, "Удаление прошло успешно", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(UpdDataAdmin.this, FuncAdmin.class);
                startActivity(myIntent);
            }
            @Override
            public void onFailure(Call<BeautifulPlacesModel> call, Throwable t) {
                Toast.makeText(UpdDataAdmin.this, "При удаление возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                loadingPB.setVisibility(View.INVISIBLE);
            }
        });
    }

    private int getIdTypeLocality(String typeLocality)
    {
        for (int i = 0; i < type_localityArray.length; i++)
        {
            if(type_localityArray[i][1].equals(typeLocality))
            {
                return Integer.parseInt(type_localityArray[i][0]);
            }
        }
        return 0;
    }

    private int getPositionTypeLocality(int id_typeLocality)
    {
        for (int i = 0; i < type_localityArray.length; i++)
        {
            if(type_localityArray[i][0] == String.valueOf(id_typeLocality))
            {
                return i;
            }
        }
        return 0;
    }

    private int getIdCountry(String country)
    {
        for (int i = 0; i < countryArray.length; i++)
        {
            if(countryArray[i][1].equals(country))
            {
                return Integer.parseInt(countryArray[i][0]);
            }
        }
        return 0;
    }

    private int getPositionCountry(int id_country)
    {
        for (int i = 0; i < countryArray.length; i++)
        {
            if(countryArray[i][0] == String.valueOf(id_country))
            {
                return i;
            }
        }
        return 0;
    }
}