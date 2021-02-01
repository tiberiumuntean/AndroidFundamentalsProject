package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.calorietracker.room.User;
import com.example.calorietracker.room.UserDao;
import com.example.calorietracker.room.UserDataBase;

import java.util.ArrayList;

public class AppMainPageActivity extends AppCompatActivity {

    private ArrayList<MealTime> mDataSource;  //  Cream un dataSouuce in care adaugam o lista cu elemente
    private LinearLayoutManager mListLayoutManager;  // Definim Layout Manager
    private RecyclerView mList;
    private RecyclerviewAdapter mAdapter;

    private ArrayList<Food> foodList;
    private Button mButtonFoodList;
    private ProgressBar progressBar;

    TextView progressBarText;
    User loggedInUser;
    UserDao userDao;
    UserDataBase dataBase;

    private int totalCalories;
    private double consumedCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_mainpage);

        // Luam progressbar-ul
        progressBarText = (TextView) findViewById(R.id.calories_start);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Luam user-ul conectat din sharedp
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedInUserEmail = preferences.getString("LOGIN", "");

        /////////////////////////////////
        // Initializam DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "user2-database.db")
                .allowMainThreadQueries()
                .build();
        userDao = dataBase.getUserDao();

        // Luam user-ul conectat din DB
        loggedInUser = userDao.getUserByEmail(loggedInUserEmail);

        //////////////
        // Setam caloriile
        if(loggedInUser.getGender() == true){ // baiat
            totalCalories = (int)(1.55*(10*loggedInUser.getWeight() + 6.25*loggedInUser.getHeight() - 5*loggedInUser.getAge()  + 5));
        } else {
            totalCalories = (int)(1.55*(10*loggedInUser.getWeight() + 6.25*loggedInUser.getHeight() - 5*loggedInUser.getAge()  - 161));
        }

        progressBarText.setText(consumedCalories + " / " + totalCalories);

        /////////////////////////////////////////////////////
        // Adaugam meals
        mDataSource = new ArrayList<>();
        MealTime breakfast = new MealTime("Breakfast");
        MealTime lunch = new MealTime("Lunch");
        MealTime dinner = new MealTime("Dinner");

        mDataSource.add(breakfast);
        mDataSource.add(lunch);
        mDataSource.add(dinner);

        ////////////////////////////////////////////////////

        //// POPULARE RECYCLER VIEW
        mListLayoutManager = new LinearLayoutManager(this);
        mList = findViewById(R.id.activity_appMainPageRecycler);
        mAdapter = new RecyclerviewAdapter(this, mDataSource);

        mList.setLayoutManager(mListLayoutManager);
        mList.setAdapter(mAdapter);

        /// PRELUARE ALIMENT ADAUGAT DIN ADD FOOD
        Intent intent = getIntent();

        String foodName = intent.getStringExtra("FOOD_NAME");
        String foodCty = intent.getStringExtra("FOOD_CANTITY");
        String foodCalories = intent.getStringExtra("FOOD_CALORIES");

        if (foodCalories != null) {
            Food f = new Food(2, foodName, Double.parseDouble(foodCalories));
            FoodItem fi = new FoodItem(f, Double.parseDouble(foodCty));

            String chosenMealTime = preferences.getString("CHOSEN_MEAL_TIME", "");

            if(!chosenMealTime.equalsIgnoreCase("")) {

                if (chosenMealTime == "Breakfast") {
                    breakfast.addButtonItem(fi);
                } else if (chosenMealTime == "Lunch") {
                    lunch.addButtonItem(fi);
                } else if (chosenMealTime == "Dinner") {
                    dinner.addButtonItem(fi);
                }

                consumedCalories = fi.getmFoodItem().getCalories() * fi.getmCantity()/100;
                progressBarText.setText(consumedCalories + " / " + totalCalories);
                progressBar.setProgress((int) consumedCalories*100/totalCalories);

            }
        }

    }

    public void doLogout(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LOGIN", "");
        editor.apply();

        Intent i = new Intent(AppMainPageActivity.this, MainActivity.class);
        startActivity(i);
    }
}
