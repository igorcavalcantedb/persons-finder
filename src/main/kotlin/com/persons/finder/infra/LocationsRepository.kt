package com.persons.finder.infra

import com.persons.finder.domain.model.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LocationsRepository : JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.person.id = :personId")
    fun findByPersonId(@Param("personId") personId: Long): Location?

    @Query("SELECT l FROM Location l WHERE l.person.id != :personId")
    fun findOtherLocations(@Param("personId") personId: Long): List<Location>

    @Query("SELECT l FROM Location l WHERE l.person.id != :personId AND l.latitude BETWEEN :minLatitude AND :maxLatitude")
    fun findOtherLocations(
        @Param("personId") personId: Long,
        @Param("minLatitude") minLatitude: Double,
        @Param("maxLatitude") maxLatitude: Double
    ): List<Location>
}
