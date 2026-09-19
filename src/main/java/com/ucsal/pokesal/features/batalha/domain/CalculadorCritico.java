package com.ucsal.pokesal.features.batalha.domain;

import com.ucsal.pokesal.features.pokesal.domain.Pokesal;

@FunctionalInterface
public interface CalculadorCritico {

    boolean isCritico(Pokesal atacante, Pokesal defensor);
}
