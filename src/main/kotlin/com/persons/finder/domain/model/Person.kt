package com.persons.finder.domain.model
import javax.persistence.*

@Entity
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null, // usar um UUID ?? talvez melhor colocar uma nova coluna com UUID
    @Column(nullable = false)
    var name: String ="",
    @Column(nullable = false, unique = true)
    var email: String =""


){
    constructor():this(null,"","")
}
