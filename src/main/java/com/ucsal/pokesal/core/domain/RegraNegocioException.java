package com.ucsal.pokesal.core.domain;

public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(final String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(final String mensagem, final Throwable causa) {
        super(mensagem, causa);
    }
}
