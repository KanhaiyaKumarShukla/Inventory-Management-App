package com.example.inventorymanagement.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.inventorymanagement.HelperClass.Category
import com.example.inventorymanagement.HelperClass.Stock
import com.example.inventorymanagement.HelperClass.inventory
import com.example.inventorymanagement.Repository.InventoryRepository

class InventoryViewModel : ViewModel() {
    private val repository = InventoryRepository()

    val categories: LiveData<List<Category>> = repository.categories
    val stocks: LiveData<List<Stock>> = repository.stocks
    val inventoryHistory : LiveData<List<inventory>> = repository.inventoryHistory

    fun fetchStocks(categoryKey: String) {
        repository.fetchStock(categoryKey)
    }

    fun saveProductInDatabase(data:Stock, category: String, image_uri: Uri?) {
        repository.saveProductInDatabase(data, category, image_uri)
    }
    fun saveProductInRealTime(data: Stock, category: String){
        repository.saveProductInRealTime(data, category)
    }
    fun addInventoryTransectionHistory(inventory: inventory){
        repository.addInventoryTransectionHistory(inventory)
    }
    fun deleteCategory(category: String) {
        repository.deleteCategory(category)
    }
    fun deleteStocks(category: String, stocksId:String) {
        repository.deleteStocks(category, stocksId)
    }
}