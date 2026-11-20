package com.biblioteca.controller;

import com.biblioteca.model.Autor;
import com.biblioteca.repository.AutorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AutorRepository repository;

    @Test
    void deveCriarAutorComSucesso() throws Exception {
        Autor entrada = new Autor();
        entrada.setNome("Machado de Assis");
        entrada.setNacionalidade("Brasileira");

        Autor salvo = new Autor();
        salvo.setId(1L);
        salvo.setNome("Machado de Assis");
        salvo.setNacionalidade("Brasileira");

        when(repository.save(any(Autor.class))).thenReturn(salvo);

        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Machado de Assis"));
    }

    @Test
    void deveRetornar404ParaAutorInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/autores/999"))
                .andExpect(status().isNotFound());
    }

    // (extra) listagem ordenada
    @Test
    void deveListarAutoresOrdenados() throws Exception {
        Autor a = new Autor();
        a.setId(1L);
        a.setNome("Clarice Lispector");
        Autor b = new Autor();
        b.setId(2L);
        b.setNome("Machado de Assis");

        when(repository.findAllByOrderByNomeAsc()).thenReturn(List.of(a, b));

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Clarice Lispector"))
                .andExpect(jsonPath("$[1].nome").value("Machado de Assis"));
    }

    @Test
    void deveRejeitarAutorSemNome() throws Exception {
        mockMvc.perform(post("/api/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nacionalidade\":\"Brasileira\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoAtualizarAutorInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/autores/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Fulano\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar404AoRemoverAutorInexistente() throws Exception {
        when(repository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/autores/999"))
                .andExpect(status().isNotFound());
    }
}
