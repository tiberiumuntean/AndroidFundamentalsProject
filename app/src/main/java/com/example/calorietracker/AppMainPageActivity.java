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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AppMainPageActivity extends AppCompatActivity {

    private ArrayList<MealTime> mDataSource = new ArrayList<>();        // Data source pentru obiectele de tip MealTime (Breakfast, Lunch, Dinner)
    private LinearLayoutManager mListLayoutManager;                     // Definim Layout Manager
    private RecyclerView mList;                                         // RecyclerView pentru mDataSource
    private RecyclerViewAdapter mAdapter;                               // RecycleViewAdapter pentru mDataSource

    private ProgressBar progressBar;                                    // ProgressBar pentru numarul de calorii
    private TextView progressBarText;                                   // Textul din ProgressBar pentru numarul de calorii

    private User loggedInUser;                                          // Variabila pentru a stoca utilizatorul conectat din DB
    private UserDao userDao;
    private UserDataBase dataBase;

    private int totalCalories;                                          // Variabila pentru calculul caloriilor sugerate
    private double consumedCalories;                                    // Variabila pentru calculul caloriilor consumate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_mainpage);

        // Initializam SharedPreferences pentru state (Pastreaza mail-ul utilizatorului conectat)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String loggedInUserEmail = preferences.getString("LOGIN", "");
        SharedPreferences.Editor editor = preferences.edit(); // Initializam un editor pentru a putea insera variabile in SharedPreferences

        // Initializam DB
        dataBase = Room.databaseBuilder(this, UserDataBase.class, "user2-database.db").allowMainThreadQueries().build();
        userDao = dataBase.getUserDao();
        loggedInUser = userDao.getUserByEmail(loggedInUserEmail); /// Luam utilizatorul conectat din baza de date pe baza email-ului stocat in SharedPreferences

        // Initializam ProgressBar-ul (ProgressBar si ProgressBarText)
        progressBarText = (TextView) findViewById(R.id.calories_start);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Calculul caloriilor total in functie de sex
        if (loggedInUser.getGender() == true) {
            totalCalories = (int) (1.55 * (10 * loggedInUser.getWeight() + 6.25 * loggedInUser.getHeight() - 5 * loggedInUser.getAge() + 5));
        } else {
            totalCalories = (int) (1.55 * (10 * loggedInUser.getWeight() + 6.25 * loggedInUser.getHeight() - 5 * loggedInUser.getAge() - 161));
        }

        // Setam numarul caloriilor totale in ProgressBar
        progressBarText.setText(consumedCalories + " / " + totalCalories);

        // Declaram cele 3 perioade ale zilei
        MealTime breakfast = new MealTime("Breakfast");
        MealTime lunch = new MealTime("Lunch");
        MealTime dinner = new MealTime("Dinner");

        // Adaugam in dataSource cele 3 perioade declarate
        mDataSource.add(breakfast);
        mDataSource.add(lunch);
        mDataSource.add(dinner);

        // Trimitem dataSource-ul catre RecyclerView
        mListLayoutManager = new LinearLayoutManager(this);
        mList = findViewById(R.id.activity_appMainPageRecycler);
        mAdapter = new RecyclerViewAdapter(mDataSource);
        mList.setLayoutManager(mListLayoutManager);
        mList.setAdapter(mAdapter);

        addNewItemToDataSource(preferences, editor, breakfast, lunch, dinner);
    }

    // Metoda pentru adaugarea unui nou aliment din Dialog
    private void addNewItemToDataSource(SharedPreferences preferences, SharedPreferences.Editor editor, MealTime breakfast, MealTime lunch, MealTime dinner) {
        Intent intent = getIntent();

        String foodName = intent.getStringExtra("FOOD_NAME");
        String foodCty = intent.getStringExtra("FOOD_CANTITY");
        String foodCalories = intent.getStringExtra("FOOD_CALORIES");

        if (foodCalories != null) {
            Food newFood = new Food(foodName, Double.parseDouble(foodCalories));
            FoodItem newFoodItem = new FoodItem(newFood, Double.parseDouble(foodCty));

            String chosenMealTime = preferences.getString("CHOSEN_MEAL_TIME", "");

            // In functie de momentul zilei ales, adaugam alimentul in categoria corespunzatoare
            if (!chosenMealTime.equalsIgnoreCase("")) {
                if (chosenMealTime == "Breakfast") {
                    breakfast.addSubItem(newFoodItem);
                } else if (chosenMealTime == "Lunch") {
                    lunch.addSubItem(newFoodItem);
                } else if (chosenMealTime == "Dinner") {
                    dinner.addSubItem(newFoodItem);
                }

                // Actualizam numarul de calorii consumate;
                consumedCalories = newFoodItem.getmFoodItem().getCalories();
                progressBarText.setText(consumedCalories + " / " + totalCalories);
                progressBar.setProgress((int) consumedCalories * 100 / totalCalories);
            }
        }
    }

    public void doLogout(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("LOGIN", "");
        editor.apply();

        Intent intent = new Intent(AppMainPageActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
