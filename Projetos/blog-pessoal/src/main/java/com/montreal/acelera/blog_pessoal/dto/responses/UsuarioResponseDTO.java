/**
 * A Data Transfer Object (DTO) for representing a user response.
 * This class is implemented as a Java record and is used to encapsulate
 * user-related data to be sent as a response in the application.
 * 
 * @param id      The unique identifier of the user.
 * @param nome    The name of the user.
 * @param usuario The username of the user.
 * @param foto    The URL or path to the user's profile picture.
 * 
 * <p>
 * This DTO also provides a constructor that accepts a {@link Usuario} object
 * and maps its fields to the corresponding fields in this DTO.
 * </p>
 */
package com.montreal.acelera.blog_pessoal.dto.responses;

import com.montreal.acelera.blog_pessoal.model.Usuario;

import lombok.Builder;

@Builder
public record UsuarioResponseDTO(
        Long id,
        String nome,
        String usuario,
        String foto) {

    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getUsuario(),
                usuario.getFoto());
    }
}
