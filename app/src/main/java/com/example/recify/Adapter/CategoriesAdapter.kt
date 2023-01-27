package com.example.recify.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.recify.classes.Category
import com.example.recify.classes.CategoryList
import com.example.recify.databinding.CategoryItemBinding

class CategoriesAdapter :RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categoriesList  = ArrayList<Category>()
    fun setCategoryList(categoriesList: List<Category>){
        this.categoriesList = categoriesList as ArrayList<Category>
    }
    // do binding as val (imP)
    inner class CategoryViewHolder(val binding:CategoryItemBinding):ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoriesList[position].strCategoryThumb).into(holder.binding.ivCategory)
        holder.binding.tvCatName.text = categoriesList[position].strCategory
    }

    override fun getItemCount(): Int {
       return categoriesList.size
    }
}