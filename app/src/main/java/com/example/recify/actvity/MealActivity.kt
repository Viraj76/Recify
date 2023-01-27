package com.example.recify.actvity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recify.Fragments.HomeFragment
import com.example.recify.R
import com.example.recify.classes.Meal
import com.example.recify.databinding.ActivityMealBinding
import com.example.recify.db.MealDataBase
import com.example.recify.viewModel.MealViewModel
import com.example.recify.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    lateinit var binding: ActivityMealBinding
    lateinit var mealId : String
    lateinit var mealName : String
    lateinit var mealThumb : String
    lateinit var youtubeLink : String
    private lateinit var mealMvvm : MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDataBase = MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDataBase)

        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)          // here mealId will be initialized after the image set in the home fragment
        observerMealDetailsLiveData()

        onYtbeImageClicked()
        onFavClick()
    }

    private fun onFavClick() {
        binding.fabFav.setOnClickListener {
            saveMeal?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal Saved",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYtbeImageClicked() {
        binding.ivYtbe.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    var saveMeal: Meal?=null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this,object : Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase()
                binding.tvCategory.text = "Category : ${t!!.strCategory}"
                binding.tvArea.text = "Area : ${t.strArea}"
                binding.tvInstructionDetail.text = t.strInstructions
                saveMeal = t
                youtubeLink = t.strYoutube.toString()
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.ivMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.accent))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.progressbar.visibility=View.VISIBLE
        binding.fabFav.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvInstructionDetail.visibility = View.INVISIBLE
        binding.ivYtbe.visibility = View.INVISIBLE
    }
    private fun onResponseCase(){
        binding.progressbar.visibility=View.INVISIBLE
        binding.fabFav.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstructionDetail.visibility = View.VISIBLE
        binding.ivYtbe.visibility = View.VISIBLE
    }
}