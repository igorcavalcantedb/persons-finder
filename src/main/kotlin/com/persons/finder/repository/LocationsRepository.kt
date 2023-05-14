package com.persons.finder.repository

import com.persons.finder.domain.model.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface LocationsRepository : JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.personId = :personId")
    fun findByPersonId(@Param("personId") personId: Long): Optional<Location>

    @Query("SELECT l FROM Location l WHERE l.personId != :personId")
    fun findOtherLocations(@Param("personId") personId: Long): List<Location>

    @Query("SELECT l FROM Location l WHERE l.personId != :personId AND l.latitude BETWEEN :minLatitude AND :maxLatitude")
    fun findLocationsWithinLatitudeRangeExcludingPersonId(
        @Param("personId") personId: Long,
        @Param("minLatitude") minLatitude: Double,
        @Param("maxLatitude") maxLatitude: Double
    ): List<Location>

    @Query("SELECT l FROM Location l WHERE   l.latitude != :latitude AND l.longitude != :longitude AND l.latitude BETWEEN :minLatitude AND :maxLatitude")
    fun findLocationsWithRangeExcludingLocation(
        @Param("latitude") latitude: Double,
        @Param("longitude") longitude: Double,
        @Param("minLatitude") minLatitude: Double,
        @Param("maxLatitude") maxLatitude: Double
    ): List<Location>

    @Query("SELECT l FROM Location l WHERE l.personId != :personId AND l.latitude BETWEEN :minLatitude AND :maxLatitude AND l.longitude BETWEEN :minLongitude AND :maxLongitude")
    fun findLocationsWithinCoordanatesRangeExcludingPersonId(
        @Param("personId") personId: Long,
        @Param("minLatitude") minLatitude: Double,
        @Param("maxLatitude") maxLatitude: Double,
        @Param("minLongitude") minLongitude: Double,
        @Param("maxLongitude") maxLongitude: Double
    ): List<Location>
}
