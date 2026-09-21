package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;

public class LimiteItensExcedidoException extends RegraNegocioException {

    private static final long serialVersionUID = 1L;

    public LimiteItensExcedidoException(final String mensagem) {
        super(mensagem);
    }
}
