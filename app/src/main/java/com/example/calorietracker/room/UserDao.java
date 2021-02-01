package com.example.calorietracker.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User where email= :mail and password= :password")
    User getUser(String mail, String password);

    @Query("SELECT * FROM User where email= :mail")
    User getUserByEmail(String mail);

    @Query("UPDATE User SET age = :age, height = :height, weight = :weight, gender = :gender WHERE email = :email")
    void updateUser(String email, int age, double height, double weight, Boolean gender);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}