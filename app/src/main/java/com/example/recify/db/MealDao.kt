package com.example.recify.db

import androidx.core.view.WindowInsetsCompat.Type.InsetsType
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recify.classes.Meal
import com.example.recify.classes.MealList

@Dao
interface MealDao {
    // one way to this
    /*
    @Insert
    suspend fun insertMeal(meal: Meal)
    @Update
    suspend fun updateMeal(meal: Meal)
    */

    //Other way
    @Insert(onConflict = OnConflictStrategy.REPLACE)  //if the data already exists then it will update id
    suspend fun upsert(meal:Meal)

    @Delete
    suspend fun delete(meal:Meal)

    @Query("SELECT * FROM mealInfo")
    fun getAllMeals() : LiveData<List<Meal>>


}