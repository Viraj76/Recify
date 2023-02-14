package com.example.recify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recify.Adapter.MealsAdapter
import com.example.recify.R
import com.example.recify.actvity.MainActivity
import com.example.recify.databinding.FragmentSearchBinding
import com.example.recify.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel : HomeViewModel
    private  lateinit var searchRecylerViewAdapter:MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        binding.ivArrow.setOnClickListener { searchMeals() }

        observeSearchedMealsLiveData()

        //Creating coroutines to search without the clicking on the arrow
        var searchJob : Job? = null
        binding.etSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500) // it will take 500ms after entering some text then it will work
                viewModel.searchMeals(searchQuery.toString())
            }
        }


    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner){mealsList ->
            searchRecylerViewAdapter.differ.submitList(mealsList)
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.etSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecylerViewAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply{
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = searchRecylerViewAdapter
        }
    }


}