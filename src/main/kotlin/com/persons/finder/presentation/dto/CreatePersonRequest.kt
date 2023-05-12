package com.persons.finder.presentation.dto

import com.persons.finder.domain.model.Person
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


data class CreatePersonRequest(@field:NotBlank @field:Size(max=100) val name: String, @field:Email val email: String) {

    fun toDomain(): Person {
        return Person(id = null, name = this.name, email = this.email)
    }
}
