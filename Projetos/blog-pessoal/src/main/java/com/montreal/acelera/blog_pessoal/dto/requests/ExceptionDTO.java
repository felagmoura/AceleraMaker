/**
 * A Data Transfer Object (DTO) for representing exceptions in the application.
 * This record encapsulates the HTTP status and an optional message describing the exception.
 *
 * @param status  The HTTP status code associated with the exception. Must not be null.
 * @param message An optional message providing additional details about the exception.
 */
package com.montreal.acelera.blog_pessoal.dto.requests;

import org.springframework.http.HttpStatus;

import lombok.NonNull;

public record ExceptionDTO(
                @NonNull HttpStatus status,
                String message) {
}