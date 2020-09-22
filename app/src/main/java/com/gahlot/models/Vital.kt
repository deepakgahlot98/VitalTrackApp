package com.gahlot.models


import com.google.gson.annotations.SerializedName

data class Vital(
    @SerializedName("dates")
    val dates: List<String>,
    @SerializedName("type")
    val type: String,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("values")
    val values: List<String>
)