package com.persons.finder.domain.services.impl

import com.persons.finder.domain.model.Location
import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.impl.dto.CoordinatesRange
import com.persons.finder.repository.LocationsRepository
import org.springframework.stereotype.Service
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class LocationsServiceImpl(private val locationsRepository: LocationsRepository) : LocationsService {

    companion object {
        private const val RADIUS = 6371 // The average radius of the Earth in kilometers
        private const val AVG_KM_PER_LATITUDE_DEGREE = 111.2 // 1 degree of latitude = 111.2 km
    }

    override fun addLocation(location: Location) {
        location.personId.let { personId ->
            val oldLocation = locationsRepository.findByPersonId(personId)
            if (oldLocation.isPresent) {
                location.id = oldLocation.get().id
                locationsRepository.save(location)
            } else locationsRepository.save(location)
        }
    }

    override fun removeLocation(locationReferenceId: Long) {
        locationsRepository.deleteById(locationReferenceId)
    }


    override fun findAround(latitude: Double, longitude: Double, radiusInKm: Double): List<Location> {
        val personLocation = Location(latitude, longitude)
        val latitudeRange = calculateRange(personLocation.latitude, radiusInKm)
        val othersLocations = locationsRepository.findLocationsWithRangeExcludingLocation(
            latitude,
            longitude,
            latitudeRange.min,
            latitudeRange.max
        )
        return othersLocations
            .map { otherLocation -> Pair(otherLocation, calculateDistanceInKm(personLocation, otherLocation)) }
            .filter { (_, distance) -> distance <= radiusInKm }
            .sortedBy { (_, distance) -> distance }
            .map { (location, _) -> location }

    }

    override fun findAround(personId: Long, radiusInKm: Double): List<Long> {
        val personLocation = locationsRepository.findByPersonId(personId).orElseThrow {
            IllegalArgumentException("Could not find location for person with ID $personId")
        }
        val latitudeRange = calculateRange(personLocation.latitude, radiusInKm)
        val otherLocations = locationsRepository.findLocationsWithinLatitudeRangeExcludingPersonId(
            personId,
            latitudeRange.min,
            latitudeRange.max
        )

        val locationDistances = otherLocations.map { location ->
            Pair(location, calculateDistanceInKm(personLocation, location))
        }
        val nearbyLocations = locationDistances.filter { (_, distance) ->
            distance <= radiusInKm
        }
        val sortedLocations = nearbyLocations.sortedBy { it.second }

        return sortedLocations.map { it.first.personId }
    }

    private fun calculateRange(latitude: Double, rangeInKm: Double): CoordinatesRange {
        val latitudeRange = rangeInKm / AVG_KM_PER_LATITUDE_DEGREE
        val latitudeMax = latitude - latitudeRange
        val latitudeMin = latitude + latitudeRange

        return CoordinatesRange(latitudeMin, latitudeMax)
    }

    private fun calculateDistanceInKm(personLocation: Location, otherLocation: Location): Double {


        val deltaLatitude = Math.toRadians(otherLocation.latitude - personLocation.latitude)
        val deltaLongitude = Math.toRadians(otherLocation.longitude - personLocation.longitude)
        val a = sin(deltaLatitude / 2) * sin(deltaLatitude / 2)
        +cos(Math.toRadians(personLocation.latitude)) * cos(Math.toRadians(otherLocation.latitude)) * sin(deltaLongitude / 2) * sin(
            deltaLongitude / 2
        )
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return RADIUS * c


    }



}