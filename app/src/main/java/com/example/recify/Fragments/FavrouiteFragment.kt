package com.example.recify.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recify.Adapter.MealsAdapter
import com.example.recify.actvity.MainActivity
import com.example.recify.databinding.FragmentFavrouiteBinding
import com.example.recify.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavrouiteFragment : Fragment() {
    private lateinit var  binding: FragmentFavrouiteBinding
    private lateinit var viewModel: HomeViewModel
    private  lateinit var mealsAdapter: MealsAdapter

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

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(mealsAdapter.differ.currentList[position])

                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(mealsAdapter.differ.currentList[position])
                    }
                ).show()

            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorite)
    }

    private fun prepareRecyclerView() {
        mealsAdapter = MealsAdapter()
        binding.rvFavorite.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = mealsAdapter
        }
    }

    private fun observeFavourite() {
        viewModel.observeFavouriteMealsLiveData().observe(viewLifecycleOwner){ meals ->
            mealsAdapter.differ.submitList(meals)
        }
    }


}