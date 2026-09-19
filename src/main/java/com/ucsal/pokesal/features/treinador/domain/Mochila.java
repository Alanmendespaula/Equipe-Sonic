package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mochila {

    public static final int LIMITE_MAXIMO_ITENS = 2;

    private final List<Item> itens = new ArrayList<>();

    public void adicionarItem(final Item item) {
        if (item == null) {
            throw new RegraNegocioException("Item não pode ser nulo.");
        }
        if (itens.size() >= LIMITE_MAXIMO_ITENS) {
            throw new LimiteItensExcedidoException("A mochila já atingiu o limite máximo de "
                    + LIMITE_MAXIMO_ITENS + " itens.");
        }
        itens.add(item);
    }

    public boolean removerItem(final Item item) {
        return itens.remove(item);
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public int getQuantidadeItens() {
        return itens.size();
    }

    public boolean estaCheia() {
        return itens.size() >= LIMITE_MAXIMO_ITENS;
    }
}
