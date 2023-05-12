package com.persons.finder.config

import com.persons.finder.presentation.dto.ErrorResponse
import com.persons.finder.presentation.dto.ValidationErrorDto
import com.persons.finder.presentation.dto.ValidationErrorDtoList
import org.hibernate.exception.ConstraintViolationException
import org.springframework.data.crossstore.ChangeSetPersister
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

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleMethodArgumentNotValidException(ex: ConstraintViolationException): ResponseEntity<String> {

        //todo improve error messages
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(value = [(ChangeSetPersister.NotFoundException::class)])
    fun handleNotFoundException(ex: ChangeSetPersister.NotFoundException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "Resource not found", HttpStatus.NOT_FOUND.value())
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

}