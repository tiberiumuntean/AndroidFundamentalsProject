package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.calorietracker.room.User;
import com.example.calorietracker.room.UserDao;
import com.example.calorietracker.room.UserDataBase;

public class FinalStepsSignUpActivity extends AppCompatActivity {

    private UserDao userDao;
    private Button buttonSignup;
    private EditText ageEditText, heightEditText, weightEditText, genderEditText;
    private RadioButton maleRadio, femaleRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_steps_sign_up);

        userDao = Room.databaseBuilder(this, UserDataBase.class, "user2-database.db").allowMainThreadQueries()
                .build().getUserDao();

        buttonSignup = findViewById(R.id.button_signup);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("REGISTER_USERNAME");
        String email = intent.getStringExtra("REGISTER_EMAIL");
        String password = intent.getStringExtra("REGISTER_PASSWORD");

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageEditText = (EditText) findViewById(R.id.user_age);
                int ageValue = Integer.parseInt(ageEditText.getText().toString());

                heightEditText = (EditText) findViewById(R.id.user_height);
                double heightValue = Integer.parseInt(heightEditText.getText().toString());

                weightEditText = (EditText) findViewById(R.id.user_weight);
                double weightValue = Integer.parseInt(weightEditText.getText().toString());

                Boolean gender;

                maleRadio = (RadioButton) findViewById(R.id.radio_male);
                femaleRadio = (RadioButton) findViewById(R.id.radio_female);

                if(maleRadio.isChecked()){
                    gender = true;
                } else {
                    gender = false;
                }

                User user = new User(userName, password, email, ageValue, heightValue, weightValue, gender);
                userDao.insert(user);

                Intent moveToLogin = new Intent(FinalStepsSignUpActivity.this, LogInActivity.class);
                startActivity(moveToLogin);
            }
        });

    }
}