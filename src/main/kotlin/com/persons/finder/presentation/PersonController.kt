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

    /*
        TODO PUT API to update/create someone's location using latitude and longitude
        (JSON) Body
     */
    @PutMapping
    fun updateLocation(@RequestBody @Valid updateLocationRequest: UpdateLocationRequest): ResponseEntity<Void> {

        locationsService.addLocation(updateLocationRequest.toDomain())

        return ResponseEntity.noContent().build()

    }


    /*
        TODO GET API to retrieve people around query location with a radius in KM, Use query param for radius.
        TODO API just return a list of persons ids (JSON)
        // Example
        // John wants to know who is around his location within a radius of 10km
        // API would be called using John's id and a radius 10km
     */
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