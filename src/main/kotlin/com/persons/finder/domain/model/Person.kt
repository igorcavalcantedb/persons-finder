package com.persons.finder.domain.model
import javax.persistence.*

@Entity
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var name: String ="",
    @Column(nullable = false, unique = true)
    var email: String ="",



){
    constructor():this(null,"","")
    constructor(id: Long?):this(id,"","")
}
