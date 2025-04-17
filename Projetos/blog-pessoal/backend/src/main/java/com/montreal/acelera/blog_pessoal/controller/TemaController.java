/**
 * Controller class for managing "Tema" resources.
 * Provides endpoints for creating, updating, deleting, and retrieving "Tema" entities.
 * 
 * <p>Base URL: /api/tema</p>
 * 
 * <p>Endpoints:</p>
 * <ul>
 *   <li><b>POST /criar</b>: Creates a new "Tema".</li>
 *   <li><b>PUT /{id}</b>: Updates an existing "Tema" by its ID.</li>
 *   <li><b>DELETE /{id}</b>: Deletes a "Tema" by its ID.</li>
 *   <li><b>GET</b>: Retrieves a list of all "Tema" entities with optional pagination and sorting.</li>
 * </ul>
 * 
 * <p>Exceptions:</p>
 * <ul>
 *   <li>{@link com.montreal.acelera.blog_pessoal.exeception.ServiceException}</li>
 *   <li>{@link com.montreal.acelera.blog_pessoal.exeception.InvalidRequestException}</li>
 *   <li>{@link org.springframework.data.crossstore.ChangeSetPersister.NotFoundException}</li>
 *   <li>{@link java.lang.IllegalArgumentException}</li>
 * </ul>
 * 
 * <p>Dependencies:</p>
 * <ul>
 *   <li>{@link com.montreal.acelera.blog_pessoal.service.TemaService}</li>
 * </ul>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>{@link org.springframework.web.bind.annotation.RestController}</li>
 *   <li>{@link org.springframework.web.bind.annotation.RequestMapping}</li>
 *   <li>{@link lombok.RequiredArgsConstructor}</li>
 * </ul>
 */
package com.montreal.acelera.blog_pessoal.controller;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.montreal.acelera.blog_pessoal.dto.requests.TemaDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.TemaResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.InvalidRequestException;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.service.TemaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tema")
@RequiredArgsConstructor
public class TemaController extends BaseController {

    private final TemaService temaService;

    /**
     * Creates a new theme based on the provided TemaDTO.
     *
     * @param temaDTO The data transfer object containing the details of the theme
     *                to be created.
     * @return A ResponseEntity containing the created TemaResponseDTO and an HTTP
     *         status of CREATED.
     * @throws ServiceException        If an error occurs during the creation of the
     *                                 theme.
     * @throws InvalidRequestException If the provided request data is invalid.
     */
    @PostMapping("/criar")
    public ResponseEntity<TemaResponseDTO> criarTema(@RequestBody @Valid TemaDTO temaDTO)
            throws ServiceException, InvalidRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.criarTema(temaDTO));
    }

    /**
     * Updates an existing theme based on the provided TemaDTO and theme ID.
     *
     * @param temaDTO The data transfer object containing the updated details of the
     *                theme.
     * @param id      The ID of the theme to be updated.
     * @return A ResponseEntity containing the updated TemaResponseDTO.
     * @throws ServiceException        If an error occurs during the update of the
     *                                 theme.
     * @throws InvalidRequestException If the provided request data is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TemaResponseDTO> autualizarTema(@RequestBody @Valid TemaDTO temaDTO, @PathVariable Long id)
            throws ServiceException, InvalidRequestException {
        return ResponseEntity.ok(temaService.atualizarTema(temaDTO, id));
    }

    /**
     * Deletes a theme based on the provided theme ID.
     *
     * @param id The ID of the theme to be deleted.
     * @return A ResponseEntity with an HTTP status of NO_CONTENT.
     * @throws ServiceException         If an error occurs during the deletion of
     *                                  the theme.
     * @throws InvalidRequestException  If the provided request data is invalid.
     * @throws IllegalArgumentException If the provided ID is invalid.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTema(@PathVariable Long id)
            throws ServiceException, InvalidRequestException, IllegalArgumentException {
        temaService.deletarTema(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a list of all themes with optional pagination and sorting.
     *
     * @param per_page The number of items per page (optional).
     * @param sort     The sorting criteria (optional).
     * @param page     The page number to retrieve (optional).
     * @return A ResponseEntity containing a list of TemaResponseDTO objects.
     * @throws ServiceException  If an error occurs during the retrieval of themes.
     * @throws NotFoundException If no themes are found.
     */
    @GetMapping
    public ResponseEntity<List<TemaResponseDTO>> listarTodosTemas(
            @RequestParam(required = false) Integer per_page,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer page) throws ServiceException, NotFoundException {
        return ResponseEntity.ok(temaService.listarTodosTemas(paginate(page, per_page, sort)));
    }
}
