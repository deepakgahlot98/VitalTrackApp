package com.gahlot.ui

import android.R.attr.entries
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gahlot.models.Vital
import com.gahlot.utils.Helper
import com.gahlot.viewmodel.VitalViewModel
import com.gahlot.vitaltrackingapp.MainActivity
import com.gahlot.vitaltrackingapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
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
        setupBarChart()

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

                        var bloodsugar = list.indices.map { i ->
                            BarEntry(
                                //TODO need to improve this naive implementation
                                Helper.convertDateToFloat(list[i][1]),
                                list[i][0].toFloat()
                            )
                        }

                        val barDataSet = BarDataSet(bloodsugar, "Blood Sugar").apply {
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
    private fun addGraphData(vital: Vital) : MutableList<List<String>> {
        for ((index) in vital.dates.withIndex()) {
            var subList  = mutableListOf<String>()
            subList.add(vital.values[index])
            subList.add(vital.dates[index])
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
            description.text = "Avg Blood Sugar"
            description.textSize = 14f
            description.textColor = Color.RED
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