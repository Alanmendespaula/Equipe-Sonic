package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;

public class SaldoInsuficienteException extends RegraNegocioException {

    public SaldoInsuficienteException(final String mensagem) {
        super(mensagem);
    }
}
