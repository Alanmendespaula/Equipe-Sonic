package com.ucsal.pokesal.features.pokesal.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.core.domain.TipoElemental;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "pokesals")
public class Pokesal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoElemental tipo;

    @Column(name = "hp_max", nullable = false)
    private int hpMax;

    @Column(name = "hp_atual", nullable = false)
    private int hpAtual;

    @Column(nullable = false)
    private int ataque;

    @Column(nullable = false)
    private int defesa;

    private static final double FATOR_LIMITE_FURIA = 0.30;

    @Column(nullable = false)
    private int velocidade;

    protected Pokesal() {

    }

    public Pokesal(final String nome,
                   final TipoElemental tipo,
                   final int hpMax,
                   final int ataque,
                   final int defesa,
                   final int velocidade) {
        validarAtributos(nome, tipo, hpMax, ataque, defesa, velocidade);
        this.nome = nome;
        this.tipo = tipo;
        this.hpMax = hpMax;
        this.hpAtual = hpMax;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
    }

    private void validarAtributos(final String nome,
                                  final TipoElemental tipo,
                                  final int hpMax,
                                  final int ataque,
                                  final int defesa,
                                  final int velocidade) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome do PokéSal não pode ser vazio.");
        }
        if (tipo == null) {
            throw new RegraNegocioException("O tipo elemental do PokéSal é obrigatório.");
        }
        if (hpMax <= 0) {
            throw new RegraNegocioException("O HP máximo deve ser maior que zero.");
        }
        if (ataque <= 0) {
            throw new RegraNegocioException("O valor de ataque deve ser maior que zero.");
        }
        if (defesa <= 0) {
            throw new RegraNegocioException("O valor de defesa deve ser maior que zero.");
        }
        if (velocidade <= 0) {
            throw new RegraNegocioException("O valor de velocidade deve ser maior que zero.");
        }
    }

    public void sofrerDano(final int dano) {
        if (dano <= 0) {
            return;
        }
        this.hpAtual = Math.max(0, this.hpAtual - dano);
    }

    public void curar(final int pontosCura) {
        if (pontosCura <= 0 || estaDerrotado()) {
            return;
        }
        this.hpAtual = Math.min(this.hpMax, this.hpAtual + pontosCura);
    }

    public boolean estaDerrotado() {
        return this.hpAtual <= 0;
    }

    public boolean isFuriaAtiva() {
        return this.hpAtual > 0 && this.hpAtual <= (int) Math.floor(this.hpMax * FATOR_LIMITE_FURIA);
    }

    public void restaurar() {
        this.hpAtual = this.hpMax;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoElemental getTipo() {
        return tipo;
    }

    public int getHpMax() {
        return hpMax;
    }

    public int getHpAtual() {
        return hpAtual;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getVelocidade() {
        return velocidade;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Pokesal other = (Pokesal) obj;
        return Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
}
