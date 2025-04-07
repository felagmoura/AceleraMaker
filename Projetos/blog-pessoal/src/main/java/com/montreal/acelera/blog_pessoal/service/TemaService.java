package com.montreal.acelera.blog_pessoal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.montreal.acelera.blog_pessoal.dto.requests.TemaDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.TemaResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;

import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.repository.TemaRepository;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemaService {

    private final TemaRepository temaRepository;

    /**
     * Converts a {@link TemaDTO} object to a {@link Tema} entity.
     *
     * @param temaDTO the data transfer object containing theme information; must
     *                not be null.
     * @return a {@link Tema} entity built from the provided {@link TemaDTO}.
     */
    public Tema toEntity(@NonNull TemaDTO temaDTO) {
        return Tema.builder().descricao(temaDTO.descricao()).build();
    }

    /**
     * Creates a new theme in the system.
     *
     * @param temaDTO The data transfer object containing the information for the
     *                new theme.
     * @return A {@link TemaResponseDTO} containing the created theme.
     * @throws ServiceException If an error occurs while creating the theme.
     */
    public TemaResponseDTO criarTema(@NonNull TemaDTO temaDTO) throws ServiceException {
        try {
            return new TemaResponseDTO(temaRepository.save(toEntity(temaDTO)));
        } catch (Exception e) {
            throw new ServiceException("Erro ao cadastrar o tema", e);
        }
    }

    /**
     * Updates an existing theme in the system.
     *
     * @param temaDTO The data transfer object containing the updated information
     *                for the theme.
     * @param temaId  The ID of the theme to be updated.
     * @return A {@link TemaResponseDTO} containing the updated theme.
     * @throws ServiceException If an error occurs while updating the theme.
     */
    public TemaResponseDTO atualizarTema(@NonNull TemaDTO temaDTO, Long temaId) throws ServiceException {
        try {
            Tema tema = temaRepository.findById(temaId)
                    .orElseThrow(() -> new ServiceException("Tema não encontrado"));
            tema.setDescricao(temaDTO.descricao());

            return new TemaResponseDTO(temaRepository.save(tema));
        } catch (Exception e) {
            throw new ServiceException("Erro ao alterar o tema", e);
        }
    }

    /**
     * Deletes a theme from the system.
     *
     * @param temaId The ID of the theme to be deleted.
     * @throws IllegalArgumentException If the theme is not found.
     * @throws ServiceException         If an error occurs while deleting the theme.
     */
    public void deletarTema(Long temaId) throws IllegalArgumentException, ServiceException {
        try {
            Tema tema = temaRepository.findById(temaId)
                    .orElseThrow(() -> new IllegalArgumentException("Tema não encontrado"));
            temaRepository.delete(tema);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Lists all themes in the system with pagination support.
     *
     * @param pageable The pagination information.
     * @return A list of {@link TemaResponseDTO} containing all themes.
     * @throws ServiceException If an error occurs while listing the themes.
     */
    public List<TemaResponseDTO> listarTodosTemas(Pageable pageable) throws ServiceException {
        try {
            return temaRepository.findAll(pageable).stream()
                    .map(TemaResponseDTO::new)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Erro ao listar os temas", e);
        }
    }
}
