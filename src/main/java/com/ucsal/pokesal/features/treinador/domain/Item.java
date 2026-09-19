package com.ucsal.pokesal.features.treinador.domain;

import java.util.Objects;

/**
 * Representa um item utilizável na mochila do treinador (ex: Poção de Cura).
 */
public class Item {

    private final String nome;
    private final int pontosCura;

    public Item(final String nome, final int pontosCura) {
        this.nome = nome;
        this.pontosCura = pontosCura;
    }

    public String getNome() {
        return nome;
    }

    public int getPontosCura() {
        return pontosCura;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        return pontosCura == other.pontosCura && Objects.equals(nome, other.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, pontosCura);
    }
}
