package com.example.inventorymanagement.HelperClass

data class Stock(
    val name: String = "",
    val image: String? = null,
    val inventory: inventory = inventory(),
    val sells : Sell =Sell(),
    val stockId : String? =null
)