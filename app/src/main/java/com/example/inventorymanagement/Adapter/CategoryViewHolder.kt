package com.example.inventorymanagement.Adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventorymanagement.HelperClass.Category
import com.example.inventorymanagement.databinding.CategoryItemBinding

class CategoryViewHolder (val binding: CategoryItemBinding, val  context : Context): RecyclerView.ViewHolder(binding.root) {
    fun bind(category : Category){
        binding.categoryName.text = category.name
        Glide.with(context).load(category.image).into(binding.categoryImg)
    }
}