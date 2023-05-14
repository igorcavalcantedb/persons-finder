package com.persons.finder.domain.services.impl.dto

data class CoordinatesRange(var min: Double, var max: Double) {
    init {
        if (min > max) {
            val temp = max
            max = min
            min = temp
        }
    }
}