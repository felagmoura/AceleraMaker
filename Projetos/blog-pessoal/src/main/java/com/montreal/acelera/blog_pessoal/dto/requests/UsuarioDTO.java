/**
 * Data Transfer Object (DTO) for representing a user in the system.
 * This class is used to validate and transfer user data between layers.
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li><b>nome</b>: The name of the user. Must be between 2 and 255 characters, 
 *   contain only letters, and cannot be blank.</li>
 *   <li><b>usuario</b>: The username of the user. Must be at most 255 characters, 
 *   contain only letters, numbers, or the characters '.', '_', and '-', and cannot be blank.</li>
 *   <li><b>senha</b>: The password of the user. Must be between 8 and 72 characters, 
 *   contain at least one uppercase letter, one number, and one special character.</li>
 *   <li><b>foto</b>: The URL of the user's photo. Must not exceed the maximum length defined in 
 *   {@link Usuario#FOTO_LENGTH}.</li>
 * </ul>
 * 
 * <p>Validation annotations are used to enforce these constraints.</p>
 * 
 * <p>Note: The {@link Usuario#LENGTH} constant is used to define the maximum length for 
 * the "nome" and "usuario" fields.</p>
 */
package com.montreal.acelera.blog_pessoal.dto.requests;

import com.montreal.acelera.blog_pessoal.model.Usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import jakarta.validation.constraints.Pattern;

@Builder
public record UsuarioDTO(
		@NotBlank(message = "Nome é obrigatório") @Size(min = 2, max = Usuario.LENGTH, message = "Nome deve ter entre 2-255 caracteres") @Pattern(regexp = "^[\\p{L} ]+$", message = "Nome deve conter apenas letras") String nome,

		@NotBlank(message = "Usuário é obrigatório") @Size(max = Usuario.LENGTH, message = "Usuário deve ter no máximo 255 caracteres") @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Usuário deve conter apenas letras, números ou ._-") String usuario,

		@Size(min = 8, max = 72, message = "Senha deve ter 8-72 caracteres") @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).*$", message = "Deve conter pelo menos 1 maiúscula, 1 número e 1 especial") String senha,

		@Size(max = Usuario.FOTO_LENGTH, message = "URL da foto muito longa") String foto) {
}
