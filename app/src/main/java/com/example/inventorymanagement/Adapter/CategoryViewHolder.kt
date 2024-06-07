package com.example.inventorymanagement.Adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventorymanagement.HelperClass.Category
import com.example.inventorymanagement.R
import com.example.inventorymanagement.databinding.CategoryItemBinding

class CategoryViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
    val categoryView=itemView.findViewById<TextView>(R.id.category_name)
    val categoryImage=itemView.findViewById<ImageView>(R.id.category_img)
    val view=itemView
}