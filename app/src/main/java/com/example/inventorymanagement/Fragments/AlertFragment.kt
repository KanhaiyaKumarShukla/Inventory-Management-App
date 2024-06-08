package com.example.inventorymanagement.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventorymanagement.Adapter.CategoryViewHolder
import com.example.inventorymanagement.Adapter.StockViewHolder
import com.example.inventorymanagement.HelperClass.Category
import com.example.inventorymanagement.HelperClass.Stock
import com.example.inventorymanagement.R
import com.example.inventorymanagement.databinding.FragmentAlertBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference


class AlertFragment : Fragment() {

    private var _binding:FragmentAlertBinding ?=null
    private val binding get() = _binding!!
    private var stocks = mutableListOf<Stock>()
    private lateinit var database: FirebaseDatabase
    private lateinit var dataRef: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var key:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database=FirebaseDatabase.getInstance()
        dataRef=database.reference.child("Category")
        firebaseAuth= FirebaseAuth.getInstance()
        key=firebaseAuth.currentUser!!.uid

    }
    fun loadStockData(){
        dataRef.child("$key/categories").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val newStocks = mutableListOf<Stock>()
                    for(categorySnapshot in snapshot.children) {
                        for (dataSnapshot in categorySnapshot.children) {
                            val stock = dataSnapshot.getValue(Stock::class.java)
                            if (stock != null) {
                                newStocks.add(stock)
                            }
                        }
                    }
                    stocks=(stocks+newStocks).toMutableList()
                    // here apply the recycler view adapter notification
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("HomeFragment","Error Failed to saved: ${error.message}")
            }
        })
    }
}