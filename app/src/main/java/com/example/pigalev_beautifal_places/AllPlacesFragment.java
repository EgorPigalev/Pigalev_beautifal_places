package com.example.pigalev_beautifal_places;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPlacesFragment extends Fragment {

    View v;
    Connection connection;
    List<Mask> data;
    ListView listView;
    AdapterMask pAdapter;

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
        String query = "Select * From BeautifulPlace";
        RequestExecution(query);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_places, container, false);
    }

    public void enterMobile() {
        pAdapter.notifyDataSetInvalidated();
        listView.setAdapter(pAdapter);
    }
    public void RequestExecution(String query) {
        data = new ArrayList<Mask>();
        listView = (ListView) getActivity().findViewById(R.id.lvData);
        pAdapter = new AdapterMask(AllPlacesFragment.this.getContext(), data);
        try {
            BaseData baseData = new BaseData();
            connection = baseData.connectionClass();
            if (connection != null)
            {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next())
                {
                    Mask tempMask = new Mask
                            (resultSet.getInt("ID"),
                                    resultSet.getString("Name"),
                                    resultSet.getString("Picture")
                            );
                    data.add(tempMask);
                    pAdapter.notifyDataSetInvalidated();
                }
                connection.close();
            }
            else
            {

            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        enterMobile();
    }
}