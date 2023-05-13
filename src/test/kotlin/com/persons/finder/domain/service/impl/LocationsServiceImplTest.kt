package com.persons.finder.domain.service.impl

import com.persons.finder.domain.model.Location
import com.persons.finder.domain.model.Person
import com.persons.finder.domain.services.impl.LocationsServiceImpl
import com.persons.finder.infra.LocationsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.*

class LocationsServiceImplTest {

    @Mock
    private lateinit var locationsRepository: LocationsRepository
    private lateinit var locationsService: LocationsServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        locationsService = LocationsServiceImpl(locationsRepository)
    }

    @Test
    fun `should update a location`() {
        val personId = 1L
        val location = Location(null, 37.7749, -122.4194, person = Person(personId))
        val oldLocation = Location(id = 1L, 36.7749, -152.4195, person = Person(personId))

        doReturn(Optional.of(oldLocation)).`when`(locationsRepository).findByPersonId(personId)
        doReturn(location).`when`(locationsRepository).save(location)

        locationsService.addLocation(location)

        verify(locationsRepository).findByPersonId(personId)
        verify(locationsRepository).save(location)
    }

    @Test
    fun `should not add a location without a valid person reference`() {
        val location = Location(1.0, 2.0)
        assertThrows(IllegalArgumentException::class.java) {
            locationsService.addLocation(location)
        }
    }

    @Test
    fun `should remove a location`() {
        val locationId = 1L
        locationsService.removeLocation(locationId)
        verify(locationsRepository).deleteById(locationId)
    }

    @Test
    fun `should find locations within given radius`() {

        val latitude = -36.7863921
        val longitude = 174.724943
        val radiusInKm = 7.0

        val locations = createLocations()

        doReturn(locations).`when`(locationsRepository)
            .findLocationsWithRangeExcludingLocation(anyDouble(), anyDouble(), anyDouble(), anyDouble())

        val locationsWithinRadius = locationsService.findAround(latitude, longitude, radiusInKm)

        assertThat(locationsWithinRadius).containsExactly(locations.component3(), locations.component2())
    }

    @Test
    fun `should find locations by Id within given radius `() {

        val latitude = -36.7863921
        val longitude = 174.724943
        val radiusInKm = 7.0

        val locations = createLocations()

        doReturn(Optional.of(Location(latitude, longitude))).`when`(locationsRepository).findByPersonId(anyLong())
        doReturn(locations).`when`(locationsRepository)
            .findLocationsWithinLatitudeRangeExcludingPersonId(anyLong(), anyDouble(), anyDouble())

        val locationsWithinRadius = locationsService.findAround(1L, radiusInKm)

        assertThat(locationsWithinRadius).containsExactly(3, 2)
    }

    private fun createLocations(): List<Location> {
        val location1 = Location(1L, -37.0082476, 174.7824609, person = Person(1, "AKL"))
        val location2 = Location(2L, -36.8485, 174.7633, person = Person(2, "Sky Tower"))
        val location3 = Location(3L, -36.8252, 174.7503, person = Person(3, "Auckland Harbour Bridge"))
        val location4 = Location(4L, -36.8531, 174.7667, person = Person(4, "Auckland Art Gallery"))
        val location5 = Location(5L, -36.8780, 174.7645, person = Person(5, "Mount Eden"))
        return listOf(location1, location2, location3, location4, location5)
    }

}