package com.example.recify.Retrofit

import com.example.recify.classes.Meal
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.stream.DoubleStream.builder

object RetrofitInstance {
//    lateinit var mealApi :MealApi
//    init {
//
//    }

    val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().
            create(MealApi::class.java)
    }
}