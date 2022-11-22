package com.example.pigalev_beautifal_places;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class Registration extends AppCompatActivity {

    ImageView imPassword, imConfirmPassword;
    EditText etLogin, etPassword, etConfirmPassword;

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
        startActivity(new Intent(this, Login.class));
    }

    public void nextAuthorization(View v)
    {
        startActivity(new Intent(this, Login.class));
    }
}