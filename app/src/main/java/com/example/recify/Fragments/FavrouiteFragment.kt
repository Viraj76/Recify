package com.example.recify.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recify.Adapter.FavoriteMealsAdapter
import com.example.recify.R
import com.example.recify.actvity.MainActivity
import com.example.recify.databinding.FragmentFavrouiteBinding
import com.example.recify.viewModel.HomeViewModel

class FavrouiteFragment : Fragment() {
    private lateinit var  binding: FragmentFavrouiteBinding
    private lateinit var viewModel: HomeViewModel
    private  lateinit var favoriteMealsAdapter: FavoriteMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavrouiteBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavourite()

    }

    private fun prepareRecyclerView() {
        favoriteMealsAdapter = FavoriteMealsAdapter()
        binding.rvFavorite.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favoriteMealsAdapter
        }
    }

    private fun observeFavourite() {
        viewModel.observeFavouriteMealsLiveData().observe(viewLifecycleOwner){ meals ->
            favoriteMealsAdapter.differ.submitList(meals)
        }
    }


}