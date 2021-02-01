package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.calorietracker.room.User;
import com.example.calorietracker.room.UserDao;
import com.example.calorietracker.room.UserDataBase;

public class FinalStepsSignUpActivity extends AppCompatActivity {
    private UserDao userDao;

    private Button buttonSignup;
    private EditText ageEditText, heightEditText, weightEditText;
    private RadioButton maleRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_steps_sign_up);

        // Initializam DB
        userDao = Room.databaseBuilder(this, UserDataBase.class, "user2-database.db").allowMainThreadQueries().build().getUserDao();

        // Initializam butonul de signup
        buttonSignup = findViewById(R.id.button_signup);

        // Luam datele din activitatea anterioara
        Intent intent = getIntent();
        String userName = intent.getStringExtra("REGISTER_USERNAME");
        String email = intent.getStringExtra("REGISTER_EMAIL");
        String password = intent.getStringExtra("REGISTER_PASSWORD");

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean gender;
                int ageValue = 0, heightValue = 0, weightValue = 0;

                // Preluam datele din input-uri
                ageEditText = (EditText) findViewById(R.id.user_age);
                if (!ageEditText.getText().toString().isEmpty()) {
                    ageValue = Integer.parseInt(ageEditText.getText().toString());
                }

                heightEditText = (EditText) findViewById(R.id.user_height);
                if (!heightEditText.getText().toString().isEmpty()) {
                    heightValue = Integer.parseInt(heightEditText.getText().toString());
                }

                weightEditText = (EditText) findViewById(R.id.user_weight);
                if (!weightEditText.getText().toString().isEmpty()) {
                    weightValue = Integer.parseInt(weightEditText.getText().toString());
                }

                maleRadio = (RadioButton) findViewById(R.id.radio_male);

                if (maleRadio.isChecked()) {
                    gender = true;
                } else {
                    gender = false;
                }

                if (!ageEditText.getText().toString().isEmpty() && !heightEditText.getText().toString().isEmpty() && !weightEditText.getText().toString().isEmpty()) {
                    // Construim un nou obiect de tip User cu ajutorul datelor din formular
                    User user = new User(userName, password, email, ageValue, heightValue, weightValue, gender);

                    // Inseram noul user in DB
                    userDao.insert(user);

                    // Ne intoarcem inapoi la login
                    Intent moveToLogin = new Intent(FinalStepsSignUpActivity.this, LogInActivity.class);
                    startActivity(moveToLogin);
                } else {
                    Toast.makeText(v.getContext(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}