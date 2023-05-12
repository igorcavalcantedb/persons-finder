package com.persons.finder.presentation.dto

import com.persons.finder.domain.model.Location
import com.persons.finder.domain.model.Person

class UpdateLocationRequest(
    val personId: Long,
    val latitude: Double,
    val longitude: Double
) {
    fun toDomain(): Location {
        return Location(null,this.latitude,this.longitude, Person(id=personId,"",""))
    }
}


