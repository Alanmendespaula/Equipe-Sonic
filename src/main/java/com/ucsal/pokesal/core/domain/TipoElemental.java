package com.ucsal.pokesal.core.domain;

/**
 * Representa os tipos elementais disponíveis no ecossistema PokéSal.
 */
public enum TipoElemental {
    FOGO("Fogo"),
    AGUA("Água"),
    PLANTA("Planta"),
    ELETRICO("Elétrico"),
    NORMAL("Normal");

    private final String descricao;

    TipoElemental(final String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
