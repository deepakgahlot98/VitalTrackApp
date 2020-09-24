package com.gahlot.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gahlot.models.Vital
import com.gahlot.viewmodel.VitalViewModel
import com.gahlot.vitaltrackingapp.MainActivity
import com.gahlot.vitaltrackingapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_blood_sugar.*

class BloodSugarFragment : Fragment(R.layout.fragment_blood_sugar) {

    lateinit var viewModel : VitalViewModel
    lateinit var chart : BarChart
    private val TAG = "BloodSugarFragment"
    private var list = mutableListOf<List<String>>()
    private var map  = mutableListOf<List<String>>()
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        chart = view.findViewById(R.id.barChat_bloodSugar)
        viewModel.getVitalData()

        viewModel.vitalInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        for (item in it.vitals) {
                            if (item.type == "Blood Sugar") {
                                list = addGraphData(item)
                            }
                        }
                        Log.i(TAG, "onViewCreated: " + list.get(1).get(1).split("/")[0].toFloat())
                        val bloodsugar = list.indices.map { i ->
                            BarEntry(
                                list.get(i).get(1).split("/").get(0).toFloat(),
                                list.get(i).get(0).toFloat()
                            )
                        }
                        val barDataSet = BarDataSet(bloodsugar, "bLOOD sUGAR").apply {
                            valueTextColor = Color.WHITE
                            color = ContextCompat.getColor(requireContext(), R.color.white)
                        }
                        chart.data = BarData(barDataSet)
                        chart.invalidate()

                    }
                }
            }
        })
    }

    private fun addGraphData(vital : Vital) : MutableList<List<String>> {
        for ((index, item) in vital.dates.withIndex()) {
            var subList  = mutableListOf<String>()
            subList.add(vital.values.get(index))
            subList.add(vital.dates.get(index))
            map.add(subList)
        }
        return map
    }

    private fun setupBarChart() {
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color .WHITE
            setDrawGridLines(false)
        }

        chart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        chart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        chart.apply {
            description.text = "Avg Blood Sugar"
            legend.isEnabled = false
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

}