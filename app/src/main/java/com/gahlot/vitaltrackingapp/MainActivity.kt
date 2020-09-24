package com.gahlot.vitaltrackingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gahlot.repository.VitalRepository
import com.gahlot.viewmodel.VitalViewModel
import com.gahlot.viewmodel.VitalViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mNavigationView: BottomNavigationView
    lateinit var viewModel: VitalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = VitalRepository()
        val viewModelProviderFactory = VitalViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(VitalViewModel::class.java)

        mNavigationView = findViewById(R.id.bottomNavigationView)
        mNavigationView.itemIconTintList = null

        mNavigationView.setupWithNavController(vitalNavHostFragment.findNavController())
    }
}