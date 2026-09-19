package com.ucsal.pokesal.features.treinador.domain;

import com.ucsal.pokesal.core.domain.RegraNegocioException;

/**
 * Exceção disparada quando há tentativa de adicionar itens além do limite permitido na mochila (máx 2 itens).
 */
public class LimiteItensExcedidoException extends RegraNegocioException {

    private static final long serialVersionUID = 1L;

    public LimiteItensExcedidoException(final String mensagem) {
        super(mensagem);
    }
}
