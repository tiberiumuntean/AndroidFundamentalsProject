package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.calorietracker.room.User;
import com.example.calorietracker.room.UserDao;
import com.example.calorietracker.room.UserDataBase;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextCnfPassword;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                Intent moveToRegister = new Intent(SignUpActivity.this, FinalStepsSignUpActivity.class);
                moveToRegister.putExtra("REGISTER_EMAIL", email);
                moveToRegister.putExtra("REGISTER_USERNAME", userName);
                moveToRegister.putExtra("REGISTER_PASSWORD", password);

                startActivity(moveToRegister);
            }
        });
    }

    // back pentru imaginea cu sagetuta
    public void doGoBack(View view) {
        finish();
    }

}