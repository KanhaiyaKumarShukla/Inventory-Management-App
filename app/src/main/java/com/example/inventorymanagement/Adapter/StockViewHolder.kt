package com.example.inventorymanagement.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventorymanagement.helperClass.Stock
import com.example.inventorymanagement.R
import com.example.inventorymanagement.databinding.StockItemBinding

class StockViewHolder ( val binding: StockItemBinding, val  context : Context) : RecyclerView.ViewHolder(binding.root){


    fun bind(stock : Stock){
        binding.stockName.text = stock.name
        binding.price.text = stock.price.toString()
        binding.quantity.text = stock.quantity.toString()
        if(stock.image==null){
            binding.pickedImg.setImageResource(R.drawable.default_img)
        }
        else {
            Glide.with(context).load(stock.image).into(binding.pickedImg)
        }
    }
}