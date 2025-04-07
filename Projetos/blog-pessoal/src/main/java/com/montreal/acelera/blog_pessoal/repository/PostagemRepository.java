/**
 * Repository interface for managing Postagem entities.
 * Extends JpaRepository to provide basic CRUD operations and JpaSpecificationExecutor
 * for advanced query capabilities.
 * 
 * Methods:
 * - findByTema: Retrieves a paginated list of Postagem entities associated with a specific Tema.
 * - findByUsuario: Retrieves a paginated list of Postagem entities associated with a specific Usuario.
 * 
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 */
package com.montreal.acelera.blog_pessoal.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.montreal.acelera.blog_pessoal.model.Postagem;
import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.model.Usuario;

public interface PostagemRepository extends JpaRepository<Postagem, Long>, JpaSpecificationExecutor<Postagem> {
	List<Postagem> findByTema(Tema tema, Pageable pageable);

	List<Postagem> findByUsuario(Usuario usuario, Pageable pageable);
}
