/**
 * This class contains specifications for querying the Usuario entity
 * using the Spring Data JPA Specification API.
 */
package com.montreal.acelera.blog_pessoal.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.montreal.acelera.blog_pessoal.model.Usuario;

public class UsuarioSpecifications {
    public static Specification<Usuario> hasNome (String nome) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.<String>get("nome"), "%" + nome + "%");
    }

    public static Specification<Usuario> hasUsuario (String usuario) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.<String>get("usuario"), "%" + usuario + "%");
    }
}
