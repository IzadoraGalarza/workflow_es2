package com.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidade Livro (tabela "livros"). Modelo: Compromisso.java da Agenda.
 * Muitos livros (N) pertencem a um autor (1): FK autor_id.
 */
@Data
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 200)
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Size(max = 20)
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Size(max = 50)
    @Column(name = "genero", length = 50)
    private String genero;

    @NotNull(message = "O autor é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    protected void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }
}
