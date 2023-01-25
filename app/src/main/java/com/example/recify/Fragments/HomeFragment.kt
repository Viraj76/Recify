package com.example.recify.Fragments
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recify.R
import com.example.recify.Retrofit.RetrofitInstance
import com.example.recify.classes.Meal
import com.example.recify.classes.MealList
import com.example.recify.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import com.bumptech.glide.Glide
import com.example.recify.Adapter.MostPopularAdapter

import com.example.recify.actvity.MealActivity
import com.example.recify.classes.CategoryMeals
import com.example.recify.viewModel.HomeViewModel
import retrofit2.Response
import retrofit2.Retrofit
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm :HomeViewModel
    lateinit var randomMeal: Meal
    lateinit var popularItemsAdapter : MostPopularAdapter
    companion object{
        const val MEAL_ID = "com.example.recify.Fragments.idMeal"
        const val MEAL_NAME = "com.example.recify.Fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recify.Fragments.thumpMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemRecyclerView()
        homeMvvm.randomMeal()
        observeRandomMeal()
        onRandomMealClick()
        homeMvvm.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {meal ->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
       binding.rvPopularMeals.apply {
           layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
           adapter = popularItemsAdapter
       }
    }

    private fun observePopularItemsLiveData() {
        homeMvvm.observePopularItemLiveData().observe(viewLifecycleOwner){ mealList->
            popularItemsAdapter.setMeals(mealList as ArrayList<CategoryMeals>)
        }
    }

    private fun onRandomMealClick() {
        binding.ivRandomMeal.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
    private fun observeRandomMeal() {
       homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner){ meal->
           if (meal != null) {
               Glide.with(this@HomeFragment)
                   .load(meal.strMealThumb)
                   .into(binding.ivRandomMeal)
               this.randomMeal = meal
           }

       }
    }

}