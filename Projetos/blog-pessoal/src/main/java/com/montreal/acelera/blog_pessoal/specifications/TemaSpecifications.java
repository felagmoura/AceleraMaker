/**
 * Specification for filtering Tema entities by their descricao field.
 * This method creates a JPA Specification that checks if the descricao
 * field of a Tema entity matches the given string.
 *
 * @param descricao The string to match against the descricao field.
 * @return A Specification for filtering Tema entities by descricao.
 */
package com.montreal.acelera.blog_pessoal.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.montreal.acelera.blog_pessoal.model.Tema;

public class TemaSpecifications {
    public static Specification<Tema> hasDescricao (String descricao) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.<String>get("descricao"), "%" + descricao + "%");
    }
}
