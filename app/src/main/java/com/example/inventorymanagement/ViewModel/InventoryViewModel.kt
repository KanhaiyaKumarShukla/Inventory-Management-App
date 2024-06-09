package com.example.inventorymanagement.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.inventorymanagement.helperClass.Category
import com.example.inventorymanagement.helperClass.Stock
import com.example.inventorymanagement.helperClass.inventory
import com.example.inventorymanagement.Repository.InventoryRepository
import com.example.inventorymanagement.helperClass.profit_loss

class InventoryViewModel : ViewModel() {
    private val repository = InventoryRepository

    val defaultImage:String? =repository.defaultImage


    fun addInventoryTransectionHistory(inventory: inventory){
        repository.addInventoryTransectionHistory(inventory)
    }
    fun deleteCategory(category: String) {
        repository.deleteCategory(category)
    }
    fun deleteStocks(category: String, stocksId:String) {
        repository.deleteStocks(category, stocksId)
    }
    fun fetchCategoryKey(category: String) : String?{
        return repository.fetchCategoryKey(category)
    }
    fun sellStock(quantity: Int, price: Double,category: String, stockId: String){
        return repository.sellStock(quantity,price,category, stockId)
    }
    fun updateStock(
        name: String = "",
        image: String? = null,
        quantity: Int=0,
        price: Double=0.0,
        category: String,
        stockId: String
    ) {
        return repository.updateStocks(name, image, quantity,price,category, stockId)
    }

    fun buyStock(quantity: Int, price: Double, category: String, stockId: String){
        return repository.buyStock(quantity, price, category, stockId)
    }
    fun addCostPriceSellingPrice(profitLoss: profit_loss){
        return InventoryRepository.addCostPriceSellingPrice(profitLoss)
    }

}