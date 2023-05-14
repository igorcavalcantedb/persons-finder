package com.persons.finder.presentation.dto

import com.persons.finder.domain.model.Person

data class PersonResponse(val id: Long?, val name: String, val email: String) {
    companion object {
        fun fromDomain(person: Person): PersonResponse {
            return PersonResponse(id = person.id, name = person.name, email = person.email)
        } //todo Hiding the value of the database ID.
    }
}
