package com.persons.finder.presentation

import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.exceptions.PersonNotFoundException
import com.persons.finder.presentation.dto.*
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Pattern

@RestController
@RequestMapping("api/v1/persons")
@Validated
class PersonController(private val personsService: PersonsService, private val locationsService: LocationsService) {

    @PostMapping
    @ApiOperation("Create a person")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
        ApiResponse(code = 201, message = "Created", response = PersonResponse::class),
        ApiResponse(code = 400, message = "Bad Request")
    )
    fun createPerson(@RequestBody @Valid createPersonRequest: CreatePersonRequest): ResponseEntity<PersonResponse> {
        val newPerson = personsService.save(createPersonRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(PersonResponse.fromDomain(newPerson))

    }

    @PutMapping("/locations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update or create someone's location using latitude and longitude")
    @ApiResponses(
        value = [
            ApiResponse(code = 204, message = "Location updated successfully"),
            ApiResponse(code = 400, message = "Invalid request"),
            ApiResponse(code = 404, message = "Person not found")
        ]
    )
    fun updateLocation(@RequestBody @Valid updateLocationRequest: UpdateLocationRequest): ResponseEntity<Void> {
        val location = updateLocationRequest.toDomain()
        location.person?.id?.let { id -> personsService.getById(id) }
            ?: throw PersonNotFoundException("Person not found.")
        locationsService.addLocation(location)

        return ResponseEntity.noContent().build()

    }

    @GetMapping("/{personId}/nearby")
    @ApiOperation(
        value = "Locate the IDs of people who are within a specific range from your current location.",
        response = PeopleAroundResponse::class
    )
    @ApiResponses(
        value = [
            ApiResponse(
                code = 200,
                message = "List of people around the specified person within the given radius",
                response = PeopleAroundResponse::class
            ),
            ApiResponse(
                code = 400,
                message = "Invalid input parameter"
            ),
            ApiResponse(
                code = 404,
                message = "Person not found"
            ),
            ApiResponse(
                code = 500,
                message = "Internal server error"
            )
        ]
    )
    fun getPeopleAround(
        @PathVariable @Valid @Pattern(regexp = "^[1-9]\\d{0,17}$") personId: String,
        @RequestParam @Valid @DecimalMin("0.0") radius: Double
    ): ResponseEntity<PeopleAroundResponse> {
        val findAroundList = locationsService.findAround(personId.toLong(), radius)
        val response = PeopleAroundResponse(findAroundList)
        return ResponseEntity.ok(response)
    }


    @GetMapping("/names")
    @ApiOperation(value = "Get persons' names by their IDs", response = PeopleNamesResponse::class)
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successfully retrieved persons' names"),
            ApiResponse(code = 400, message = "Invalid input parameter(s)"),
            ApiResponse(code = 404, message = "One or more persons not found")
        ]
    )
    fun getPersonsNames(@RequestParam ids: List<Long>): ResponseEntity<PeopleNamesResponse> {
        val names = ids.map { id ->
            val person = personsService.getById(id)
            person.name
        }
        val response = PeopleNamesResponse(names)
        return ResponseEntity.ok(response)
    }

}