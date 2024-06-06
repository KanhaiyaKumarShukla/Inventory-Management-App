package com.example.inventorymanagement.HelperClass

data class Stock(
    val id: Int = 0,
    val name: String = "",
    val image: String? = null,
    val inventory: inventory = inventory(),
    val sells : Sell =Sell()
)