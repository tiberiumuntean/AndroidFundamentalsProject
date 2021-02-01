package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calorietracker.room.User;
import com.example.calorietracker.room.UserDao;
import com.example.calorietracker.room.UserDataBase;

public class LogInActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    private UserDao db;
    private UserDataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializam DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "user2-database.db").allowMainThreadQueries().build();
        db = dataBase.getUserDao();

        // Initializam componentele din formular
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Verifcam daca exista un utilizator cu emailul si parola introduse
                User user = db.getUser(email, password);

                if (user != null) {
                    Intent i = new Intent(LogInActivity.this, AppMainPageActivity.class);
                    i.putExtra("User", user);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("LOGIN", email);
                    editor.apply();

                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LogInActivity.this, "Wrong email or password! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void DoGoBack(View view) {
        finish();
    }
}