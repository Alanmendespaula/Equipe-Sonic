package com.ucsal.pokesal.features.batalha.domain;

import com.ucsal.pokesal.features.pokesal.domain.Pokesal;

/**
 * Requisito Autoral: Estratégia de determinação de Acerto Crítico em batalha.
 */
@FunctionalInterface
public interface CalculadorCritico {

    /**
     * Determina se o ataque desferido resulta em um golpe crítico.
     *
     * @param atacante PokéSal que desfere o ataque
     * @param defensor PokéSal que recebe o ataque
     * @return true se o ataque for crítico, false caso contrário
     */
    boolean isCritico(Pokesal atacante, Pokesal defensor);
}
