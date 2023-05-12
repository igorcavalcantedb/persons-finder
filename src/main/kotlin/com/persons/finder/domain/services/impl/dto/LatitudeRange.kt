package com.persons.finder.domain.services.impl.dto

data class LatitudeRange(var min: Double, var max: Double) {
    init {
        if (min > max) {
            val temp = max
            max = min
            min = temp
        }
    }
}