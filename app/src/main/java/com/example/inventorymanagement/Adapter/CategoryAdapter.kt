package com.example.inventorymanagement.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventorymanagement.HelperClass.Category
import com.example.inventorymanagement.HelperClass.Stock
import com.example.inventorymanagement.databinding.CategoryItemBinding
import com.example.inventorymanagement.databinding.StockItemBinding

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.ViewHolder>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding,parent.context)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(val binding: CategoryItemBinding, val  context : Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(category : Category){
            binding.categoryName.text = category.name
            Glide.with(context).load(category.image).into(binding.categoryImg)
        }
    }

    class Diff : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }

}