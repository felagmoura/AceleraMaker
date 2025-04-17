/**
 * Data Transfer Object (DTO) representing the response for authentication requests.
 * This class contains the authentication token and its expiration date.
 * 
 * <p>
 * Annotations:
 * <ul>
 *   <li>{@code @Data} - Generates getters, setters, toString, equals, and hashCode methods.</li>
 *   <li>{@code @Builder} - Enables the builder pattern for object creation.</li>
 *   <li>{@code @AllArgsConstructor} - Generates a constructor with all fields as parameters.</li>
 *   <li>{@code @NoArgsConstructor} - Generates a no-argument constructor.</li>
 * </ul>
 * </p>
 * 
 * Fields:
 * <ul>
 *   <li>{@code token} - The authentication token as a {@code String}.</li>
 *   <li>{@code expiration} - The expiration date of the token as a {@code Date}.</li>
 * </ul>
 */
package com.montreal.acelera.blog_pessoal.dto.responses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Date expiration;
}