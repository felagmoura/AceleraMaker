/**
 * Data Transfer Object (DTO) for Tema entity.
 * This class is used to transfer data related to Tema between different layers of the application.
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li><b>descricao</b>: A mandatory field representing the description of the Tema. 
 *       It must not be blank and should have a maximum length of 255 characters.</li>
 * </ul>
 * 
 * <p>Validation:</p>
 * <ul>
 *   <li><b>@NotBlank</b>: Ensures that the descricao field is not null or empty.</li>
 *   <li><b>@Size</b>: Restricts the length of descricao to a maximum of 255 characters.</li>
 * </ul>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li><b>@Builder</b>: Enables the builder pattern for creating instances of TemaDTO.</li>
 * </ul>
 */
package com.montreal.acelera.blog_pessoal.dto.requests;

import com.montreal.acelera.blog_pessoal.model.Tema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TemaDTO(
		@NotBlank(message = "Descrição é obrigatória") @Size(max = Tema.LENGTH, message = "Descrição deve ter no máximo 255 caracteres") String descricao) {
}
