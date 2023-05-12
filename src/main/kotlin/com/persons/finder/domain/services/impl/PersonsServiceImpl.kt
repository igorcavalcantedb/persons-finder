package com.persons.finder.domain.services.impl

import com.persons.finder.Exceptions.DatabaseException
import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.infra.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonsServiceImpl(private val personRepository: PersonRepository) : PersonsService {

    override fun getById(id: Long): Person {
       return  personRepository.findById(id).orElseThrow()

    }

    override fun save(person: Person):Person {
       // return personRepository.save(person).id ?: throw DatabaseException("Failed to save person")
        return personRepository.save(person)
    }

}