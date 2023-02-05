package com.example.recify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recify.Adapter.CategoriesAdapter
import com.example.recify.R
import com.example.recify.actvity.MainActivity
import com.example.recify.databinding.FragmentCategoriesBinding
import com.example.recify.viewModel.HomeViewModel


class CategoriesFragment : Fragment() {
    private lateinit var binding : FragmentCategoriesBinding
    private  lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var  viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentCategoriesBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
    }

    private fun observeCategories() {
        viewModel.observeGetCategoriesLiveData().observe(viewLifecycleOwner){ categories ->
            categoriesAdapter.setCategoryList(categories)
        }
    }
    private fun prepareRecyclerView() {
       categoriesAdapter = CategoriesAdapter()
        binding.rvViewCategories.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }


}