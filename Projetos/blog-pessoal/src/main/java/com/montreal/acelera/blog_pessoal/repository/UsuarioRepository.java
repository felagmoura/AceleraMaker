/**
 * This package contains the repository interface for managing user-related
 * data in the Blog Pessoal application. The repository is responsible for
 * providing data access methods to interact with the underlying database
 * for user entities.
 */
package com.montreal.acelera.blog_pessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.montreal.acelera.blog_pessoal.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findByNome(String usuario);

    boolean existsByUsuario(String usuario);
}
