package com.example.calorietracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SelectFoodRVAdapter extends RecyclerView.Adapter<SelectFoodRVAdapter.mViewHolder> {

    private ArrayList<Food> mDataSet;
    private LayoutInflater mLayoutInflater; // definim inflatterul pentru a lua chestii din xml
    /////////////////Dialog/////////
    private EditText mCantity;
    private double mCantityValue;
    private Food selectedFood;

    public SelectFoodRVAdapter(ArrayList<Food> dataset) {
        mDataSet = dataset;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sublist_recyclerview, parent, false); // Reprezentarea in java a layoutului item_recyclerview
        ///////////////////////////////////////////
        AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
        final EditText text = new EditText(parent.getContext());

        builder.setTitle("Cantity").setMessage("Insert Cantity").setView(text);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
                final String cantityString = text.getText().toString();
                Intent intent = new Intent(parent.getContext(), AppMainPageActivity.class);

                FoodItem fi = new FoodItem(selectedFood, Double.parseDouble(cantityString));
                intent.putExtra("FOOD_NAME", (String) fi.getmFoodItem().getName());
                intent.putExtra("FOOD_CALORIES", String.valueOf(fi.getmFoodItem().getCalories()));
                intent.putExtra("FOOD_CANTITY", String.valueOf(fi.getmCantity()));

                parent.getContext().startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface di, int i) {
            }
        });

        //////////////////////////////////////////

        mViewHolder viewHolder = new mViewHolder(view);

        viewHolder.item_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView foodNameTV = (TextView) v.findViewById(R.id.item_sublist_list_textview_title);
                TextView foodCaloriesTV = (TextView) v.findViewById(R.id.item_sublist_list_textview_calories);
                String foodName = foodNameTV.getText().toString();
                String[] foodCalories = foodCaloriesTV.getText().toString().split(" ");

                selectedFood = new Food(1, foodName, Double.parseDouble(foodCalories[0]));

                builder.create().show();
            }
        });

        return new SelectFoodRVAdapter.mViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        String name = mDataSet.get(position).getName();
        double calories = mDataSet.get(position).getCalories();
        double cantity = 100;
        holder.update(name, calories, cantity); // facem un update prin care se afisaeza datele pe ecran
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView, caloriesView, cantityView;
        ///////////////////////DIALOG//////////////////////////////
        private LinearLayout item_food;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.item_sublist_list_textview_title); // cautam doar in interiorul itemView
            caloriesView = itemView.findViewById(R.id.item_sublist_list_textview_calories); // cautam doar in interiorul itemView
            cantityView = itemView.findViewById(R.id.item_sublist_list_textview_cantity); // cautam doar in interiorul itemView

            item_food = (LinearLayout) itemView.findViewById(R.id.food_item);
        }

        public void update(String name, double calories, double cantity) {
            nameView.setText(name);
            caloriesView.setText(calories + " kcal");
            cantityView.setText(cantity + " g");
        }

    }


}
