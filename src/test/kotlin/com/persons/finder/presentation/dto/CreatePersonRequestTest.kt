package com.persons.finder.presentation.dto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreatePersonRequestTest {

    @Test
    fun `create person request with valid data`() {
        val name = "Ronald"
        val email = "rd@finder.com"
        val request = CreatePersonRequest(name, email)
        val person = request.toDomain()
        assertEquals(name, person.name)
        assertEquals(email, person.email)
        assertEquals(null, person.id)
    }

}