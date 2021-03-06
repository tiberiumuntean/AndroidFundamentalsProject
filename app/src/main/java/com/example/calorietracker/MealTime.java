package com.example.calorietracker;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MealTime {
    private String itemTitle;
    private ArrayList<FoodItem> subItemlist = new ArrayList<>();

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public ArrayList<FoodItem> getSubItemlist() {
        return subItemlist;
    }

    public void setSubItemlist(ArrayList<FoodItem> subItemlist) {
        this.subItemlist = subItemlist;
    }

    public MealTime(String itemTitle, ArrayList<FoodItem> subItemlist) {
        this.itemTitle = itemTitle;
        this.subItemlist = subItemlist;
    }

    public MealTime(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void addSubItem(Food food, int cantity) {
        FoodItem newFoodItem = new FoodItem(food, cantity);
        subItemlist.add(newFoodItem);
    }

    public void addSubItem(FoodItem foodItem) {
        subItemlist.add(foodItem);
    }
}
