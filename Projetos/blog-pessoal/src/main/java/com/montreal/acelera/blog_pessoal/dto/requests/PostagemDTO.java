/**
 * Data Transfer Object (DTO) for representing a Postagem (Post) entity.
 * This class is used to transfer data between layers in a structured manner.
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li><b>titulo</b>: The title of the post. It is mandatory and must not exceed the maximum length defined in {@link Postagem#TITULO_LENGTH}.</li>
 *   <li><b>texto</b>: The content of the post. It is mandatory and must not exceed the maximum length defined in {@link Postagem#TEXTO_LENGTH}.</li>
 *   <li><b>usuarioId</b>: The ID of the user (author) who created the post. This field is mandatory.</li>
 *   <li><b>temaId</b>: The ID of the theme associated with the post. This field is optional.</li>
 *   <li><b>data</b>: The date and time when the post was created or last updated. This field is optional.</li>
 * </ul>
 * 
 * <p>Validation Constraints:</p>
 * <ul>
 *   <li><b>@NotBlank</b>: Ensures that the field is not null or empty (applies to <code>titulo</code> and <code>texto</code>).</li>
 *   <li><b>@Size</b>: Ensures that the field does not exceed the specified maximum length (applies to <code>titulo</code> and <code>texto</code>).</li>
 *   <li><b>@NotNull</b>: Ensures that the field is not null (applies to <code>usuarioId</code>).</li>
 * </ul>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li><b>@Builder</b>: Enables the builder pattern for creating instances of this record.</li>
 * </ul>
 */
package com.montreal.acelera.blog_pessoal.dto.requests;

import java.time.LocalDateTime;

import com.montreal.acelera.blog_pessoal.model.Postagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PostagemDTO(
		@NotBlank(message = "Título é obrigatório") @Size(max = Postagem.TITULO_LENGTH, message = "Título deve ter no máximo {max} caracteres") String titulo,

		@NotBlank(message = "Texto é obrigatório") @Size(max = Postagem.TEXTO_LENGTH, message = "Texto deve ter no máximo {max} caracteres") String texto,

		@NotNull(message = "Autor é obrigatório") Long usuarioId,

		Long temaId,

		LocalDateTime data) {

}
