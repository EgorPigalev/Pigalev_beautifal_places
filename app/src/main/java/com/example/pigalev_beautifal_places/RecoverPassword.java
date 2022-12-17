package com.example.pigalev_beautifal_places;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RecoverPassword extends AppCompatActivity {

    EditText login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        login = findViewById(R.id.etLogin);
    }

    public void nextBack(View view)
    {
        startActivity(new Intent(this, Login.class));
    }

    public void sendMail(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW
        , Uri.parse("mailto: 1pigaleve@mail.ru"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Востановить пароль");
        intent.putExtra(Intent.EXTRA_TEXT, login.getText().toString());
        startActivity(intent);
    }
}