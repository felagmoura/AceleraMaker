/**
 * A Data Transfer Object (DTO) for representing a response containing details of a "Postagem" (Post).
 * This record is used to encapsulate the data of a post, including its associated user and theme.
 * 
 * @param id      The unique identifier of the post.
 * @param titulo  The title of the post.
 * @param texto   The content or body of the post.
 * @param data    The date and time when the post was created or last updated.
 * @param usuario The user who created the post, represented as a {@link UsuarioResponseDTO}.
 * @param tema    The theme associated with the post, represented as a {@link TemaResponseDTO}.
 * 
 * @see com.montreal.acelera.blog_pessoal.model.Postagem
 * @see UsuarioResponseDTO
 * @see TemaResponseDTO
 */
package com.montreal.acelera.blog_pessoal.dto.responses;

import com.montreal.acelera.blog_pessoal.model.Postagem;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostagemResponseDTO(
        Long id,
        String titulo,
        String texto,
        LocalDateTime data,
        UsuarioResponseDTO usuario,
        TemaResponseDTO tema) {

    public PostagemResponseDTO(Postagem postagem) {
        this(
                postagem.getId(),
                postagem.getTitulo(),
                postagem.getTexto(),
                postagem.getData(),
                new UsuarioResponseDTO(postagem.getUsuario()),
                postagem.getTema() == null ? null : new TemaResponseDTO(postagem.getTema()));
    }
}
