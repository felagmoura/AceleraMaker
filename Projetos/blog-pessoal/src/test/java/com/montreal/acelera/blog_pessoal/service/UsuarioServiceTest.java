package com.montreal.acelera.blog_pessoal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.montreal.acelera.blog_pessoal.dto.requests.UsuarioDTO;
import com.montreal.acelera.blog_pessoal.dto.responses.UsuarioResponseDTO;
import com.montreal.acelera.blog_pessoal.exeception.ServiceException;
import com.montreal.acelera.blog_pessoal.model.Usuario;
import com.montreal.acelera.blog_pessoal.repository.UsuarioRepository;






class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarUsuario() throws ServiceException {
        UsuarioDTO usuarioDTO = new UsuarioDTO("Nome", "usuario", "senha", "foto");
        Usuario usuario = Usuario.builder()
                .nome("Nome")
                .usuario("usuario")
                .senha("encodedSenha")
                .foto("foto")
                .build();

        when(passwordEncoder.encode(usuarioDTO.senha())).thenReturn("encodedSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO response = usuarioService.cadastrarUsuario(usuarioDTO);

        assertNotNull(response);
        assertEquals("Nome", response.nome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testAtualizarUsuario() throws ServiceException {
        Long usuarioId = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO("Novo Nome", "novoUsuario", "senha", "novaFoto");
        Usuario usuario = Usuario.builder()
                .id(usuarioId)
                .nome("Nome")
                .usuario("usuario")
                .foto("foto")
                .build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO response = usuarioService.atualizarUsuario(usuarioDTO, usuarioId);

        assertNotNull(response);
        assertEquals("Novo Nome", response.nome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testDeletarUsuario() throws ServiceException {
        Long usuarioId = 1L;
        Usuario usuario = Usuario.builder().id(usuarioId).build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        assertDoesNotThrow(() -> usuarioService.deletarUsuario(usuarioId));
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void testBuscarUsuarioPorId() throws ServiceException {
        Long usuarioId = 1L;
        Usuario usuario = Usuario.builder().id(usuarioId).nome("Nome").build();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioService.buscarUsuarioPorId(usuarioId);

        assertNotNull(response);
        assertEquals("Nome", response.nome());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    @Test
    void testBuscarUsuarioPorNome() throws ServiceException {
        String nome = "Nome";
        Usuario usuario = Usuario.builder().nome(nome).build();

        when(usuarioRepository.findByNome(nome)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioService.buscarUsuarioPorNome(nome);

        assertNotNull(response);
        assertEquals(nome, response.nome());
        verify(usuarioRepository, times(1)).findByNome(nome);
    }

    @Test
    void testBuscarUsuarioPorUsuario() throws ServiceException {
        String usuarioStr = "usuario";
        Usuario usuario = Usuario.builder().usuario(usuarioStr).build();

        when(usuarioRepository.findByUsuario(usuarioStr)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO response = usuarioService.buscarUsuarioPorUsuario(usuarioStr);

        assertNotNull(response);
        assertEquals(usuarioStr, response.usuario());
        verify(usuarioRepository, times(1)).findByUsuario(usuarioStr);
    }

    @Test
    void testListarTodosUsuarios() throws ServiceException {
        Pageable pageable = mock(Pageable.class);
        Usuario usuario = Usuario.builder().nome("Nome").build();
        Page<Usuario> page = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findAll(pageable)).thenReturn(page);

        List<UsuarioResponseDTO> response = usuarioService.listarTodosUsuarios(pageable);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Nome", response.get(0).nome());
        verify(usuarioRepository, times(1)).findAll(pageable);
    }
}