package com.example.recify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recify.R
import com.example.recify.Retrofit.RetrofitInstance
import com.example.recify.classes.Meal
import com.example.recify.classes.MealList
import com.example.recify.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import com.bumptech.glide.Glide
import retrofit2.Response
import retrofit2.Retrofit


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal :Meal = response.body()!!.meals[0]
                    Glide.with(this@HomeFragment).load(randomMeal.strMealThumb).into(binding.ivRandomMeal)

                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}