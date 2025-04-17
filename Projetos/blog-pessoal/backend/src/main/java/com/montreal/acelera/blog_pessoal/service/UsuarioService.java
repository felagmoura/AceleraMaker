package com.montreal.acelera.blog_pessoal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.montreal.acelera.blog_pessoal.dto.requests.UsuarioDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.UsuarioResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.model.Usuario;
import com.montreal.acelera.blog_pessoal.repository.UsuarioRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Converts a {@link UsuarioDTO} object to a {@link Usuario} entity.
     *
     * @param usuarioDTO the data transfer object containing user information; must
     *                   not be null.
     * @return a {@link Usuario} entity built from the provided {@link UsuarioDTO}.
     *         The password is encoded using the configured {@code passwordEncoder}.
     */
    public Usuario toEntity(@NonNull UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.nome())
                .usuario(usuarioDTO.usuario())
                .senha(passwordEncoder.encode(usuarioDTO.senha()))
                .foto(usuarioDTO.foto())
                .build();
    }

    /**
     * Registers a new user in the system.
     *
     * @param usuarioDTO The data transfer object containing user information to be
     *                   registered. Must not be null.
     * @return A {@link UsuarioResponseDTO} containing the created user.
     * @throws ServiceException If an error occurs during user registration.
     */
    public UsuarioResponseDTO cadastrarUsuario(@NonNull UsuarioDTO usuarioDTO) throws ServiceException {
        try {
            return new UsuarioResponseDTO(usuarioRepository.save(toEntity(usuarioDTO)));
        } catch (Exception e) {
            throw new ServiceException("Erro ao cadastrar o usuário", e);
        }
    }

    /**
     * Updates an existing user in the system.
     *
     * @param usuarioDTO The data transfer object containing the updated user
     *                   information. Must not be null.
     * @param usuarioId  The ID of the user to be updated.
     * @return A {@link UsuarioResponseDTO} containing the updated user.
     * @throws ServiceException If an error occurs during user update.
     */
    public UsuarioResponseDTO atualizarUsuario(@NonNull UsuarioDTO usuarioDTO, Long usuarioId) throws ServiceException {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ServiceException("Usuário não encontrado"));
            usuario.setNome(usuarioDTO.nome());
            usuario.setUsuario(usuarioDTO.usuario());
            usuario.setFoto(usuarioDTO.foto());

            return new UsuarioResponseDTO(usuarioRepository.save(usuario));
        } catch (Exception e) {
            throw new ServiceException("Erro ao alterar o usuário", e);
        }
    }

    /**
     * Deletes a user from the system.
     *
     * @param id The ID of the user to be deleted.
     * @throws IllegalArgumentException If the user is not found.
     * @throws ServiceException         If an error occurs during user deletion.
     */
    public void deletarUsuario(Long id) throws IllegalArgumentException, ServiceException {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            usuarioRepository.delete(usuario);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param usuarioId The ID of the user to be retrieved.
     * @return A {@link UsuarioResponseDTO} containing the user information.
     * @throws ServiceException If an error occurs during user retrieval.
     */
    public UsuarioResponseDTO buscarUsuarioPorId(Long usuarioId) throws ServiceException {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ServiceException("Usuário não encontrado"));
            return new UsuarioResponseDTO(usuario);
        } catch (Exception e) {
            throw new ServiceException("Erro ao buscar o usuário", e);
        }
    }

    /**
     * Retrieves a user by their name.
     *
     * @param nome The name of the user to be retrieved.
     * @return A {@link UsuarioResponseDTO} containing the user information.
     * @throws ServiceException If an error occurs during user retrieval.
     */
    public UsuarioResponseDTO buscarUsuarioPorNome(String nome) throws ServiceException {
        try {
            Usuario usuario = usuarioRepository.findByNome(nome)
                    .orElseThrow(() -> new ServiceException("Usuário não encontrado"));
            return new UsuarioResponseDTO(usuario);
        } catch (Exception e) {
            throw new ServiceException("Erro ao buscar o usuário", e);
        }
    }

    /**
     * Retrieves a user by their username.
     *
     * @param usuario The username of the user to be retrieved.
     * @return A {@link UsuarioResponseDTO} containing the user information.
     * @throws ServiceException If an error occurs during user retrieval.
     */
    public UsuarioResponseDTO buscarUsuarioPorUsuario(String usuario) throws ServiceException {
        try {
            Usuario usuarioEncontrado = usuarioRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new ServiceException("Usuário não encontrado"));
            return new UsuarioResponseDTO(usuarioEncontrado);
        } catch (Exception e) {
            throw new ServiceException("Erro ao buscar o usuário", e);
        }
    }

    /**
     * Lists all users in the system with pagination.
     *
     * @param pageable The pagination information.
     * @return A list of {@link UsuarioResponseDTO} containing all users.
     * @throws ServiceException If an error occurs during user listing.
     */
    public List<UsuarioResponseDTO> listarTodosUsuarios(Pageable pageable) throws ServiceException {
        try {
            return usuarioRepository.findAll(pageable).stream()
                    .map(UsuarioResponseDTO::new)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Erro ao listar os usuários", e);
        }
    }

}
