package com.gahlot.models


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("city")
    val city: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("vitals")
    val vitals: List<Vital>
)