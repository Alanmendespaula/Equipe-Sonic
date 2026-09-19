package com.ucsal.pokesal.features.batalha.domain;

import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import java.security.SecureRandom;
import java.util.Random;
import org.springframework.stereotype.Component;

/**
 * Requisito Autoral: Implementação padrão do cálculo de Acerto Crítico.
 * Chance base de 15%, com acréscimo de 5% caso o atacante seja mais veloz que o defensor.
 */
@Component
public class CalculadorCriticoPadrao implements CalculadorCritico {

    private static final double CHANCE_BASE_CRITICO = 0.15;
    private static final double BONUS_VELOCIDADE_CRITICO = 0.05;

    private final Random random;

    public CalculadorCriticoPadrao() {
        this(new SecureRandom());
    }

    public CalculadorCriticoPadrao(final Random random) {
        this.random = random;
    }

    @Override
    public boolean isCritico(final Pokesal atacante, final Pokesal defensor) {
        double chance = CHANCE_BASE_CRITICO;
        if (atacante != null && defensor != null && atacante.getVelocidade() > defensor.getVelocidade()) {
            chance += BONUS_VELOCIDADE_CRITICO;
        }
        return random.nextDouble() < chance;
    }
}
