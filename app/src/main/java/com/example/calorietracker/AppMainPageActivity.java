package com.example.calorietracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
    private double consumedCalories = 0;                                // Variabila pentru calculul caloriilor consumate

    // Declaram cele 3 perioade ale zilei
    private MealTime breakfast = new MealTime("Breakfast");
    private MealTime lunch = new MealTime("Lunch");
    private MealTime dinner = new MealTime("Dinner");

    private ArrayList<FoodItem> breakfastMeals = new ArrayList<>();
    private ArrayList<FoodItem> lunchMeals = new ArrayList<>();
    private ArrayList<FoodItem> dinnerMeals = new ArrayList<>();

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

        loadMealItems(preferences);
        addNewItemToDataSource(preferences, editor, breakfast, lunch, dinner);

        // Actualizam numarul de calorii consumate;
        for (int i = 0; i < breakfast.getSubItemlist().size(); i++) {
            consumedCalories += breakfast.getSubItemlist().get(i).getmFoodItem().getCalories();
        }
        for (int i = 0; i < lunch.getSubItemlist().size(); i++) {
            consumedCalories += lunch.getSubItemlist().get(i).getmFoodItem().getCalories();
        }
        for (int i = 0; i < dinner.getSubItemlist().size(); i++) {
            consumedCalories += dinner.getSubItemlist().get(i).getmFoodItem().getCalories();
        }

        progressBarText.setText(consumedCalories + " / " + totalCalories);
        progressBar.setProgress((int) consumedCalories * 100 / totalCalories);
    }

    private void loadMealItems(SharedPreferences preferences) {
        Gson gBreakfastItems = new Gson();
        Gson gLunchItems = new Gson();
        Gson gDinnerItems = new Gson();

        String jBreakfastItems = preferences.getString("FOOD_LIST_BREAKFAST", null);
        String jLunchItems = preferences.getString("FOOD_LIST_LUNCH", null);
        String jDinnerItems = preferences.getString("FOOD_LIST_DINNER", null);

        Type type = new TypeToken<ArrayList<FoodItem>>() {
        }.getType();

        breakfastMeals = gBreakfastItems.fromJson(jBreakfastItems, type);
        lunchMeals = gLunchItems.fromJson(jLunchItems, type);
        dinnerMeals = gDinnerItems.fromJson(jDinnerItems, type);

        updateMealArrays();
    }

    private void updateMealArrays() {
        if (breakfastMeals != null) {
            for (int i = 0; i < breakfastMeals.size(); i++) {
                breakfast.addSubItem(breakfastMeals.get(i));
            }
        } else {
            breakfastMeals = new ArrayList<>();
        }

        if (lunchMeals != null) {
            for (int i = 0; i < lunchMeals.size(); i++) {
                lunch.addSubItem(lunchMeals.get(i));
            }
        } else {
            lunchMeals = new ArrayList<>();
        }

        if (dinnerMeals != null) {
            for (int i = 0; i < dinnerMeals.size(); i++) {
                dinner.addSubItem(dinnerMeals.get(i));
            }
        } else {
            dinnerMeals = new ArrayList<>();
        }
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
            if (!chosenMealTime.isEmpty()) {
                if (chosenMealTime.equals("Breakfast")) {
                    breakfastMeals.add(newFoodItem);
                    breakfast.addSubItem(newFoodItem);
                } else if (chosenMealTime.equals("Lunch")) {
                    lunchMeals.add(newFoodItem);
                    lunch.addSubItem(newFoodItem);
                } else if (chosenMealTime.equals("Dinner")) {
                    dinnerMeals.add(newFoodItem);
                    dinner.addSubItem(newFoodItem);
                }

                saveMealItems(editor);
            }
        }
    }

    private void saveMealItems(SharedPreferences.Editor editor) {
        Gson gBreakfastItems = new Gson();
        Gson gLunchItems = new Gson();
        Gson gDinnerItems = new Gson();

        String jBreakfastItems = gBreakfastItems.toJson(breakfastMeals);
        String jLunchItems = gLunchItems.toJson(lunchMeals);
        String jDinnerItems = gDinnerItems.toJson(dinnerMeals);

        editor.putString("FOOD_LIST_BREAKFAST", jBreakfastItems);
        editor.putString("FOOD_LIST_LUNCH", jLunchItems);
        editor.putString("FOOD_LIST_DINNER", jDinnerItems);

        editor.apply();
    }

    public void doLogout(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("LOGIN", "");
        editor.putString("FOOD_LIST_BREAKFAST", "");
        editor.putString("FOOD_LIST_LUNCH", "");
        editor.putString("FOOD_LIST_DINNER", "");

        editor.apply();

        Intent intent = new Intent(AppMainPageActivity.this, MainActivity.class);
        startActivity(intent);
    }
}