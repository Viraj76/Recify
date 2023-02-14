package com.example.recify.Fragments
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recify.classes.Meal
import com.example.recify.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.example.recify.Adapter.CategoriesAdapter
import com.example.recify.Adapter.MostPopularAdapter
import com.example.recify.Fragments.bottomsheet.MealBottomSheetFragment
import com.example.recify.R
import com.example.recify.actvity.MainActivity

import com.example.recify.actvity.MealActivity
import com.example.recify.classes.MealsByCategory
import com.example.recify.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel :HomeViewModel
    lateinit var randomMeal: Meal
    lateinit var popularItemsAdapter : MostPopularAdapter
    lateinit var categoriesAdapter: CategoriesAdapter
    companion object{
        const val MEAL_ID = "com.example.recify.Fragments.idMeal"
        const val MEAL_NAME = "com.example.recify.Fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recify.Fragments.thumpMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java] // we are removing it from here to MainActivity becz we dont want to initialize 3 instance for the
        // for the viewModel instead we will initialize it into the main act. and then we will use it for all the frags
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
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
        viewModel.randomMeal()
        observeRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

//        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeGetCategoriesLiveData()

        onPopularItemLongClick()

        onSearchIconClick()


    }

    private fun onSearchIconClick() {
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = {meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

//    private fun prepareCategoriesRecyclerView() {
//
//        binding.rvViewCategories.apply {
//            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
//            adapter = categoriesAdapter
//        }
//    }

    private fun observeGetCategoriesLiveData() {
        viewModel.observeGetCategoriesLiveData().observe(viewLifecycleOwner){ categories ->

                categoriesAdapter.setCategoryList(categories)

        }
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
           layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
           adapter = popularItemsAdapter
       }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemLiveData().observe(viewLifecycleOwner){ mealList->
            popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)
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
       viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner){ meal->
           if (meal != null) {
               Glide.with(this@HomeFragment)
                   .load(meal.strMealThumb)
                   .into(binding.ivRandomMeal)
               this.randomMeal = meal
           }

       }
    }

}