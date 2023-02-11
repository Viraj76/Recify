package com.example.recify.Retrofit

import com.example.recify.classes.CategoryList
import com.example.recify.classes.MealsByCategoryList
import com.example.recify.classes.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface MealApi {
    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php") //here ? is not required because we have only one query so remove it
    fun getMealDetail(@Query("i") id :String):Call<MealList>
//
    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName:String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>
}