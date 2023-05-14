package com.persons.finder.domain.model

import javax.persistence.*

@Entity
class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val latitude: Double,
    val longitude: Double,
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", unique = true)
    val person: Person? = null
) {
    constructor() : this(null, 0.0, 0.0, null)
    constructor(latitude: Double, longitude: Double) : this(null, latitude, longitude, null)
    constructor(latitude: Double, longitude: Double, person: Person) : this(null, latitude, longitude, person)

}
