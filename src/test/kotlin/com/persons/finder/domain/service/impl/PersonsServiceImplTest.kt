package com.persons.finder.domain.service.impl

import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.domain.services.impl.PersonsServiceImpl
import com.persons.finder.infra.PersonRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations
import java.util.*

class PersonsServiceImplTest {

    @Mock
    private lateinit var personRepository: PersonRepository

    private lateinit var personsService: PersonsService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        personsService = PersonsServiceImpl(personRepository)
    }

    @Test
    fun `should return a person by id`() {
        val person = Person(1L, "Josh Lee", "jl@gmail.com")
        val optionalPerson: Optional<Person> = Optional.of(person)
        doReturn(optionalPerson).`when`(personRepository).findById(1L)

        val result = personsService.getById(1L)

        assertEquals(person, result)
    }

    @Test
    fun `should save a person`() {
        val person = Person(null, "Josh Lee", "jl@gmail.com")
        val savedPerson = Person(1L, "Josh Lee", "jl@gmail.com")
        doReturn(savedPerson).`when`(personRepository).save(person)
        val result = personsService.save(person)

        assertEquals(savedPerson, result)
    }
}