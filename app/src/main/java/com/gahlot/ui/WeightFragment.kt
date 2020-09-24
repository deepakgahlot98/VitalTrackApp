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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.fragment_blood_sugar.*

class WeightFragment : Fragment(R.layout.fragment_weight) {

    lateinit var viewModel : VitalViewModel
    lateinit var chart : BarChart
    private val TAG = "WeightFragment"
    private var list = mutableListOf<List<String>>()
    private var map  = mutableListOf<List<String>>()
    var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        chart = view.findViewById(R.id.barChat_weight)
        viewModel.getVitalData()
        setupBarChart()

        viewModel.vitalInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        for (item in it.vitals) {
                            if (item.type == "Weight") {
                                list = addGraphData(item)
                            }
                        }

                        val bloodsugar = list.indices.map { i ->
                            BarEntry(
                                //TODO need to improve this naive implementation
                                list.get(i).get(1).split("/").get(0).toFloat(),
                                list.get(i).get(0).toFloat()
                            )
                        }
                        val barDataSet = BarDataSet(bloodsugar, "Weight").apply {
                            valueTextColor = Color.WHITE
                            valueTextSize = 12f
                            color = ContextCompat.getColor(requireContext(), R.color.white)
                        }
                        chart.data = BarData(barDataSet)
                        chart.invalidate()

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let {
                        Log.e(TAG, "Error Occured: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    //TODO need to use enum or some better approach to solve this
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
            textSize = 15f
            setDrawGridLines(false)
        }
        chart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            textSize = 15f
            setDrawGridLines(false)
        }
        chart.apply {
            description.text = "Avg Weight"
            description.textSize = 14f
            description.textColor = Color.YELLOW
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