package com.gahlot.api

import retrofit2.Response
import retrofit2.http.GET
import javax.xml.transform.Source

interface VitalApi {

    @GET("v3/cbeaa5c4-9fe3-4a60-abbf-ca95b70a48df")
    suspend fun getVitals() : Response<Source>

}