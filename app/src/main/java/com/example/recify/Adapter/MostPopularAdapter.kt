package com.example.recify.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recify.classes.CategoryMeals
import com.example.recify.classes.MealList
import com.example.recify.databinding.ActivityMealBinding
import com.example.recify.databinding.PopularItemsBinding


class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    private var mealList = ArrayList<CategoryMeals>()
    lateinit var onItemClick : ((CategoryMeals) -> Unit)

    fun setMeals(mealList: ArrayList<CategoryMeals>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    class  PopularMealViewHolder( val binding: PopularItemsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.ivPopularItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

}