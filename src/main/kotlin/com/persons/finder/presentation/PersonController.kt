package com.persons.finder.presentation

import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.presentation.dto.CreatePersonRequest
import com.persons.finder.presentation.dto.PersonResponse
import com.persons.finder.presentation.dto.UpdateLocationRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/persons") //people
@Validated
class PersonController(private val personsService: PersonsService, private val locationsService: LocationsService) {

   @PostMapping
    fun createPerson(@RequestBody @Valid createPersonRequest: CreatePersonRequest): ResponseEntity<PersonResponse> {
        val newPerson = personsService.save(createPersonRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(PersonResponse.fromDomain(newPerson))

    }

    @PutMapping
    fun updateLocation(@RequestBody @Valid updateLocationRequest: UpdateLocationRequest): ResponseEntity<Void> {

        locationsService.addLocation(updateLocationRequest.toDomain())

        return ResponseEntity.noContent().build()

    }


    @GetMapping("/{personId}/nearby")
    fun getPeopleAround(@PathVariable personId: Long, @RequestParam @Valid radius: Double): ResponseEntity<List<Long>> {
        val response = locationsService.findAround(personId, radius)

        return ResponseEntity.ok(response)
    }


    @GetMapping
    fun getPersonsNames(@RequestParam ids: List<Long>): ResponseEntity<List<String>> {
        val names = mutableListOf<String>()
        for (id in ids) {
            val person = personsService.getById(id)
            names.add(person.name)
        }
        return ResponseEntity.ok(names)
    }

    @GetMapping("/Example")
    fun getExample(): String {
        return "Hello Example"
    }

}