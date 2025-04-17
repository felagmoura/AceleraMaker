package com.montreal.acelera.blog_pessoal.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.montreal.acelera.blog_pessoal.dto.requests.TemaDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.TemaResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.repository.TemaRepository;


class TemaServiceTest {

    @InjectMocks
    private TemaService temaService;

    @Mock
    private TemaRepository temaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarTema() throws ServiceException {
        TemaDTO temaDTO = new TemaDTO("Descrição do Tema");
        Tema savedTema = Tema.builder().id(1L).descricao("Descrição do Tema").build();

        when(temaRepository.save(any(Tema.class))).thenReturn(savedTema);

        TemaResponseDTO response = temaService.criarTema(temaDTO);

        assertNotNull(response);
        assertEquals(savedTema.getId(), response.id());
        assertEquals(savedTema.getDescricao(), response.descricao());
        verify(temaRepository, times(1)).save(any(Tema.class));
    }

    @Test
    void testAtualizarTema() throws ServiceException {
        Long temaId = 1L;
        TemaDTO temaDTO = new TemaDTO("Nova Descrição");
        Tema tema = Tema.builder().id(temaId).descricao("Descrição Antiga").build();

        when(temaRepository.findById(temaId)).thenReturn(Optional.of(tema));
        when(temaRepository.save(any(Tema.class))).thenReturn(tema);

        TemaResponseDTO response = temaService.atualizarTema(temaDTO, temaId);

        assertNotNull(response);
        assertEquals(temaId, response.id());
        assertEquals("Nova Descrição", response.descricao());
        verify(temaRepository, times(1)).findById(temaId);
        verify(temaRepository, times(1)).save(tema);
    }

    @Test
    void testDeletarTema() throws ServiceException {
        Long temaId = 1L;
        Tema tema = Tema.builder().id(temaId).descricao("Descrição").build();

        when(temaRepository.findById(temaId)).thenReturn(Optional.of(tema));
        doNothing().when(temaRepository).delete(tema);

        assertDoesNotThrow(() -> temaService.deletarTema(temaId));
        verify(temaRepository, times(1)).findById(temaId);
        verify(temaRepository, times(1)).delete(tema);
    }

    @Test
    void testListarTodosTemas() throws ServiceException {
        Pageable pageable = mock(Pageable.class);
        Tema tema = Tema.builder().id(1L).descricao("Descrição").build();
        Page<Tema> page = new PageImpl<>(List.of(tema));

        when(temaRepository.findAll(pageable)).thenReturn(page);

        List<TemaResponseDTO> response = temaService.listarTodosTemas(pageable);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(tema.getId(), response.get(0).id());
        assertEquals(tema.getDescricao(), response.get(0).descricao());
        verify(temaRepository, times(1)).findAll(pageable);
    }
}