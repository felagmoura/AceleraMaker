/**
 * Controller for managing "Postagem" (Posts) resources.
 * Provides endpoints for creating, updating, deleting, and retrieving posts.
 * 
 * Endpoints:
 * - POST /api/postagens/criar: Create a new post.
 * - PUT /api/postagens/{id}: Update an existing post by ID.
 * - DELETE /api/postagens/{id}: Delete a post by ID.
 * - GET /api/postagens/todas: Retrieve all posts with optional pagination and sorting.
 * - GET /api/postagens/filtrar: Filter posts based on various criteria.
 * 
 * Dependencies:
 * - PostagemService: Service layer for handling business logic related to posts.
 * 
 * Annotations:
 * - @RestController: Marks this class as a REST controller.
 * - @RequestMapping("/api/postagens"): Maps all endpoints to the base URL "/api/postagens".
 * - @RequiredArgsConstructor: Generates a constructor for final fields (dependency injection).
 * 
 * Exceptions:
 * - ServiceException: Thrown for service layer errors.
 * - InvalidRequestException: Thrown for invalid requests.
 * - NotFoundException: Thrown when a resource is not found.
 * - IllegalArgumentException: Thrown for invalid arguments.
 */
package com.montreal.acelera.blog_pessoal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montreal.acelera.blog_pessoal.dto.requests.PostagemDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.PostagemResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.InvalidRequestException;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.service.PostagemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/postagens")
@RequiredArgsConstructor
public class PostagemController extends BaseController {

    private final PostagemService postagemService;

    /**
     * Creates a new post.
     * 
     * @param postagemDTO The post data to create.
     * @return The created post.
     * @throws ServiceException        If there is a service-level error.
     * @throws InvalidRequestException If the request is invalid.
     */
    @PostMapping("/criar")
    public ResponseEntity<PostagemResponseDTO> criarPostagem(@RequestBody @Valid PostagemDTO postagemDTO)
            throws ServiceException, InvalidRequestException {
        System.out.println(postagemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(postagemService.criarPostagem(postagemDTO));
    }

    /**
     * Updates an existing post by its ID.
     * 
     * @param postagemDTO The updated post data.
     * @param id          The ID of the post to update.
     * @return The updated post.
     * @throws ServiceException        If there is a service-level error.
     * @throws InvalidRequestException If the request is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> atualizarPostagem(@RequestBody @Valid PostagemDTO postagemDTO,
            @PathVariable Long id)
            throws ServiceException, InvalidRequestException {
        return ResponseEntity.ok(postagemService.atualizarPostagem(postagemDTO, id));
    }

    /**
     * Deletes a post by its ID.
     * 
     * @param id The ID of the post to delete.
     * @return A response entity with no content.
     * @throws ServiceException         If there is a service-level error.
     * @throws InvalidRequestException  If the request is invalid.
     * @throws IllegalArgumentException If the ID is invalid.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id)
            throws ServiceException, InvalidRequestException, IllegalArgumentException {
        postagemService.deletarPostagem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all posts with optional pagination and sorting.
     * 
     * @param per_page The number of items per page (optional).
     * @param sort     The sorting criteria (optional).
     * @param page     The page number to retrieve (optional).
     * @return A list of all posts.
     * @throws ServiceException  If there is a service-level error.
     * @throws NotFoundException If no posts are found.
     */
    @GetMapping("/todas")
    public ResponseEntity<List<PostagemResponseDTO>> listarTodasPostagens(
            @RequestParam(required = false) Integer per_page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer page) throws ServiceException, NotFoundException {
        return ResponseEntity.ok(postagemService.listarTodasPostagens(paginate(page, per_page, sort)));
    }

    /**
     * Filters posts based on various criteria.
     * 
     * @param titulo         The title of the post (optional).
     * @param nomeUsuario    The name of the user (optional).
     * @param usuarioUsuario The username of the user (optional).
     * @param descricaoTema  The description of the theme (optional).
     * @return A list of filtered posts.
     * @throws ServiceException  If there is a service-level error.
     * @throws NotFoundException If no posts are found matching the criteria.
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<PostagemResponseDTO>> filtarPostagens(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String nomeUsuario,
            @RequestParam(required = false) String usuarioUsuario,
            @RequestParam(required = false) String descricaoTema) throws ServiceException, NotFoundException {
        return ResponseEntity.ok(postagemService.filtrarPostagens(titulo, nomeUsuario, usuarioUsuario, descricaoTema));
    }

}
