package com.biblioteca.controller;

import com.biblioteca.model.Autor;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.LivroRepository;
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

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LivroRepository repository;

    @MockBean
    private AutorRepository autorRepository;

    private Autor autorExemplo() {
        Autor autor = new Autor();
        autor.setId(1L);
        autor.setNome("Machado de Assis");
        return autor;
    }

    @Test
    void deveCriarLivroComSucesso() throws Exception {
        Autor autor = autorExemplo();
        Livro salvo = new Livro();
        salvo.setId(10L);
        salvo.setTitulo("Dom Casmurro");
        salvo.setAutor(autor);

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(repository.save(any(Livro.class))).thenReturn(salvo);

        String json = "{\"titulo\":\"Dom Casmurro\",\"isbn\":\"978-8535910667\","
                + "\"anoPublicacao\":1899,\"genero\":\"Romance\",\"autor\":{\"id\":1}}";

        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$.autor.id").value(1));
    }

    @Test
    void deveRetornar404ParaLivroInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/livros/999"))
                .andExpect(status().isNotFound());
    }

    // (extra) listagem ordenada
    @Test
    void deveListarLivrosOrdenados() throws Exception {
        Autor autor = autorExemplo();
        Livro a = new Livro();
        a.setId(1L);
        a.setTitulo("Dom Casmurro");
        a.setAutor(autor);
        Livro b = new Livro();
        b.setId(2L);
        b.setTitulo("Memórias Póstumas");
        b.setAutor(autor);

        when(repository.findAllByOrderByTituloAsc()).thenReturn(List.of(a, b));

        mockMvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$[1].titulo").value("Memórias Póstumas"));
    }

    @Test
    void deveRejeitarLivroSemAutor() throws Exception {
        mockMvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Livro sem autor\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404AoRemoverLivroInexistente() throws Exception {
        when(repository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/livros/999"))
                .andExpect(status().isNotFound());
    }
}
