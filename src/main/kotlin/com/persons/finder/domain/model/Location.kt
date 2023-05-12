package com.persons.finder.domain.model

import javax.persistence.*

@Entity
class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?= null,
    @Column(nullable = false)
    val latitude: Double=0.0,
    @Column(nullable = false)
    val longitude: Double=0.0,
    @OneToOne
    @JoinColumn(name = "person_id")
    val person: Person? = null

    //validar se precisa usar @field nas validações
){
    constructor() : this(null,0.0,0.0,null)
}
