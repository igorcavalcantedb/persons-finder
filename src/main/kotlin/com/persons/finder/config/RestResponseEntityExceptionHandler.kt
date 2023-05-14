package com.persons.finder.config

import com.persons.finder.exceptions.PersonNotFoundException
import com.persons.finder.presentation.dto.ErrorResponse
import com.persons.finder.presentation.dto.ValidationErrorDto
import com.persons.finder.presentation.dto.ValidationErrorDtoList
import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorDtoList> {
        val errors = ex.bindingResult.allErrors.map { error ->
            ValidationErrorDto(
                field = (error as FieldError).field,
                message = error.defaultMessage ?: "Invalid value"
            )
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidationErrorDtoList(errors))
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException::class)
    fun constraintViolationException(ex: ConstraintViolationException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "Resource not found", HttpStatus.NOT_FOUND.value())
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException::class)
    fun constraintJavaxViolationException(ex: javax.validation.ConstraintViolationException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "Invalid input parameter", HttpStatus.BAD_REQUEST.value())
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(PersonNotFoundException::class)
    fun handleMethodPersonNotFoundException(ex: PersonNotFoundException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "Person not found", HttpStatus.NOT_FOUND.value())
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }


}