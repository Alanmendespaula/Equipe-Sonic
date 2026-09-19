package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Objects;

/**
 * Entidade que representa o Treinador no domínio PokéSal.
 */
@Entity
@Table(name = "treinadores")
public class Treinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pokesal_ativo_id")
    private Pokesal pokesalAtivo;

    @Transient
    private final Mochila mochila = new Mochila();

    protected Treinador() {
        // Construtor padrão JPA
    }

    public Treinador(final String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome do treinador é obrigatório.");
        }
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Pokesal getPokesalAtivo() {
        return pokesalAtivo;
    }

    public void setPokesalAtivo(final Pokesal pokesalAtivo) {
        this.pokesalAtivo = pokesalAtivo;
    }

    public Mochila getMochila() {
        return mochila;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Treinador other = (Treinador) obj;
        return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}
