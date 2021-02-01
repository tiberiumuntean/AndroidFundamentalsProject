package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;

public class SelectFoodActivity extends AppCompatActivity{

    private ArrayList<Food>foodList;
    public static final String getFoodItemMessage = "GET_FOOD_ITEM" ;
    private LinearLayoutManager mListLayoutManager;  // Definim Layout Manager
    private RecyclerView mList;
    private SelectFoodRVAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        Intent intent = getIntent();
        String chosenMealTime = intent.getStringExtra("CHOSEN_MEAL_TIME");

        Food mChicken = new Food(1,"chicken",230);
        Food mPork = new Food(2,"Pork",250);
        Food mApple = new Food(3,"Apple", 80);

        foodList = new ArrayList<>();
        foodList.add(mChicken);
        foodList.add(mPork);
        foodList.add(mApple);

        mListLayoutManager = new LinearLayoutManager(this);
        mList = findViewById(R.id.food_list_rv);
        mAdapter = new SelectFoodRVAdapter(foodList);

        mList.setLayoutManager(mListLayoutManager);
        mList.setAdapter(mAdapter);
    }
}