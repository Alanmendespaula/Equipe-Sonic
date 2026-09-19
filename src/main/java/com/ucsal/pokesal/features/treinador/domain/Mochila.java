package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a mochila do treinador, com capacidade estrita de até 2 itens.
 */
public class Mochila {

    public static final int LIMITE_MAXIMO_ITENS = 2;

    private final List<Item> itens = new ArrayList<>();

    /**
     * Adiciona um item à mochila, respeitando o limite máximo de 2 itens.
     *
     * @param item item a ser adicionado
     * @throws LimiteItensExcedidoException se a mochila já contiver 2 itens
     */
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

    /**
     * Remove um item da mochila.
     *
     * @param item item a ser removido
     * @return true se removido com sucesso
     */
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
