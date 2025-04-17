package com.montreal.acelera.blog_pessoal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.montreal.acelera.blog_pessoal.dto.requests.PostagemDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.PostagemResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.model.Postagem;
import com.montreal.acelera.blog_pessoal.repository.PostagemRepository;
import com.montreal.acelera.blog_pessoal.repository.TemaRepository;
import com.montreal.acelera.blog_pessoal.repository.UsuarioRepository;
import com.montreal.acelera.blog_pessoal.specifications.PostagemSpecifications;

import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostagemService {

    private final TemaRepository temaRepository;
    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Converts a PostagemDTO object to a Postagem entity.
     *
     * @param postagemDTO the PostagemDTO object containing the data to be converted
     * @return a Postagem entity built from the provided PostagemDTO
     * @throws RuntimeException if the user associated with the provided usuarioId
     *                          is not found or if the theme associated with the
     *                          provided temaId is not found
     */
    public Postagem toEntity(@NonNull PostagemDTO postagemDTO) {
        Postagem.PostagemBuilder builder = Postagem.builder()
                .titulo(postagemDTO.titulo())
                .texto(postagemDTO.texto())
                .usuario(usuarioRepository.findById(postagemDTO.usuarioId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado")))
                .data(postagemDTO.data());

        if (postagemDTO.temaId() != null) {
            builder.tema(temaRepository.findById(postagemDTO.temaId())
                    .orElseThrow(() -> new RuntimeException("Tema não encontrado")));
        }

        return builder.build();
    }

    /**
     * Creates a new Postagem in the system.
     *
     * @param postagemDTO The data transfer object containing the information for
     *                    the new Postagem.
     * @return A PostagemResponseDTO containing the created Postagem.
     * @throws RuntimeException if an error occurs while creating the Postagem.
     */
    public PostagemResponseDTO criarPostagem(@NonNull PostagemDTO postagemDTO) {
        try {
            return new PostagemResponseDTO(postagemRepository.save(toEntity(postagemDTO)));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar postagem", e);
        }
    }

    /**
     * Updates an existing Postagem in the system.
     *
     * @param postagemDTO The data transfer object containing the updated
     *                    information for the Postagem.
     * @param postagemId  The ID of the Postagem to be updated.
     * @return A PostagemResponseDTO containing the updated Postagem.
     * @throws ServiceException if an error occurs while updating the Postagem or if
     *                          the Postagem is not found.
     */
    public PostagemResponseDTO atualizarPostagem(@NonNull PostagemDTO postagemDTO, Long postagemId)
            throws ServiceException {
        try {
            Postagem postagem = postagemRepository.findById(postagemId)
                    .orElseThrow(() -> new ServiceException("Postagem não encontrada"));
            postagem.setTitulo(postagemDTO.titulo());
            postagem.setTexto(postagemDTO.texto());
            if (postagemDTO.temaId() != null) {
                postagem.setTema(temaRepository.findById(postagemDTO.temaId())
                        .orElseThrow(() -> new ServiceException("Tema não encontrado")));
            }

            return new PostagemResponseDTO(postagemRepository.save(postagem));
        } catch (Exception e) {
            throw new ServiceException("Erro ao atualizar postagem", e);
        }
    }

    /**
     * Deletes a Postagem from the system.
     *
     * @param postagemId The ID of the Postagem to be deleted.
     * @throws IllegalArgumentException if the Postagem is not found.
     * @throws ServiceException         if an error occurs while deleting the
     *                                  Postagem.
     */
    public void deletarPostagem(Long postagemId) throws IllegalArgumentException, ServiceException {
        try {
            Postagem postagem = postagemRepository.findById(postagemId)
                    .orElseThrow(() -> new IllegalArgumentException("Postagem não encontrada"));
            postagemRepository.delete(postagem);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Lists all Postagens in the system with pagination.
     *
     * @param paginate The pagination information.
     * @return A list of PostagemResponseDTO containing all Postagens.
     * @throws ServiceException if an error occurs while listing the Postagens.
     */
    public List<PostagemResponseDTO> listarTodasPostagens(Pageable paginate) throws ServiceException {
        try {
            return postagemRepository.findAll(paginate).stream()
                    .map(PostagemResponseDTO::new)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Erro ao listar postagens", e);
        }
    }

    /**
     * Retrieves a Postagem by its ID.
     *
     * @param postagemId The ID of the Postagem to be retrieved.
     * @return A PostagemResponseDTO containing the retrieved Postagem.
     * @throws ServiceException if an error occurs while retrieving the Postagem or
     *                          if the Postagem is not found.
     */
    public PostagemResponseDTO buscarPostagemPorId(Long postagemId) throws ServiceException {
        try {
            Postagem postagem = postagemRepository.findById(postagemId)
                    .orElseThrow(() -> new ServiceException("Postagem não encontrada"));
            return new PostagemResponseDTO(postagem);
        } catch (Exception e) {
            throw new ServiceException("Erro ao buscar postagem", e);
        }
    }

    /**
     * Filters Postagens based on the provided criteria.
     *
     * @param titulo         The title of the Postagem to filter by.
     * @param nomeUsuario    The name of the user to filter by.
     * @param usuarioUsuario The username to filter by.
     * @param descricaoTema  The theme description to filter by.
     * @return A list of PostagemResponseDTO containing the filtered Postagens.
     * @throws RuntimeException if an error occurs while filtering the Postagens.
     */
    public List<PostagemResponseDTO> filtrarPostagens(String titulo, String nomeUsuario, String usuarioUsuario,
            String descricaoTema) {
        try {
            Specification<Postagem> spec = Specification.where(null);

            if (titulo != null && !titulo.isEmpty()) {
                spec = spec.and(PostagemSpecifications.hasTitulo(titulo));
            }
            if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
                spec = spec.and(PostagemSpecifications.hasNomeUsuario(nomeUsuario));
            }
            if (usuarioUsuario != null && !usuarioUsuario.isEmpty()) {
                spec = spec.and(PostagemSpecifications.hasUsuarioUsuario(usuarioUsuario));
            }
            if (descricaoTema != null && !descricaoTema.isEmpty()) {
                spec = spec.and(PostagemSpecifications.hasTemaDescricao(descricaoTema));
            }

            return postagemRepository.findAll(spec).stream()
                    .map(PostagemResponseDTO::new)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao filtrar postagens", e);
        }
    }
}
