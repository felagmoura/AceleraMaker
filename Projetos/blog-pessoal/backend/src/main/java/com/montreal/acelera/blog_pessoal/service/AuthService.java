package com.montreal.acelera.blog_pessoal.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.montreal.acelera.blog_pessoal.dto.requests.AuthDTO;
import com.montreal.acelera.blog_pessoal.dto.requests.UsuarioDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.AuthResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.model.Usuario;
import com.montreal.acelera.blog_pessoal.repository.UsuarioRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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
     * Registers a new user in the system and generates a JWT token for
     * authentication.
     *
     * @param usuarioDTO The data transfer object containing user information to be
     *                   registered. Must not be null.
     * @return An {@link AuthResponseDTO} containing the generated JWT token.
     * @throws ServiceException If an error occurs during user registration or token
     *                          generation.
     */
    public AuthResponseDTO signup(@NonNull UsuarioDTO usuarioDTO) throws ServiceException {
        try {
            System.out.println(usuarioDTO);
            Usuario usuario = usuarioRepository.save(toEntity(usuarioDTO));
            var jwtToken = jwtService.generateToken(usuario);
            return AuthResponseDTO.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            throw new ServiceException("Erro ao cadastrar o usuário", e);
        }
    }

    /**
     * Authenticates a user using the provided credentials and generates a JWT
     * token.
     *
     * @param authDTO The data transfer object containing user credentials for
     *                authentication. Must not be null.
     * @return An {@link AuthResponseDTO} containing the generated JWT token.
     * @throws ServiceException If an error occurs during authentication or token
     *                          generation.
     */
    public AuthResponseDTO signin(AuthDTO authDTO) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authDTO.usuario(), authDTO.senha()));
        var user = usuarioRepository.findByUsuario(authDTO.usuario()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

}