package com.carlosmuvi.segmentedprogressbar

import android.graphics.Color


data class PropertiesModel(
    var segmentCount : Int = 5,
    var containerColor : Int = Color.LTGRAY,
    var fillColor : Int = Color.BLUE
)