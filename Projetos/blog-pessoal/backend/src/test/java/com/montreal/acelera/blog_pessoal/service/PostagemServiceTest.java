package com.montreal.acelera.blog_pessoal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.montreal.acelera.blog_pessoal.dto.requests.PostagemDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.PostagemResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.model.Postagem;
import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.model.Usuario;
import com.montreal.acelera.blog_pessoal.repository.PostagemRepository;
import com.montreal.acelera.blog_pessoal.repository.TemaRepository;
import com.montreal.acelera.blog_pessoal.repository.UsuarioRepository;


class PostagemServiceTest {

    @InjectMocks
    private PostagemService postagemService;

    @Mock
    private PostagemRepository postagemRepository;

    @Mock
    private TemaRepository temaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarPostagem() {
        PostagemDTO postagemDTO = new PostagemDTO("Titulo", "Texto", 1L, 1L, null);
        Usuario usuario = new Usuario();
        Tema tema = new Tema();
        Postagem postagem = new Postagem();
        postagem.setTitulo("Titulo");
        postagem.setTexto("Texto");
        postagem.setUsuario(usuario);
        postagem.setTema(tema);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(temaRepository.findById(1L)).thenReturn(Optional.of(tema));
        when(postagemRepository.save(any(Postagem.class))).thenReturn(postagem);

        PostagemResponseDTO response = postagemService.criarPostagem(postagemDTO);

        assertNotNull(response);
        assertEquals("Titulo", response.titulo());
        verify(postagemRepository, times(1)).save(any(Postagem.class));
    }

    @Test
    void testAtualizarPostagem() throws ServiceException {
        PostagemDTO postagemDTO = new PostagemDTO("Novo Titulo", "Novo Texto", 1L, 1L, null);
        Postagem postagem = new Postagem();
        postagem.setId(1L);
        postagem.setTitulo("Titulo");
        postagem.setTexto("Texto");
        postagem.setUsuario(new Usuario());
        postagem.setTema(new Tema());

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(postagem.getUsuario()));
        when(temaRepository.findById(1L)).thenReturn(Optional.of(postagem.getTema()));

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(postagemRepository.save(any(Postagem.class))).thenReturn(postagem);

        PostagemResponseDTO response = postagemService.atualizarPostagem(postagemDTO, 1L);

        assertNotNull(response);
        assertEquals("Novo Titulo", response.titulo());
        verify(postagemRepository, times(1)).save(any(Postagem.class));
    }

    @Test
    void testDeletarPostagem() throws ServiceException {
        Postagem postagem = new Postagem();
        postagem.setId(1L);

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        doNothing().when(postagemRepository).delete(postagem);

        postagemService.deletarPostagem(1L);

        verify(postagemRepository, times(1)).delete(postagem);
    }

    @Test
    void testListarTodasPostagens() throws ServiceException {
        Postagem postagem = new Postagem();
        postagem.setTitulo("Titulo");
        postagem.setUsuario(new Usuario());

        Page<Postagem> page = new PageImpl<>(List.of(postagem));

        when(postagemRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<PostagemResponseDTO> response = postagemService.listarTodasPostagens(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Titulo", response.get(0).titulo());
    }

    @Test
    void testBuscarPostagemPorId() throws ServiceException {
        Postagem postagem = new Postagem();
        postagem.setId(1L);
        postagem.setTitulo("Titulo");
        postagem.setUsuario(new Usuario());

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));

        PostagemResponseDTO response = postagemService.buscarPostagemPorId(1L);

        assertNotNull(response);
        assertEquals("Titulo", response.titulo());
    }

    @Test
    void testFiltrarPostagens() {
        Postagem postagem = new Postagem();
        postagem.setTitulo("Titulo");
        postagem.setUsuario(new Usuario());

        when(postagemRepository.findAll(ArgumentMatchers.<Specification<Postagem>>any())).thenReturn(List.of(postagem));
    
        List<PostagemResponseDTO> response = postagemService.filtrarPostagens("Titulo", null, null, null);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Titulo", response.get(0).titulo());
    }
}