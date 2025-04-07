/**
 * Controller responsible for handling authentication-related operations such as
 * user registration and login.
 * 
 * <p>This controller provides endpoints for:
 * <ul>
 *   <li>Registering a new user</li>
 *   <li>Authenticating an existing user</li>
 * </ul>
 * </p>
 * 
 * <p>All endpoints return appropriate HTTP status codes and response bodies
 * based on the operation's success or failure.</p>
 * 
 * <p>Dependencies:
 * <ul>
 *   <li>{@link AuthService} - Service layer for authentication logic</li>
 * </ul>
 * </p>
 * 
 * <p>Exceptions:
 * <ul>
 *   <li>{@link ServiceException} - Thrown when a service-level error occurs</li>
 *   <li>{@link InvalidRequestException} - Thrown when the request is invalid</li>
 * </ul>
 * </p>
 */
package com.montreal.acelera.blog_pessoal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montreal.acelera.blog_pessoal.dto.requests.AuthDTO;
import com.montreal.acelera.blog_pessoal.dto.requests.UsuarioDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.AuthResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.InvalidRequestException;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user.
     * 
     * @param usuarioDTO The user data transfer object containing the user's
     *                   details.
     * @return A ResponseEntity containing the created AuthResponseDTO and an HTTP
     *         status of CREATED.
     * @throws ServiceException        If an error occurs during the registration of
     *                                 the user.
     * @throws InvalidRequestException If the provided request data is invalid.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UsuarioDTO usuarioDTO)
            throws ServiceException, InvalidRequestException {
        System.out.println(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(usuarioDTO));
    }

    /**
     * Authenticates an existing user.
     * 
     * @param authDTO The authentication data transfer object containing the user's
     *                credentials.
     * @return A ResponseEntity containing the AuthResponseDTO and an HTTP status of
     *         OK.
     * @throws ServiceException        If an error occurs during the authentication
     *                                 process.
     * @throws InvalidRequestException If the provided request data is invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDTO authDTO)
            throws ServiceException, InvalidRequestException {
        return ResponseEntity.ok(authService.signin(authDTO));
    }
}
