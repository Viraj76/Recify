package com.example.recify.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.recify.classes.Meal
import com.example.recify.databinding.FavItemViewBinding

class MealsAdapter: RecyclerView.Adapter<MealsAdapter.FavoriteMealViewHolder>(){

    // we will use diffUtils to enhance the RV
    //if we delete a item (notify data) then it refresh all the items
    //but when we use the diffUtils then it only refresh the item that got changed not the others

    inner class FavoriteMealViewHolder( val binding: FavItemViewBinding):ViewHolder(binding.root)


    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealViewHolder {
        return FavoriteMealViewHolder(
            FavItemViewBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.ivFavorite)
        holder.binding.tvFavItemTitle.text = meal.strMeal

    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
}