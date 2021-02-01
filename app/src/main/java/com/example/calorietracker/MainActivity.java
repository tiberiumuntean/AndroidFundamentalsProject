package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mNextButton;
    private Button mButtonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedInUser = preferences.getString("LOGIN", "");

        if(loggedInUser != ""){
            Intent Intent = new Intent(MainActivity.this, AppMainPageActivity.class); // prin terminatia .class ii trimitem tipul de activity care trebuie instantiat

            startActivity(Intent);
        } else {
            setContentView(R.layout.activity_main);
            // creem o referinta spre butonul de Sign In
            mNextButton = findViewById(R.id.button_signup_activity_main);

            // creem un mecanism de tip callback - butonul face o actiune
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // creem o intent prin care pornim SignUpActivity
                    Intent Intent = new Intent(MainActivity.this, SignUpActivity.class); // prin terminatia .class ii trimitem tipul de activity care trebuie instantiat

                    startActivity(Intent);
                }
            });


            // creem o referinta spre butinunl de Log In
            mButtonLogin = findViewById(R.id.button_login_activity_main);

            mButtonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // creem o intent prin care pornim LogInActivity
                    Intent Intent = new Intent(MainActivity.this, LogInActivity.class); // prin terminatia .class ii trimitem tipul de activity care trebuie instantiat

                    startActivity(Intent);
                }
            });
        }

    }

}