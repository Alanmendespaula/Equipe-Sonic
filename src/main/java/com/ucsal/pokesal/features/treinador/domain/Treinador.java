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
 * Contém informações de PokéSal ativo, mochila e saldo de PokéMoedas (Requisito Autoral 3).
 */
@Entity
@Table(name = "treinadores")
public class Treinador {

    private static final int MOEDAS_INICIAIS_PADRAO = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false)
    private int moedas;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pokesal_ativo_id")
    private Pokesal pokesalAtivo;

    @Transient
    private final Mochila mochila = new Mochila();

    protected Treinador() {
        // Construtor padrão JPA
    }

    public Treinador(final String nome) {
        this(nome, MOEDAS_INICIAIS_PADRAO);
    }

    public Treinador(final String nome, final int moedasIniciais) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome do treinador é obrigatório.");
        }
        if (moedasIniciais < 0) {
            throw new RegraNegocioException("O saldo inicial de moedas não pode ser negativo.");
        }
        this.nome = nome;
        this.moedas = moedasIniciais;
    }

    /**
     * Adiciona moedas ao saldo do treinador.
     *
     * @param quantidade valor a ser creditado
     */
    public void adicionarMoedas(final int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade de moedas a adicionar deve ser maior que zero.");
        }
        this.moedas += quantidade;
    }

    /**
     * Debita moedas do saldo do treinador.
     *
     * @param quantidade valor a ser debitado
     * @throws SaldoInsuficienteException caso o saldo atual seja menor que a quantidade
     */
    public void debitarMoedas(final int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade de moedas a debitar deve ser maior que zero.");
        }
        if (quantidade > this.moedas) {
            throw new SaldoInsuficienteException(
                    String.format("Saldo insuficiente de PokéMoedas. Atual: %d, Necessário: %d",
                            this.moedas, quantidade));
        }
        this.moedas -= quantidade;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getMoedas() {
        return moedas;
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
