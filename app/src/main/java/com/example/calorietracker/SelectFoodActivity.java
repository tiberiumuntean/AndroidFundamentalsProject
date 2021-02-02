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

    private LinearLayoutManager mListLayoutManager;  // Definim Layout Manager
    private RecyclerView mList;
    private SelectFoodRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);

        Food mChicken = new Food("Chicken",115);
        Food mPork = new Food("Pork",125);
        Food mApple = new Food("Apple", 70);

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