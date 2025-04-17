package com.montreal.acelera.blog_pessoal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montreal.acelera.blog_pessoal.dto.requests.TemaDTO;
import com.montreal.acelera.blog_pessoal.model.Tema;
import com.montreal.acelera.blog_pessoal.repository.TemaRepository;
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
public class TemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        temaRepository.deleteAll();
    }

    @Test
    void shouldCreateTema() throws Exception {
        TemaDTO temaDTO = TemaDTO.builder()
                .descricao("Tecnologia")
                .build();

        mockMvc.perform(post("/api/tema/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(temaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Tecnologia"));
    }

    @Test
    void shouldGetAllTemas() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        temaRepository.save(tema);

        mockMvc.perform(get("/api/tema")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Tecnologia"));
    }

    @Test
    void shouldUpdateTema() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        TemaDTO updatedTemaDTO = TemaDTO.builder()
                .descricao("Ciência")
                .build();

        mockMvc.perform(put("/api/tema/" + tema.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTemaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Ciência"));
    }

    @Test
    void shouldDeleteTema() throws Exception {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");
        tema = temaRepository.save(tema);

        mockMvc.perform(delete("/api/tema/" + tema.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentTema() throws Exception {
        mockMvc.perform(delete("/api/tema/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPaginateAndSortTemas() throws Exception {
        Tema tema1 = new Tema();
        tema1.setDescricao("Tecnologia");
        temaRepository.save(tema1);

        Tema tema2 = new Tema();
        tema2.setDescricao("Ciência");
        temaRepository.save(tema2);

        mockMvc.perform(get("/api/tema")
                .param("page", "0")
                .param("per_page", "1")
                .param("sort", "descricao")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Ciência"));
    }
}