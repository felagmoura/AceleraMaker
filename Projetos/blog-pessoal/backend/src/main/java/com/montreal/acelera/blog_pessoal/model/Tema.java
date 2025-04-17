/**
 * This class represents the Tema (Theme) entity in the blog application.
 * It is part of the model layer and is used to define the structure of
 * the Tema object, which may include attributes and relationships with
 * other entities.
 *
 * Package: com.montreal.acelera.blog_pessoal.model
 */
package com.montreal.acelera.blog_pessoal.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "tb_temas")
public class Tema {
	static public final int LENGTH = 255;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = LENGTH)
	@Column(nullable = false)
	private String descricao;

	@OneToMany(mappedBy = "tema", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Postagem> postagens;
}
