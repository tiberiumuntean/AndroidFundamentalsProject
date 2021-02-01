package com.example.calorietracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodListRecyclerViewAdapter extends RecyclerView.Adapter<FoodListRecyclerViewAdapter.mViewHolder> {
    private ArrayList<FoodItem> mDataSet;
    private LayoutInflater mLayoutInflater; // definim inflatterul pentru a lua chestii din xml

    public FoodListRecyclerViewAdapter(ArrayList<FoodItem> dataset) {
        mDataSet = dataset;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sublist_recyclerview, parent, false); // Reprezentarea in java a layoutului item_recyclerview
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        String name = mDataSet.get(position).getmFoodItem().getName();
        double calories = mDataSet.get(position).getmFoodItem().getCalories();
        double cantity = mDataSet.get(position).getmCantity();
        holder.update(name, calories, cantity); // facem un update prin care se afisaeza datele pe ecran
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView, caloriesView, cantityView;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.item_sublist_list_textview_title); // cautam doar in interiorul itemView
            caloriesView = itemView.findViewById(R.id.item_sublist_list_textview_calories); // cautam doar in interiorul itemView
            cantityView = itemView.findViewById(R.id.item_sublist_list_textview_cantity); // cautam doar in interiorul itemView
        }

        public void update(String name, double calories, double cantity) {
            nameView.setText(name);
            caloriesView.setText(calories + " kcal");
            cantityView.setText(cantity + " g");
        }
    }


}
