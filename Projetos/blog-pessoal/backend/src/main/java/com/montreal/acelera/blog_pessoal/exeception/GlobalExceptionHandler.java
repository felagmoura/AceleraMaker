/**
 * GlobalExceptionHandler is a centralized exception handling class annotated with
 * @ControllerAdvice to handle exceptions across the whole application.
 * It provides specific methods to handle various types of exceptions and return
 * appropriate HTTP responses with custom error messages encapsulated in ExceptionDTO objects.
 * 
 * Exception Handlers:
 * 
 * - @ExceptionHandler(IllegalArgumentException.class)
 *   Handles IllegalArgumentException and returns a 404 NOT_FOUND response.
 * 
 * - @ExceptionHandler(Forbidden.class)
 *   Handles Forbidden exceptions and returns a 403 FORBIDDEN response.
 * 
 * - @ExceptionHandler(RuntimeException.class)
 *   Handles RuntimeException and returns a 400 BAD_REQUEST response.
 * 
 * - @ExceptionHandler(DataIntegrityViolationException.class)
 *   Handles DataIntegrityViolationException and returns a 400 BAD_REQUEST response.
 * 
 * - @ExceptionHandler(InvalidRequestException.class)
 *   Handles InvalidRequestException, logs the stack trace, and returns a 422 UNPROCESSABLE_ENTITY response.
 * 
 * - @ExceptionHandler(ServiceException.class)
 *   Handles ServiceException, logs the stack trace, and returns a 422 UNPROCESSABLE_ENTITY response.
 * 
 * - @ExceptionHandler(Exception.class)
 *   Handles generic Exception and returns a 502 BAD_GATEWAY response.
 * 
 * - @ExceptionHandler(MethodArgumentNotValidException.class)
 *   Handles MethodArgumentNotValidException and returns a 400 BAD_REQUEST response.
 * 
 * Each handler method constructs an ExceptionDTO object containing the HTTP status
 * and the exception message, which is then returned in the ResponseEntity.
 */
package com.montreal.acelera.blog_pessoal.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.montreal.acelera.blog_pessoal.dto.requests.ExceptionDTO;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDTO> NotFoundException(IllegalArgumentException e) {
        new ExceptionDTO(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDTO(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<ExceptionDTO> ForbiddenException(Forbidden e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionDTO(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> RuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> DataIntegrityViolationException(DataIntegrityViolationException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionDTO> InvalidRequestException(InvalidRequestException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ExceptionDTO(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionDTO> ServiceException(ServiceException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ExceptionDTO(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> Exception(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new ExceptionDTO(HttpStatus.BAD_GATEWAY, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> MethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}
