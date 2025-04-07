package com.montreal.acelera.blog_pessoal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montreal.acelera.blog_pessoal.dto.requests.UsuarioDTO;
import com.montreal.acelera.blog_pessoal.model.Usuario;
import com.montreal.acelera.blog_pessoal.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void shouldCreateUsuario() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder()
                .nome("Felipe")
                .usuario("felipe")
                .senha("Senha123!")
                .build();

        mockMvc.perform(post("/api/usuario/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Felipe"))
                .andExpect(jsonPath("$.usuario").value("felipe"));
    }

    @Test
    void shouldUpdateUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);

        UsuarioDTO savedUsuarioDTO = UsuarioDTO.builder()
                .nome("Felipe Updated")
                .usuario("felipe")
                .build();

        mockMvc.perform(put("/api/usuario/" + usuario.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savedUsuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Felipe Updated"));
    }

    @Test
    void shouldDeleteUsuario() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);

        mockMvc.perform(delete("/api/usuario/" + usuario.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
