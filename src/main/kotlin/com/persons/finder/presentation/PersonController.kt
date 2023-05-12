package com.persons.finder.presentation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

    /*
        TODO GET API to retrieve a person or persons name using their ids
        // Example
        // John has the list of people around them, now they need to retrieve everybody's names to display in the app
        // API would be called using person or persons ids
     */

    @GetMapping("")
    fun getExample(): String {
        return "Hello Example"
    }

}