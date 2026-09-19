package com.ucsal.pokesal.features.treinador.application;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.features.treinador.domain.Item;
import com.ucsal.pokesal.features.treinador.domain.LimiteItensExcedidoException;
import com.ucsal.pokesal.features.treinador.domain.Mochila;
import com.ucsal.pokesal.features.treinador.domain.Treinador;
import com.ucsal.pokesal.features.treinador.infrastructure.TreinadorRepository;
import org.springframework.stereotype.Service;

/**
 * Requisito Autoral: Caso de uso responsável pela compra de itens na loja do Centro PokéSal,
 * validando o saldo em PokéMoedas e o limite de itens da mochila.
 */
@Service
public class ComprarItemUseCase {

    private final TreinadorRepository treinadorRepository;

    public ComprarItemUseCase(final TreinadorRepository treinadorRepository) {
        this.treinadorRepository = treinadorRepository;
    }

    /**
     * Realiza a compra de um item debitando as moedas e inserindo na mochila do treinador.
     *
     * @param treinador treinador comprador
     * @param item item a ser adquirido
     * @param preco valor em PokéMoedas do item
     */
    public void comprarItem(final Treinador treinador, final Item item, final int preco) {
        if (treinador == null) {
            throw new RegraNegocioException("Treinador não pode ser nulo.");
        }
        if (item == null) {
            throw new RegraNegocioException("Item não pode ser nulo.");
        }
        if (preco <= 0) {
            throw new RegraNegocioException("O preço do item deve ser maior que zero.");
        }
        if (treinador.getMochila().estaCheia()) {
            throw new LimiteItensExcedidoException("Mochila cheia! Limite de "
                    + Mochila.LIMITE_MAXIMO_ITENS + " itens atingido.");
        }

        treinador.debitarMoedas(preco);
        treinador.getMochila().adicionarItem(item);

        if (treinadorRepository != null && treinador.getId() != null) {
            treinadorRepository.save(treinador);
        }
    }
}
