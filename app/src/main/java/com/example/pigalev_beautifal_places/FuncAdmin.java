package com.example.pigalev_beautifal_places;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FuncAdmin extends AppCompatActivity {

    private List<Mask> listBeautifulPlaces = new ArrayList<>();

    ListView listView;
    AdapterMask pAdapter;
    ImageView openSearch;
    Spinner TypeLocality, Country, fieldSort, order;
    ConstraintLayout settings;
    SearchView name;
    String textSearch;
    Boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_admin);
        bool = true;
        textSearch = "";
        ListView ivProducts = findViewById(R.id.lvData);
        pAdapter = new AdapterMask(this, listBeautifulPlaces);
        ivProducts.setAdapter(pAdapter);
        fieldSort = findViewById(R.id.spField);
        fieldSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetBeutifulPlace().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        order = findViewById(R.id.spSort);
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetBeutifulPlace().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        name = findViewById(R.id.svSearchName);
        name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(FuncAdmin.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                textSearch = newText;
                new GetBeutifulPlace().execute();
                return false;
            }
        });
        listView = findViewById(R.id.lvData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int)id;
                Intent intent = new Intent(FuncAdmin.this, UpdDataAdmin.class);
                Bundle b = new Bundle();
                b.putInt("key", index);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        settings = findViewById(R.id.settings);
        TypeLocality = findViewById(R.id.spTypeLocality);
        TypeLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetBeutifulPlace().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Country = findViewById(R.id.spCountry);
        Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new GetBeutifulPlace().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new GetTypeLocality().execute();
        new GetAddress().execute();
        openSearch = findViewById(R.id.ivOpenSearch);
        openSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settings.getVisibility() == View.VISIBLE)
                {
                    settings.setVisibility(View.GONE);
                }
                else
                {
                    settings.setVisibility(View.VISIBLE);
                }
            }
        });
        new GetBeutifulPlace().execute();
    }

    String[][] type_localityArray;
    String[][] countryArray;

    private class GetTypeLocality extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/TypeLocalitys");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray tempArray = new JSONArray(s);
                String[] str_array = new String[tempArray.length()+1];
                str_array[0] = "";
                type_localityArray = new String[tempArray.length()+1][2];
                type_localityArray[0][0] = "0";
                type_localityArray[0][1] = "";
                for (int i = 0; i < tempArray.length(); i++) {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    str_array[i+1] = productJson.getString("type_locality");
                    type_localityArray[i+1][0] = productJson.getString("id_type_locality");
                    type_localityArray[i+1][1] = productJson.getString("type_locality");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FuncAdmin.this, android.R.layout.simple_spinner_item, str_array);
                TypeLocality.setAdapter(adapter);
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
                String[] str_array = new String[tempArray.length()+1];
                str_array[0] = "";
                countryArray = new String[tempArray.length()+1][2];
                countryArray[0][0] = "0";
                countryArray[0][1] = "";
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    str_array[i+1]=productJson.getString("country");
                    countryArray[i+1][0] = productJson.getString("id_address");
                    countryArray[i+1][1] = productJson.getString("country");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FuncAdmin.this, android.R.layout.simple_spinner_item, str_array);
                Country.setAdapter(adapter);
            }
            catch (Exception ignored)
            {

            }
        }
    }

    private class GetBeutifulPlace extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String country;
                String typeLocality;
                String field;
                String fieldOrder;
                URL url;
                try {
                    country = Country.getSelectedItem().toString();
                    typeLocality = TypeLocality.getSelectedItem().toString();
                    field = fieldSort.getSelectedItem().toString();
                    fieldOrder = order.getSelectedItem().toString();
                    url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/BeautifulPlaces?b=" + bool + "&name=" + textSearch +"&id_address=" + getIdCountry(country) +"&id_type_locality=" + getIdTypeLocality(typeLocality) +"&fieldSort=" + field +"&valueSort=" + fieldOrder);
                }
                catch (Exception exception)
                {
                    field = "";
                    fieldOrder = "";
                    url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/BeautifulPlaces?b=" + bool + "&name=" + "" +"&id_address=" + 0 +"&id_type_locality=" + 0 +"&fieldSort=" + field +"&valueSort=" + fieldOrder);
                }

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
                listBeautifulPlaces.clear();
                pAdapter.notifyDataSetInvalidated();
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0;i<tempArray.length();i++)
                {
                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempProduct = new Mask(
                            productJson.getInt("id_beautiful_place"),
                            productJson.getString("name"),
                            productJson.getString("image")
                    );
                    listBeautifulPlaces.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
            }
            catch (Exception ignored)
            {

            }
        }
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

    public void changeNew(View v)
    {
        bool = false;
        new GetBeutifulPlace().execute();
    }

    public void changeOld(View v)
    {
        bool = true;
        new GetBeutifulPlace().execute();
    }
    public void back(View v)
    {
        startActivity(new Intent(this, Login.class));
    }
}