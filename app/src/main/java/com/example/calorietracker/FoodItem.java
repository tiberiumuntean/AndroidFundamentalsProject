package com.example.calorietracker;

public class FoodItem {
    private Food mFoodItem;
    private double mCantity;

    public FoodItem(Food mFoodItem, double mCantity) {
        this.mFoodItem = mFoodItem;
        this.mCantity = mCantity;
    }

    public Food getmFoodItem() {
        return mFoodItem;
    }

    public void setmFoodItem(Food mFoodItem) {
        this.mFoodItem = mFoodItem;
    }

    public double getmCantity() {
        return mCantity;
    }

    public void setmCantity(double mCantity) {
        this.mCantity = mCantity;
    }
}
