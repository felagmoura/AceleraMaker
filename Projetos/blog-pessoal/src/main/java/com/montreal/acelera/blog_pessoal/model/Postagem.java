/**
 * This class represents the model for a blog post (Postagem).
 * It is part of the package com.montreal.acelera.blog_pessoal.model.
 * 
 * The class is likely used to define the structure and behavior of a blog post
 * entity in the application.
 */
package com.montreal.acelera.blog_pessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_postagens")
public class Postagem {
	static public final int TITULO_LENGTH = 100;
	static public final int TEXTO_LENGTH = 500;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = TITULO_LENGTH)
	@Column(nullable = false)
	private String titulo;

	@NotBlank
	@Size(max = TEXTO_LENGTH)
	@Column(nullable = false)
	private String texto;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime data;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tema_id", nullable = true)
	private Tema tema;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
}
