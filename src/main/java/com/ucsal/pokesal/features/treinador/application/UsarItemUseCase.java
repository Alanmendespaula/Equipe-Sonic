package com.ucsal.pokesal.features.treinador.application;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import com.ucsal.pokesal.features.treinador.domain.Item;
import com.ucsal.pokesal.features.treinador.domain.Treinador;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pelo consumo de um item da mochila do treinador sobre seu PokéSal.
 */
@Service
public class UsarItemUseCase {

    /**
     * Aplica o efeito do item sobre o PokéSal e remove o item da mochila do treinador.
     *
     * @param treinador treinador portador do item
     * @param item item a ser consumido
     */
    public void usarItem(final Treinador treinador, final Item item) {
        if (treinador == null) {
            throw new RegraNegocioException("Treinador inválido.");
        }
        final Pokesal pokesal = treinador.getPokesalAtivo();
        if (pokesal == null) {
            throw new RegraNegocioException("O treinador não possui um PokéSal ativo para usar o item.");
        }
        if (pokesal.estaDerrotado()) {
            throw new RegraNegocioException("Não é possível usar item de cura em um PokéSal derrotado.");
        }
        if (!treinador.getMochila().getItens().contains(item)) {
            throw new RegraNegocioException("O item solicitado não está presente na mochila do treinador.");
        }

        pokesal.curar(item.getPontosCura());
        treinador.getMochila().removerItem(item);
    }
}
