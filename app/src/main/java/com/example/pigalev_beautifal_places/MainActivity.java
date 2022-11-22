package com.example.pigalev_beautifal_places;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    EditText textPassword, textEmailOrPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.ivVisiblePassword);
        textEmailOrPhone = findViewById(R.id.textEmailOrPhone);
        textPassword = findViewById(R.id.textPassword);
        textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        textEmailOrPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textEmailOrPhone.setHint("");
            else
                textEmailOrPhone.setHint("Enter Email / Phone Number");
        });

        textPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                textPassword.setHint("");
            else
                textPassword.setHint("Password");
        });
    }
    public void getVisiblePassword(View v)
    {
        if(textPassword.getInputType() == 129)
        {
            image.setImageResource(R.drawable.icon_visible);
            textPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else
        {
            image.setImageResource(R.drawable.icon_not_visible);
            textPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }
    public void nextRegistraation(View v)
    {
        startActivity(new Intent(this, Registration.class));
    }

    public void login(View v)
    {
        startActivity(new Intent(this, test.class));
    }
}