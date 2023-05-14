package com.persons.finder.domain.model

import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "idx_latitude", columnList = "latitude")])
class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val latitude: Double,
    val longitude: Double,
    @Column(name = "person_id", unique = true)
    val personId: Long
) {
    constructor() : this(null, 0.0, 0.0, 0)
    constructor(latitude: Double, longitude: Double) : this(null, latitude, longitude, 0)
    constructor(latitude: Double, longitude: Double, person: Long) : this(null, latitude, longitude, person)

}
