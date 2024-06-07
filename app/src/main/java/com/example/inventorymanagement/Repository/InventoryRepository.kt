package com.example.inventorymanagement.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.inventorymanagement.HelperClass.AppConstants
import com.example.inventorymanagement.HelperClass.Category
import com.example.inventorymanagement.HelperClass.Stock
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class InventoryRepository {

    private val database = FirebaseDatabase.getInstance()
    private val dataRef = database.reference.child("Category")
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val key = firebaseAuth.currentUser!!.uid
    private val storageRef= Firebase.storage.getReference("Category")

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _stocks = MutableLiveData<List<Stock>>()
    val stocks: LiveData<List<Stock>> get() = _stocks
    val categoryKeyMap: MutableMap<String, String> = mutableMapOf()

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        dataRef.child(key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoriesList = mutableListOf<Category>()
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    category?.let { categoriesList.add(it) }
                    if (category != null) {
                        categoryKeyMap[category.name]=categorySnapshot.key!!
                        fetchStock(category.name)
                    }

                    Log.d("InventoryRepository", categoryKeyMap.toString())
                }
                _categories.value = categoriesList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error\
                Log.d("category", error.message)
            }
        })
    }
    fun fetchStock(category: String){
        val categoryKey=categoryKeyMap[category]
        if(categoryKey!=null){
            fetchStocksAtKey(categoryKey)
        }else{
            Log.d("InventoryRepository", "category key not found !")
        }
    }

    private fun fetchStocksAtKey(categoryKey: String) {
        Log.d("InventoryRepository", categoryKey)
            dataRef.child("$key/$categoryKey").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stocksList = mutableListOf<Stock>()
                for (stockSnapshot in snapshot.children) {
                    if (stockSnapshot.key == "name" || stockSnapshot.key == "image") continue
                    val stock = stockSnapshot.getValue(Stock::class.java)
                    stock?.let { stocksList.add(it) }
                }
                _stocks.value = stocksList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.d("category", "${categoryKey}: ${error.message}")
            }
        })
    }

    fun saveProductInDatabase(data:Stock, category: String, image_uri:Uri?){

        val fileReference = storageRef.child("$key/$category/${data.name}.jpg")
        if(image_uri!=null) {
            fileReference.putFile(image_uri).addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener {
                    saveProductInRealTime(data, category)
                }.addOnFailureListener {
                    Log.d("StockFragment", "ImageDownload Failed: ${it.message}")
                }
            }.addOnFailureListener{
                Log.d("StockFragment", "ImageUpLoad Failed: ${it.message}")
            }
        }else {
            saveProductInRealTime(data, category)
        }
    }
    fun saveProductInRealTime(data: Stock, category: String){
        dataRef.child("$key/$category").push().setValue(data).addOnSuccessListener {

            Log.d("StockFragment","Product is successfully added.")
        }.addOnFailureListener {
            Log.d("StockFragment","Product is Failed to add : ${it.message}")
        }
    }
    fun deleteCategory(category: String){
        val categoryKey=categoryKeyMap[category]
        dataRef.child("$key/$categoryKey").removeValue().addOnSuccessListener {
            Log.d("InventoryRepository", "Category successfully deleted.")
        }.addOnFailureListener { error ->
            Log.d("InventoryRepository", "Failed to delete category: ${error.message}")
        }
    }
}