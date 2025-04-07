/**
 * A Data Transfer Object (DTO) for authentication requests.
 * This record encapsulates the user's credentials.
 *
 * @param usuario The username of the user attempting to authenticate.
 * @param senha   The password of the user attempting to authenticate.
 */
package com.montreal.acelera.blog_pessoal.dto.requests;

public record AuthDTO(
        String usuario,
        String senha) {
}
