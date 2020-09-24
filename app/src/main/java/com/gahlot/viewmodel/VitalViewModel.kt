package com.gahlot.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gahlot.repository.VitalRepository
import com.gahlot.ui.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.xml.transform.Source

class VitalViewModel(app: Application,val vitalRepository: VitalRepository) : AndroidViewModel(app) {

    var vitalInfo : MutableLiveData<Resource<com.gahlot.models.Source>> = MutableLiveData()
    var vitalInfoResponse: com.gahlot.models.Source? = null

    private fun handleVitalResponse(response: Response<com.gahlot.models.Source>) : Resource<com.gahlot.models.Source> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (vitalInfoResponse == null) {
                    vitalInfoResponse = it
                } else {
                    vitalInfoResponse = it
                }
                return Resource.Success(vitalInfoResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun getVitalCall() {
        vitalInfo.postValue(Resource.Loading())
        try {
            val response = vitalRepository.getVitals()
            vitalInfo.postValue(handleVitalResponse(response))
        } catch (t: Throwable) {
            when(t) {
                is IOException -> vitalInfo.postValue(Resource.Error("Network Error"))
                else -> vitalInfo.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getVitalData() = viewModelScope.launch {
        getVitalCall()
    }

}