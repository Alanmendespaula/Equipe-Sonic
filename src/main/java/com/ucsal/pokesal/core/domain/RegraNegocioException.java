package com.ucsal.pokesal.core.domain;

/**
 * Exceção base de domínio para violação de invariantes e regras de negócio.
 */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(final String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(final String mensagem, final Throwable causa) {
        super(mensagem, causa);
    }
}
