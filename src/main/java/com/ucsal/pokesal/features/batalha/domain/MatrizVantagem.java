package com.ucsal.pokesal.features.batalha.domain;

import com.ucsal.pokesal.core.domain.TipoElemental;

public final class MatrizVantagem {

    public static final double VANTAGEM = 2.0;
    public static final double DESVANTAGEM = 0.5;
    public static final double NEUTRO = 1.0;

    private MatrizVantagem() {

    }

    public static double calcularMultiplicador(final TipoElemental atacante, final TipoElemental defensor) {
        if (atacante == null || defensor == null) {
            return NEUTRO;
        }

        if (atacante == TipoElemental.FOGO) {
            if (defensor == TipoElemental.PLANTA) {
                return VANTAGEM;
            }
            if (defensor == TipoElemental.AGUA) {
                return DESVANTAGEM;
            }
        } else if (atacante == TipoElemental.AGUA) {
            if (defensor == TipoElemental.FOGO) {
                return VANTAGEM;
            }
            if (defensor == TipoElemental.PLANTA || defensor == TipoElemental.ELETRICO) {
                return DESVANTAGEM;
            }
        } else if (atacante == TipoElemental.PLANTA) {
            if (defensor == TipoElemental.AGUA) {
                return VANTAGEM;
            }
            if (defensor == TipoElemental.FOGO) {
                return DESVANTAGEM;
            }
        } else if (atacante == TipoElemental.ELETRICO) {
            if (defensor == TipoElemental.AGUA) {
                return VANTAGEM;
            }
            if (defensor == TipoElemental.PLANTA) {
                return DESVANTAGEM;
            }
        }

        return NEUTRO;
    }
}
