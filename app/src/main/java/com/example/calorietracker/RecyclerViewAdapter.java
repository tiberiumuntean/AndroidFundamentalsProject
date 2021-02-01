package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//AVEM NEVOIE DE UN SCHELET DE METODE - ALT+ENTER
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.mViewHolder> {
    private ArrayList<MealTime> mDataSet;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool(); // Pentru a integra un RecyclerView in alt RecyclerView

    public RecyclerViewAdapter(ArrayList<MealTime> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // Se creeaza View Holeder-ul
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false); // Reprezentarea in java a layoutului item_recyclerview
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) { // Se modifica continutul
        String title = mDataSet.get(position).getItemTitle();
        holder.update(title); // facem un update prin care se afisaeza datele pe ecran

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.rvSubItem.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(mDataSet.get(position).getSubItemlist().size());

        FoodListRecyclerViewAdapter subItemAdapter = new FoodListRecyclerViewAdapter((ArrayList<FoodItem>) mDataSet.get(position).getSubItemlist());

        holder.rvSubItem.setLayoutManager(layoutManager);
        holder.rvSubItem.setAdapter(subItemAdapter);
        holder.rvSubItem.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size(); //din adapter spre RV am expus dimensiunea datelor (cate avem)
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private RecyclerView rvSubItem;
        private Button addFoodItemBtn;

        public mViewHolder(@NonNull View itemView) {
            super(itemView); // luam o referinta din resurse spre obiectul mTitle
            mTitle = itemView.findViewById(R.id.item_list_textview_title); // cautam doar in interiorul itemView
            rvSubItem = itemView.findViewById(R.id.food_item_rv);
            addFoodItemBtn = itemView.findViewById(R.id.add_food_item_button);

            addFoodItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cand dam click pe butonul "Add Food" trimitem spre urmatorul activity momentul zilei pentru care va fi adaugat alimentul dorit
                    Intent intent = new Intent(v.getContext(), SelectFoodActivity.class); // prin terminatia .class ii trimitem tipul de activity care trebuie instantiat
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("CHOSEN_MEAL_TIME", mTitle.getText().toString());
                    editor.apply();

                    v.getContext().startActivity(intent);
                }
            });
        }

        public void update(String title) {
            mTitle.setText(title);
        }
    }
}
