package com.persons.finder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@SpringBootApplication
class ApplicationStarter

fun main(args: Array<String>) {
	runApplication<ApplicationStarter>(*args)
}
