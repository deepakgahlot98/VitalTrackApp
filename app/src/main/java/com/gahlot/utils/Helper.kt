package com.gahlot.utils

object Helper {

    fun convertDateToFloat(date : String) : Float {
        return date.split("/")[0].toFloat() + (date.split("/")[1].toFloat()/100)
    }
}