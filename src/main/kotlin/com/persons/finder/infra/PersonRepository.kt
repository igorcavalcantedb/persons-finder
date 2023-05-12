package com.persons.finder.infra

import com.persons.finder.domain.model.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository:JpaRepository<Person,Long>
