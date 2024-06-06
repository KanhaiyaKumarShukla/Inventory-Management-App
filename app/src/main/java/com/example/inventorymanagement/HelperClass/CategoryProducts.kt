package com.example.inventorymanagement.HelperClass

data class CategoryProducts (
    val name: String = "",
    val image: String? = null,
    var items: MutableMap<String, Stock> = mutableMapOf()
)