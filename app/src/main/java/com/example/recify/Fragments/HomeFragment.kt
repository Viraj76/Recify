package com.example.recify.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recify.R
import com.example.recify.Retrofit.RetrofitInstance
import com.example.recify.classes.Meal
import com.example.recify.classes.MealList
import com.example.recify.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import com.bumptech.glide.Glide
import com.example.recify.viewModel.HomeViewModel
import retrofit2.Response
import retrofit2.Retrofit


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm :HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeMvvm.randomMeal()
        observeRandomMeal()
    }

    private fun observeRandomMeal() {
       homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner){ t ->
           if (t != null) {
               Glide.with(this@HomeFragment)
                   .load(t.strMealThumb)
                   .into(binding.ivRandomMeal)
           }
       }
    }

}