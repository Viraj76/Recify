package com.example.recify.viewModel

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recify.Retrofit.RetrofitInstance
import com.example.recify.classes.*
import com.example.recify.db.MealDataBase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel(
    private val mealDataBase: MealDataBase  //Here we will take the data from our modelViewFactory
): ViewModel(

) {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<MealsByCategory>>()
    private var cateGoriesLiveData = MutableLiveData<List<Category>>()
    private var favouriteMealsLiveData = mealDataBase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealLiveData = MutableLiveData<List<Meal>>()

    /*
    init {
        randomMeal()   //even the activity recreated we have same data as we had ( we did this to keep same data on screen after rotating the screen)

        // but this not a good way to do that instead we have different approach to do that

    }
*/

    private var saveSateRandomMeal:Meal?=null


     fun randomMeal(){
         saveSateRandomMeal?.let { randomMeal->
             randomMealLiveData.postValue(randomMeal)
             return
         }
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveSateRandomMeal = randomMeal
                }
                else{
                    return
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null){
                    popularItemLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())

            }

        })
    }
    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
               response.body()?.let { categoryList ->
                   cateGoriesLiveData.postValue(categoryList.categories)
               }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViwModel",t.message.toString())
            }

        })
    }
    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }

        })
    }
    fun searchMeals(searchQuery:String){
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object:Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchedMealLiveData.postValue(it)
                }
            }
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                var tv: TextView
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }
    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDataBase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDataBase.mealDao().upsert(meal)
        }
    }


    fun observeSearchedMealsLiveData():LiveData<List<Meal>>{
        return searchedMealLiveData
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }
    fun observePopularItemLiveData():LiveData<List<MealsByCategory>>{
        return popularItemLiveData
    }
    fun observeGetCategoriesLiveData():LiveData<List<Category>>{
        return cateGoriesLiveData
    }
    fun observeFavouriteMealsLiveData() : LiveData<List<Meal>>{
        return favouriteMealsLiveData
    }
    fun observeBottomSheetMeal(): LiveData<Meal> = bottomSheetMealLiveData
}





