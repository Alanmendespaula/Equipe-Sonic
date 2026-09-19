package com.ucsal.pokesal.features.batalha.domain;

/**
 * Requisito Autoral: Representa o resultado detalhado de um cálculo de dano,
 * contendo informações sobre acerto crítico e ativação de passiva.
 */
public class ResultadoDano {

    private final int valorDano;
    private final boolean critico;
    private final boolean furiaAtivada;

    public ResultadoDano(final int valorDano, final boolean critico, final boolean furiaAtivada) {
        this.valorDano = valorDano;
        this.critico = critico;
        this.furiaAtivada = furiaAtivada;
    }

    public int getValorDano() {
        return valorDano;
    }

    public boolean isCritico() {
        return critico;
    }

    public boolean isFuriaAtivada() {
        return furiaAtivada;
    }
}
