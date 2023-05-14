package com.persons.finder.presentation.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.persons.finder.domain.model.Person
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class CreatePersonRequest @JsonCreator constructor(
    @JsonProperty("name") @field:NotNull @field:Size(min = 3, max = 100) val name: String,
    @JsonProperty("email") @field:Email val email: String
) {

    fun toDomain(): Person {
        return Person(id = null, name = this.name, email = this.email)
    }
}
