package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;

/**
 * Requisito Autoral: Exceção lançada quando o treinador tenta comprar um item sem PokéMoedas suficientes.
 */
public class SaldoInsuficienteException extends RegraNegocioException {

    public SaldoInsuficienteException(final String mensagem) {
        super(mensagem);
    }
}
