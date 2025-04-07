/**
 * This class contains static methods that define specifications for querying
 * the `Postagem` entity using Spring Data JPA's `Specification` interface.
 * These specifications allow filtering based on various attributes of the
 * `Postagem` entity and its related entities (`Usuario` and `Tema`).
 */
 
/**
 * Creates a specification to filter `Postagem` entities by their `titulo` attribute.
 *
 * @param titulo The title to filter by. Supports partial matching.
 * @return A `Specification` for filtering by `titulo`.
 */
 
/**
 * Creates a specification to filter `Postagem` entities by their `texto` attribute.
 *
 * @param texto The text to filter by. Supports partial matching.
 * @return A `Specification` for filtering by `texto`.
 */
 
/**
 * Creates a specification to filter `Postagem` entities by the `nome` attribute
 * of the associated `Usuario` entity.
 *
 * @param nomeUsuario The name of the user to filter by. Supports partial matching.
 *                     If null or empty, no filtering is applied.
 * @return A `Specification` for filtering by the user's name.
 */
 
/**
 * Creates a specification to filter `Postagem` entities by the `usuario` attribute
 * of the associated `Usuario` entity.
 *
 * @param usuarioUsuario The username of the user to filter by. Supports partial matching.
 *                        If null or empty, no filtering is applied.
 * @return A `Specification` for filtering by the user's username.
 */
 
/**
 * Creates a specification to filter `Postagem` entities by the `descricao` attribute
 * of the associated `Tema` entity.
 *
 * @param descricao The description of the theme to filter by. Supports partial matching.
 *                   If null or empty, no filtering is applied.
 * @return A `Specification` for filtering by the theme's description.
 */
package com.montreal.acelera.blog_pessoal.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.montreal.acelera.blog_pessoal.model.Postagem;
import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.model.Usuario;

import jakarta.persistence.criteria.Join;


public class PostagemSpecifications {
    public static Specification<Postagem> hasTitulo(String titulo) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(root.get("titulo"), "%" + titulo + "%");
    }
    public static Specification<Postagem> hasTexto(String texto) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(root.get("texto"), "%" + texto + "%");
    }
    
    public static Specification<Postagem> hasNomeUsuario (String nomeUsuario) {
        return (root, query, criteriaBuilder) -> {
            if (nomeUsuario == null || nomeUsuario.isEmpty()) return null;
            Join<Postagem, Usuario> usuarioJoin = root.join("usuario");
            return criteriaBuilder.like(usuarioJoin.get("nome"), "%" + nomeUsuario + "%");
        };
    }

    public static Specification<Postagem> hasUsuarioUsuario (String usuarioUsuario) {
        return (root, query, criteriaBuilder) -> {
            if (usuarioUsuario == null || usuarioUsuario.isEmpty()) return null;
            Join<Postagem, Usuario> usuarioJoin = root.join("usuario");
            return criteriaBuilder.like(usuarioJoin.get("usuario"), "%" + usuarioUsuario + "%");
        };
    }

    public static Specification<Postagem> hasTemaDescricao (String descricao) {
        return (root, query, criteriaBuilder) -> {
            if (descricao == null || descricao.isEmpty()) return null;
            Join<Postagem, Tema> temaJoin = root.join("tema");
            return criteriaBuilder.like(temaJoin.get("descricao"), "%" + descricao + "%");
        };
    }
}
