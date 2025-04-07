/**
 * Repository interface for managing Tema entities in the database.
 * This interface extends JpaRepository, providing CRUD operations
 * and additional query methods for the Tema entity.
 *
 * Package: com.montreal.acelera.blog_pessoal.repository
 */
package com.montreal.acelera.blog_pessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.montreal.acelera.blog_pessoal.model.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long>, JpaSpecificationExecutor<Tema> {
	Optional<Tema> findByDescricao(String descricao);
}
