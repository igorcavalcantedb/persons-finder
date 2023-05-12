package com.persons.finder.domain.services.impl

import com.persons.finder.domain.model.Location
import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.impl.dto.LatitudeRange
import com.persons.finder.infra.LocationsRepository
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
        locationsRepository.save(location);
    }

    override fun removeLocation(locationReferenceId: Long) {
        TODO("Not yet implemented")
    }

    override fun findAround(latitude: Double, longitude: Double, radiusInKm: Double): List<Location> {
        TODO("Not yet implemented")
    }

    override fun findAround(personId: Long, radiusInKm: Double): List<Long> {
        val personLocation = locationsRepository.findByPersonId(personId)
            ?: throw IllegalArgumentException("Could not find location for person with ID $personId")
        val latitudeRange = calculateRange(personLocation.latitude, radiusInKm)
        val otherLocations = locationsRepository.findOtherLocations(personId, latitudeRange.min,latitudeRange.max)
        val locationDistances = otherLocations.map { location ->
            Pair(location, calculateDistanceInKm(personLocation, location))
        }
        val nearbyLocations = locationDistances.filter { (location, distance) ->
            distance <= radiusInKm
        }
        val sortedLocations = nearbyLocations.sortedBy { it.second }

        return sortedLocations.map { it.first.person!!.id!! }
    }

    private fun calculateRange(latitude: Double, rangeInKm: Double): LatitudeRange {
        val latitudeRange = rangeInKm / Companion.AVG_KM_PER_LATITUDE_DEGREE;
        var latitudeMax = latitude - latitudeRange;
        var latitudeMin = latitude + latitudeRange;

        return LatitudeRange(latitudeMin, latitudeMax)
    }

    private fun calculateDistanceInKm(personLocation: Location, otherLocation: Location): Double {


        val deltaLatitude = Math.toRadians(otherLocation.latitude - personLocation.latitude)
        val deltaLongitude = Math.toRadians(otherLocation.longitude - personLocation.longitude)
        val a = sin(deltaLatitude / 2) * sin(deltaLatitude / 2)
        +cos(Math.toRadians(personLocation.latitude)) * cos(Math.toRadians(otherLocation.latitude)) * sin(deltaLongitude / 2) * sin(
            deltaLongitude / 2
        )
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return Companion.RADIUS * c


    }



}