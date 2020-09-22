package com.gahlot.repository

import com.gahlot.api.RetrofitInstance

class VitalRepository() {

    suspend fun getVitals() =
        RetrofitInstance.api.getVitals()

}