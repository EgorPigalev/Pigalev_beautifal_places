package com.example.pigalev_beautifal_places;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends AppCompatActivity {

    ImageView imPassword, imConfirmPassword;
    EditText etLogin, etPassword, etConfirmPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        imPassword = findViewById(R.id.ivVisiblePassword);
        imConfirmPassword = findViewById(R.id.ivVisibleConfirmPassword);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        progressBar = findViewById(R.id.pbLoading);

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
    }
    public void getVisiblePassword(View v)
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

    public void getVisibleConfirmPassword(View v)
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


    public void nextRegistrarion(View v)
    {
        String login = String.valueOf(etLogin.getText());
        String password = String.valueOf(etPassword.getText());
        String confirmPassword = String.valueOf(etConfirmPassword.getText());
        if(login.replaceAll("\\s+", " ").equals(""))
        {
            Toast.makeText(this,"Логин не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.replaceAll("\\s+", " ").equals(""))
        {
            Toast.makeText(this,"Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Повторный пароль введён не верно", Toast.LENGTH_SHORT).show();
        }
        else
        {
            callRegistration();
        }
    }

    public void nextAuthorization(View v)
    {
        startActivity(new Intent(this, Login.class));
    }

    private void callRegistration()
    {
        progressBar.setVisibility(View.VISIBLE);
        String login = String.valueOf(etLogin.getText());
        String password = String.valueOf(etPassword.getText());
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
                    Toast.makeText(Registration.this, "При регистрации возникла ошибка", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(response.body().equals(false))
                {
                    postDataUser(login, password);
                }
                else
                {
                    Toast.makeText(Registration.this, "Пользователь с таким логиным уже зарегистрирован", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(Registration.this, "При авторизации возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void postDataUser(String login, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ПигалевЕД/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        UserModel modal = new UserModel(login, password, null, 2);

        Call<UserModel> call = retrofitAPI.createUser(modal);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(Registration.this, "При регистрации пользователя возникла ошибка", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Registration.this, "Пользователь успешно зарегистрирован", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                Intent myIntent = new Intent(Registration.this, Login.class);
                Registration.this.startActivity(myIntent);
            }
            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(Registration.this, "При регистрации пользователя возникла ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}