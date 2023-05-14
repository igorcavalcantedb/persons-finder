package com.persons.finder.domain.services.impl

import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.exceptions.PersonNotFoundException
import com.persons.finder.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonsServiceImpl(private val personRepository: PersonRepository) : PersonsService {

    override fun getById(id: Long): Person {
        return personRepository.findById(id).orElseThrow() {
            throw PersonNotFoundException("Person with id $id not found")
        }

    }

    override fun save(person: Person):Person {
        return personRepository.save(person)
    }

}