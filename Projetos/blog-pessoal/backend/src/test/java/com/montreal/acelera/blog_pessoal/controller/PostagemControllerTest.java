package com.montreal.acelera.blog_pessoal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.model.Usuario;
import com.montreal.acelera.blog_pessoal.dto.requests.PostagemDTO;
import com.montreal.acelera.blog_pessoal.model.Postagem;
import com.montreal.acelera.blog_pessoal.repository.PostagemRepository;
import com.montreal.acelera.blog_pessoal.repository.TemaRepository;
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
public class PostagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostagemRepository postagemRepository;
    
    @Autowired
    private TemaRepository temaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        postagemRepository.deleteAll();
        temaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void shouldCreatePostagem() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);

        PostagemDTO postagemDTO = PostagemDTO.builder()
                .titulo("Nova Postagem")
                .texto("Conteúdo da postagem")
                .temaId(tema.getId())
                .usuarioId(usuario.getId())
                .build();

        mockMvc.perform(post("/api/postagens/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postagemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Nova Postagem"));
    }

    @Test
    void shouldUpdatePostagem() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);
        
        Postagem postagem = new Postagem();
        postagem.setTitulo("Postagem Antiga");
        postagem.setTexto("Conteúdo antigo");
        postagem.setTema(tema);
        postagem.setUsuario(usuario);
        postagem = postagemRepository.save(postagem);

        PostagemDTO updatedPostagemDTO = PostagemDTO.builder()
                .titulo("Postagem Atualizada")
                .texto("Conteúdo atualizado")
                .temaId(tema.getId())
                .usuarioId(usuario.getId())
                .build();

        mockMvc.perform(put("/api/postagens/" + postagem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPostagemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Postagem Atualizada"));
    }

    @Test
    void shouldDeletePostagem() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);
        
        Postagem postagem = new Postagem();
        postagem.setTitulo("Postagem para deletar");
        postagem.setTexto("Conteúdo antigo");
        postagem.setTema(tema);
        postagem.setUsuario(usuario);
        postagem = postagemRepository.save(postagem);

        mockMvc.perform(delete("/api/postagens/" + postagem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentPostagem() throws Exception {
        mockMvc.perform(delete("/api/postagens/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAllPostagens() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);
        
        Postagem postagem = new Postagem();
        postagem.setTitulo("Postagem");
        postagem.setTexto("Conteúdo");
        postagem.setTema(tema);
        postagem.setUsuario(usuario);
        postagem = postagemRepository.save(postagem);

        mockMvc.perform(get("/api/postagens/todas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Postagem"));
    }

    @Test
    void shouldFilterPostagens() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        Usuario usuario = new Usuario();
        usuario.setNome("Felipe");
        usuario.setUsuario("felipe");
        usuario.setSenha("Senha123!");
        usuario = usuarioRepository.save(usuario);
        
        Postagem postagem = new Postagem();
        postagem.setTitulo("Postagem Filtrada");
        postagem.setTexto("Conteúdo");
        postagem.setTema(tema);
        postagem.setUsuario(usuario);
        postagem = postagemRepository.save(postagem);

        mockMvc.perform(get("/api/postagens/filtrar")
                .param("titulo", "Postagem Filtrada")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Postagem Filtrada"));
    }
}