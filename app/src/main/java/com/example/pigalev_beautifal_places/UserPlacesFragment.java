package com.example.pigalev_beautifal_places;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPlacesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserPlacesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserPlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPlacesFragment newInstance(String param1, String param2) {
        UserPlacesFragment fragment = new UserPlacesFragment();
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

    private List<Mask> listBeautifulPlaces = new ArrayList<>();

    ListView listView;
    AdapterMask pAdapter;
    ProgressBar loading;
    Button addPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_places, container, false);
        addPlace = (Button) view.findViewById(R.id.btnAddPlace);
        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddPlaceFragment fragment = new AddPlaceFragment();
                ft.replace(R.id.perehodAddPlace, fragment);
                ft.commit();
            }
        });
        loading = view.findViewById(R.id.pbLoading);
        loading.setVisibility(View.VISIBLE);
        ListView ivProducts = view.findViewById(R.id.lvData);
        pAdapter = new AdapterMask(getActivity(), listBeautifulPlaces);
        ivProducts.setAdapter(pAdapter);
        listView = view.findViewById(R.id.lvData);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = (int)id;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SinglePlaceFragment fragment = new SinglePlaceFragment(index, "UserPlace");
                ft.replace(R.id.perehodAddPlace, fragment);
                ft.commit();
            }
        });
        new GetBeutifulPlace().execute();
        return view;
    }

    private class GetBeutifulPlace extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/??????????????????/api/BeautifulPlaces/"+ Main.index + "?b=" + true);
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
                loading.setVisibility(View.GONE);
            }
            catch (Exception ignored)
            {

            }
        }
    }
}