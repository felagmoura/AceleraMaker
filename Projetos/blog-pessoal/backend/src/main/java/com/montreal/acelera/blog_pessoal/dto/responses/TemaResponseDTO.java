/**
 * A Data Transfer Object (DTO) for representing a response containing information about a "Tema".
 * This record is immutable and uses the builder pattern for instantiation.
 * 
 * @param id        The unique identifier of the "Tema".
 * @param descricao The description of the "Tema".
 * 
 * <p>
 * This DTO also provides a constructor that allows creating an instance from a {@link Tema} entity.
 * </p>
 */
package com.montreal.acelera.blog_pessoal.dto.responses;

import com.montreal.acelera.blog_pessoal.model.Tema;

import lombok.Builder;

@Builder
public record TemaResponseDTO(
        Long id,
        String descricao) {
    public TemaResponseDTO(Tema tema) {
        this(
                tema.getId(),
                tema.getDescricao());
    }
}
