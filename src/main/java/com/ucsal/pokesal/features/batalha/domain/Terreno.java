package com.ucsal.pokesal.features.batalha.domain;

import com.ucsal.pokesal.core.domain.TipoElemental;

/**
 * Representa as condições do campo de batalha e modificadores associados.
 */
public enum Terreno {
    NEUTRO("Campo Neutro", null, 1.0),
    VULCANICO("Terreno Vulcânico", TipoElemental.FOGO, 1.2),
    AQUATICO("Terreno Aquático", TipoElemental.AGUA, 1.2),
    FLORESTAL("Terreno Florestal", TipoElemental.PLANTA, 1.2),
    ELETRICO("Terreno Elétrico", TipoElemental.ELETRICO, 1.2);

    private final String descricao;
    private final TipoElemental tipoFavorecido;
    private final double multiplicadorBonus;

    Terreno(final String descricao, final TipoElemental tipoFavorecido, final double multiplicadorBonus) {
        this.descricao = descricao;
        this.tipoFavorecido = tipoFavorecido;
        this.multiplicadorBonus = multiplicadorBonus;
    }

    /**
     * Calcula o multiplicador de terreno para o tipo elemental atacante.
     *
     * @param tipo tipo do ataque
     * @return multiplicador com bônus se for o elemento favorecido, ou 1.0 caso contrário
     */
    public double obterMultiplicador(final TipoElemental tipo) {
        if (tipoFavorecido != null && tipoFavorecido.equals(tipo)) {
            return multiplicadorBonus;
        }
        return 1.0;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoElemental getTipoFavorecido() {
        return tipoFavorecido;
    }
}
