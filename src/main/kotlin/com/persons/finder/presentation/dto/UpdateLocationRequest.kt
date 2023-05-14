package com.persons.finder.presentation.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.persons.finder.domain.model.Location
import javax.validation.constraints.*

class UpdateLocationRequest @JsonCreator constructor(
    @field:Positive
    @field:Pattern(regexp = "^[1-9]\\d{0,17}$")
    @JsonProperty("personId") val personId: String,
    @field:DecimalMax("90.0")
    @field:DecimalMin("-90.0")
    @JsonProperty("latitude") val latitude: Double,
    @field:DecimalMax("180.0")
    @field:DecimalMin("-180.0")
    @JsonProperty("longitude") val longitude: Double
) {

    fun toDomain(): Location {
        return Location(null, this.latitude, this.longitude, personId.toLong())
    }
}


