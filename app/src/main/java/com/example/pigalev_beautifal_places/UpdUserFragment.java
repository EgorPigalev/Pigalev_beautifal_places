package com.example.pigalev_beautifal_places;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdUserFragment newInstance(String param1, String param2) {
        UpdUserFragment fragment = new UpdUserFragment();
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

    Button back, update;
    UserModel user;
    ImageView imPassword, imConfirmPassword;
    EditText etLogin, etPassword, etConfirmPassword;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upd_user, container, false);
        user = ProfileFragment.userModel;
        back = (Button) view.findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ProfileFragment fragment = new ProfileFragment();
                ft.replace(R.id.UserProfilePerehod, fragment);
                ft.commit();
            }
        });
        etLogin = view.findViewById(R.id.etLogin);
        etLogin.setText(user.getLogin());
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        imPassword = view.findViewById(R.id.ivVisiblePassword);
        imPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVisiblePassword();
            }
        });
        imConfirmPassword = view.findViewById(R.id.ivVisibleConfirmPassword);
        imConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVisibleConfirmPassword();
            }
        });
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        progressBar = view.findViewById(R.id.pbLoading);

        etLogin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etLogin.setHint("");
            else
                etLogin.setHint("Логин");
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etPassword.setHint("");
            else
                etPassword.setHint("Пароль");
        });

        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                etConfirmPassword.setHint("");
            else
                etConfirmPassword.setHint("Повторный пароль");
        });
        update = view.findViewById(R.id.btnUpdate);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = String.valueOf(etLogin.getText());
                if(login.replaceAll("\\s+", " ").equals(""))
                {
                    Toast.makeText(getActivity(),"Логин не может быть пустым", Toast.LENGTH_SHORT).show();
                    return;
                }
                callProverkaPassword();
            }
        });
        return view;
    }
    public void getVisiblePassword()
    {
        if(etPassword.getInputType() == 129)
        {
            imPassword.setImageResource(R.drawable.icon_visible);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            imPassword.setImageResource(R.drawable.icon_not_visible);
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void getVisibleConfirmPassword()
    {
        if(etConfirmPassword.getInputType() == 129)
        {
            imConfirmPassword.setImageResource(R.drawable.icon_visible);
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            imConfirmPassword.setImageResource(R.drawable.icon_not_visible);
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void callProverkaPassword()
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        String confirmPassword = String.valueOf(etConfirmPassword.getText());
        Call<UserModel> call = retrofitAPI.Login(user.getLogin(), confirmPassword);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "При проверке пароля возникла ошибка", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(!response.body().equals(null))
                {
                    if(!ProfileFragment.userModel.getLogin().equals(String.valueOf(etLogin.getText())))
                    {
                        callRegistration();
                    }
                    else
                    {
                        callPUTDataMethod(Main.index);
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Старый пароль введён не верно", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При проверке пароля возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void callRegistration()
    {
        progressBar.setVisibility(View.VISIBLE);
        String login = String.valueOf(etLogin.getText());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Boolean> call = retrofitAPI.examinationRegistration(login);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "При проверке логина возникла ошибка", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(response.body().equals(false))
                {
                    callPUTDataMethod(Main.index);
                }
                else
                {
                    Toast.makeText(getActivity(), "Пользователь с таким логиным уже зарегистрирован, выберите другой логин", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getActivity(), "При проверке логина возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void callPUTDataMethod(int index) {

        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        user.setLogin(String.valueOf(etLogin.getText()));
        String password = String.valueOf(etPassword.getText());
        if(!password.replaceAll("\\s+", " ").equals(""))
        {
            user.setPassword(password);
        }
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<UserModel> call = retrofitAPI.updateUser(index, user);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(!response.isSuccessful())
                {
                    Toast.makeText(getActivity(), "При измнение данных пользователя возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Данные успешно изменены", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ProfileFragment fragment = new ProfileFragment();
                ft.replace(R.id.UserProfilePerehod, fragment);
                ft.commit();
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getActivity(), "При измнение данных пользователя возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}