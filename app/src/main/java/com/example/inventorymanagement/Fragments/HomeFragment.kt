package com.example.inventorymanagement.Fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.inventorymanagement.HelperClass.CustomBarChartRenderer
import com.example.inventorymanagement.R
import com.example.inventorymanagement.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding ?=null
    private val binding get() = _binding!!
    private lateinit var bar :BarChart
    private lateinit var pie:PieChart
    private lateinit var database:FirebaseDatabase
    private lateinit var dataRef: DatabaseReference

    private lateinit var categoryImage: Uri
    private var isImageAdded=false
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){uri->

        uri?.let {
            categoryImage=it
            isImageAdded=true
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database=FirebaseDatabase.getInstance()
        dataRef=database.reference.child("Category")

//        dataRef.child("$key/Fruits").setValue("30").addOnSuccessListener {
//            Toast.makeText(requireContext(), "data successfully uploaded", Toast.LENGTH_SHORT).show()
//
//        }.addOnFailureListener{
//            Toast.makeText(requireContext(), "data uploading Failed", Toast.LENGTH_SHORT).show()
//        }
        binding.fab.setOnClickListener{
            showCategoryDialog()
        }
        setUpBarChart()
        setUpPieChart()

    }
    private fun showCategoryDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_category, null)
        val edittxt = dialogView.findViewById<EditText>(R.id.edit)
        val insert_img = dialogView.findViewById<Button>(R.id.btn)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add New Category")
            .setView(dialogView)
            .setPositiveButton("Add", null)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.create()

        dialog.setOnShowListener {
            val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            insert_img.setOnClickListener {
                getContent.launch("image/*")
            }
            btn.setOnClickListener {
                val category = edittxt.text.toString()
                if(category.isNotEmpty()){
                    // navigate to next fragment
                    saveCategory(category)
                    dialog.dismiss()
                }else{
                    edittxt.error = "Category Name Cannot Be Empty"
                }
            }
        }
        dialog.show()
    }

    private fun saveCategory(category:String) {
        val key=dataRef.push().key.toString()
        dataRef.child(key).child(category)
    }

    private fun setUpPieChart(){
        val data=listOf<PieEntry>(
            PieEntry(50f, "Sell"),
            PieEntry(40f, "Buy"),
            PieEntry(10f, "Profit")
        )
        val dataSet=PieDataSet(data, "Profit-Loss Report")
        dataSet.colors=ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextColor=Color.BLACK
        dataSet.valueTextSize=15f
        val pieData=PieData(dataSet)
//        binding.piechart.apply{
//            this.data=pieData
//            this.description.isEnabled=true
//            this.centerText="Profit / Loss"
//            this.animate()
//        }
        pie= binding.piechart
        pie.apply {
            this.data = pieData
            this.description.isEnabled = true
            this.centerText = "Profit / Loss"
            this.setUsePercentValues(true) // Use percentage values
            this.setDrawEntryLabels(false) // Disable drawing entry labels
            this.setDrawCenterText(true)
            this.animate()

            // Customize label text properties
            this.setDrawEntryLabels(true)
            this.setEntryLabelColor(Color.BLACK)
            this.setEntryLabelTextSize(12f)

        }
    }
    private fun setUpBarChart(){
        bar=binding.barchart
        val barEntries = listOf<BarEntry>(
            BarEntry(0f, 70f), // Add text as the third parameter
            BarEntry(1f, 30f),
            BarEntry(2f, 100f),
            BarEntry(3f, 60f)
        )
        val dataset=BarDataSet(barEntries, "Report")
        dataset.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataset.valueTextColor= Color.BLACK
        dataset.valueTextSize=20f

        val barData=BarData(dataset)

        bar.setFitBars(true)
        bar.data=barData
        bar.description.text="Bar Report"
        bar.animateY(2000)
        val labels = listOf("Text1", "Text2", "Text3", "Text4")
        bar.renderer = CustomBarChartRenderer(
            bar,
            bar.animator,
            bar.viewPortHandler,
            labels
        )

    }

}