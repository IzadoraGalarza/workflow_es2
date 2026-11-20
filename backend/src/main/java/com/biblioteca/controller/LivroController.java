package com.biblioteca.controller;

import com.biblioteca.model.Autor;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroRepository repository;
    private final AutorRepository autorRepository;

    public LivroController(LivroRepository repository, AutorRepository autorRepository) {
        this.repository = repository;
        this.autorRepository = autorRepository;
    }

    @PostMapping
    public ResponseEntity<Livro> criar(@Valid @RequestBody Livro livro) {
        if (livro.getAutor().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Autor> autor = autorRepository.findById(livro.getAutor().getId());
        if (autor.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        livro.setId(null);
        livro.setAutor(autor.get());
        Livro salvo = repository.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Livro> listar() {
        return repository.findAllByOrderByTituloAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // (extra) lista os livros de um autor
    @GetMapping("/autor/{autorId}")
    public List<Livro> listarPorAutor(@PathVariable Long autorId) {
        return repository.findByAutorId(autorId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id, @Valid @RequestBody Livro dados) {
        Optional<Livro> existente = repository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (dados.getAutor().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Autor> autor = autorRepository.findById(dados.getAutor().getId());
        if (autor.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Livro livro = existente.get();
        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setGenero(dados.getGenero());
        livro.setAutor(autor.get()); // atualiza também o autor
        return ResponseEntity.ok(repository.save(livro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
