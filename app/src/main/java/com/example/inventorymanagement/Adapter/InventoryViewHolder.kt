package com.example.inventorymanagement.Adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inventorymanagement.HelperClass.Stock
import com.example.inventorymanagement.HelperClass.inventory
import com.example.inventorymanagement.databinding.InventoryItemBinding
import com.example.inventorymanagement.databinding.StockItemBinding

class InventoryViewHolder(val binding: InventoryItemBinding, val  context : Context) : RecyclerView.ViewHolder(binding.root) {

    fun bind(inventory: inventory){
        binding.stockName.text=inventory.stockName
        binding.price.text = inventory.price.toString()
        binding.quantity.text = inventory.quantity.toString()
        if(!inventory.buyersName.isNullOrEmpty()) {
            binding.buyersName.text = inventory.buyersName
            binding.buyersName.visibility= View.VISIBLE
        }else{
            binding.buyersName.visibility= View.INVISIBLE
        }
        if(!inventory.image.isNullOrEmpty())Glide.with(context).load(inventory.image).into(binding.pickedImg)
    }
}