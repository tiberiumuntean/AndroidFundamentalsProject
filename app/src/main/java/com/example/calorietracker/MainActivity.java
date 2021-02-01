package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mButtonSignUp;
    private Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializam SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedInUser = preferences.getString("LOGIN", "");

        // Daca exista un utilizator conectat, il trimitem la AppMainPageActivity
        if(loggedInUser != ""){
            Intent Intent = new Intent(MainActivity.this, AppMainPageActivity.class); // Prin terminatia .class ii trimitem tipul de activity care trebuie instantiat
            startActivity(Intent);
        } else {
            // Daca nu exista un utilizator conectat, il trimitem la MainActivity
            setContentView(R.layout.activity_main);

            // Signup Button
            mButtonSignUp = findViewById(R.id.button_signup_activity_main);
            mButtonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(Intent);
                }
            });

            // Login Button
            mButtonLogin = findViewById(R.id.button_login_activity_main);
            mButtonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Intent = new Intent(MainActivity.this, LogInActivity.class); // prin terminatia .class ii trimitem tipul de activity care trebuie instantiat
                    startActivity(Intent);
                }
            });
        }
    }
}