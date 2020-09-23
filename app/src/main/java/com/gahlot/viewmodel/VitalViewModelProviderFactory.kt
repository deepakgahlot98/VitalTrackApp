package com.gahlot.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gahlot.repository.VitalRepository

class VitalViewModelProviderFactory(val app: Application, val vitalRepository: VitalRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VitalViewModel(app,vitalRepository) as T
    }


}