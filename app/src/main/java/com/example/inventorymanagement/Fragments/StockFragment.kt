package com.example.inventorymanagement.Fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.inventorymanagement.HelperClass.IdManager
import com.example.inventorymanagement.HelperClass.Sell
import com.example.inventorymanagement.HelperClass.Stock
import com.example.inventorymanagement.HelperClass.inventory
import com.example.inventorymanagement.databinding.FragmentStockBinding
import com.example.inventorymanagement.databinding.SellDialogBinding
import com.example.inventorymanagement.databinding.StockDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class StockFragment : Fragment() {

    private var _binding : FragmentStockBinding ?=null
    private val binding get() = _binding!!
    private lateinit var category:String
    private lateinit var database: FirebaseDatabase
    private lateinit var dataRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var key:String
    private lateinit var storageRef: StorageReference
    private var image_uri  : Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        image_uri = it
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category= arguments?.getString("category").toString()
        database=FirebaseDatabase.getInstance()
        dataRef=database.reference.child("Category")
        firebaseAuth= FirebaseAuth.getInstance()
        key=firebaseAuth.currentUser!!.uid
        storageRef= Firebase.storage.getReference("Category")

        binding.addStockFab.setOnClickListener{
            showStockDialog()
        }

    }
    private fun saveProductInDatabase(data:Stock){

        val fileReference = storageRef.child("$key/$category/${data.name}.jpg")
        if(image_uri!=null) {
            fileReference.putFile(image_uri!!).addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener {
                    saveProductInRealTime(data)
                }.addOnFailureListener {
                    Log.d("StockFragment", "ImageDownload Failed: ${it.message}")
                }
            }.addOnFailureListener{
                Log.d("StockFragment", "ImageUpLoad Failed: ${it.message}")
            }
        }else {
            saveProductInRealTime(data)
        }
    }
    private fun saveProductInRealTime(data: Stock){

        val stockId=dataRef.child("$key/$category").push().key
        dataRef.child("$key/$category").push().setValue(data).addOnSuccessListener {

            Log.d("StockFragment","Product is successfully added.")
        }.addOnFailureListener {
            Log.d("StockFragment","Product is Failed to add : ${it.message}")
        }
    }
    private fun showStockDialog(){

        val sBinding = StockDialogBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Stock")
            .setView(sBinding.root)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }.create()

        dialog.setOnShowListener {
            val btn = dialog.getButton(Dialog.BUTTON_POSITIVE)

            sBinding.pickImageButton.setOnClickListener{
                getContent.launch("image/*")
            }

            btn.setOnClickListener {
                val name = sBinding.stockName.text.toString()
                val quantity = sBinding.quantity.text.toString()
                val price = sBinding.price.text.toString()
                if (name.isEmpty()) {
                    sBinding.stockName.error = "Name is Required"
                } else if (quantity.isEmpty()) {
                    sBinding.quantity.error = "Quantity is Required"
                } else if (price.isEmpty()) {
                    sBinding.price.error = "Price is Required"
                } else {
                    val inventory=inventory(
                        quantity = sBinding.quantity.text.toString().toIntOrNull() ?: 0,
                        price = sBinding.price.text.toString().toDoubleOrNull() ?: 0.0
                    )
                    val data= Stock(
                        name = sBinding.stockName.text.toString(),
                        inventory =inventory,
                        image = image_uri.toString()
                    )

                    saveProductInDatabase(data)
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    fun onClickEdit(stock: Stock){
        val sBinding = StockDialogBinding.inflate(layoutInflater)
        sBinding.stockName.text = Editable.Factory.getInstance().newEditable(stock.name)
        sBinding.price.text = Editable.Factory.getInstance().newEditable(stock.inventory.price.toString())
        sBinding.quantity.text = Editable.Factory.getInstance().newEditable(stock.inventory.quantity.toString())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Stock")
            .setView(sBinding.root)
            .setPositiveButton("Edit"){dialog,_ ->
                val inventory=inventory(
                    quantity = sBinding.quantity.text.toString().toIntOrNull() ?: 0,
                    price = sBinding.price.text.toString().toDoubleOrNull() ?: 0.0
                )
                val data= Stock(
                    name = sBinding.stockName.text.toString(),
                    inventory= inventory,
                    image = image_uri.toString()
                )
          // add delete this data from daotabase and then add to database

                saveProductInDatabase(data)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    fun onClickDelete(stock : Stock){
        // delete this stock from database
    }

    fun onClickSell(stock : Stock){
        val sBinding = SellDialogBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Stock")
            .setView(sBinding.root)
            .setPositiveButton("Sell", null)
            .setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }.create()

        dialog.setOnShowListener {
            val btn = dialog.getButton(Dialog.BUTTON_POSITIVE)

            btn.setOnClickListener {
                val name = sBinding.buyerName.text.toString()
                val quantity = sBinding.quantity.text.toString()
                val price = sBinding.price.text.toString()
                if (name.isEmpty()) {
                    sBinding.buyerName.error = "Name is Required"
                } else if (quantity.isEmpty()) {
                    sBinding.quantity.error = "Quantity is Required"
                } else if (price.isEmpty()) {
                    sBinding.price.error = "Price is Required"
                } else {

                    if (quantity.toInt() > stock.inventory.quantity.toInt()) {
                        sBinding.quantity.error = "Quantity is Limited"
                    } else {
                        val data = Sell(
                            name = sBinding.buyerName.text.toString(),
                            quantity = sBinding.quantity.text.toString().toInt() ?: 0,
                            price = sBinding.price.text.toString().toDoubleOrNull() ?: 0.0,
                        )
                        val amount = (quantity.toDouble()) * (price.toDouble())
                        sBinding.amount.text = amount.toString()
                        sBinding.layoutAmount.visibility = View.VISIBLE

                        // now add this sell to database

                        val newQuantity = stock.inventory.quantity.toInt() - quantity.toInt()
                        if(newQuantity == 0){
                            onClickDelete(stock) // use to delete stock
                        }else{
                            // delete this stock from database and add updated stock
                        }

                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}