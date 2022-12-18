package com.example.pigalev_beautifal_places;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPlacesFragment extends Fragment {

    private List<Mask> listBeautifulPlaces = new ArrayList<>();

    ListView listView;
    AdapterMask pAdapter;
    ProgressBar loading;
    ImageView openSearch;
    Spinner TypeLocality, Country, fieldSort, order;
    ConstraintLayout settings;
    SearchView name;
    String textSearch;
    int i1 = 0, i2 = 0, i3 = 0, i4 = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllPlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllPlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllPlacesFragment newInstance(String param1, String param2) {
        AllPlacesFragment fragment = new AllPlacesFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_places, container, false);
        loading = view.findViewById(R.id.pbLoading);
        textSearch = "";
        ListView ivProducts = view.findViewById(R.id.lvData);
        pAdapter = new AdapterMask(getActivity(), listBeautifulPlaces);
        ivProducts.setAdapter(pAdapter);
        fieldSort = view.findViewById(R.id.spField);
        fieldSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(i1 > 0)
                {
                    new GetBeutifulPlace().execute();
                }
                i1++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        order = view.findViewById(R.id.spSort);
        order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(i2 > 0)
                {
                    new GetBeutifulPlace().execute();
                }
                i2++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        name = view.findViewById(R.id.svSearchName);
        name.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
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
        listView = view.findViewById(R.id.lvData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int)id;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SinglePlaceFragment fragment = new SinglePlaceFragment(index, "AllPlace");
                ft.replace(R.id.AllPlacesPerehod, fragment);
                ft.commit();
            }
        });
        settings = view.findViewById(R.id.settings);
        TypeLocality = view.findViewById(R.id.spTypeLocality);
        TypeLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(i3 > 0)
                {
                    new GetBeutifulPlace().execute();
                }
                i3++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Country = view.findViewById(R.id.spCountry);
        Country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(i4 > 0)
                {
                    new GetBeutifulPlace().execute();
                }
                i4++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new GetTypeLocality().execute();
        new GetAddress().execute();
        openSearch = view.findViewById(R.id.ivOpenSearch);
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
        return view;
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, str_array);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, str_array);
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
                    url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/BeautifulPlaces?b=" + true + "&name=" + textSearch +"&id_address=" + getIdCountry(country) +"&id_type_locality=" + getIdTypeLocality(typeLocality) +"&fieldSort=" + field +"&valueSort=" + fieldOrder);
                }
                catch (Exception exception)
                {
                    country = "";
                    typeLocality = "";
                    field = "";
                    fieldOrder = "";
                    url = new URL("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/BeautifulPlaces?b=" + true + "&name=" + "" +"&id_address=" + 0 +"&id_type_locality=" + 0 +"&fieldSort=" + field +"&valueSort=" + fieldOrder);
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
}