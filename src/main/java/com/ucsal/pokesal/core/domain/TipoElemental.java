package com.ucsal.pokesal.core.domain;

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
