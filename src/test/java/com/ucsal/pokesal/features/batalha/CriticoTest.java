package com.ucsal.pokesal.features.batalha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ucsal.pokesal.core.domain.TipoElemental;
import com.ucsal.pokesal.features.batalha.application.CalcularDanoUseCase;
import com.ucsal.pokesal.features.batalha.domain.CalculadorCritico;
import com.ucsal.pokesal.features.batalha.domain.CalculadorCriticoPadrao;
import com.ucsal.pokesal.features.batalha.domain.ResultadoDano;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CriticoTest {

    @Test
    @DisplayName("Golpe crítico deve aplicar multiplicador de 1.5x ao dano e marcar flag isCritico")
    void deveAplicarMultiplicadorQuandoCritico() {
        final CalculadorCritico criticoSempre = (atacante, defensor) -> true;
        final CalcularDanoUseCase useCase = new CalcularDanoUseCase(criticoSempre);

        final Pokesal atacante = new Pokesal("Salmander", TipoElemental.FOGO, 100, 30, 20, 50);
        final Pokesal defensor = new Pokesal("Salvasaur", TipoElemental.PLANTA, 100, 25, 20, 45);

        final ResultadoDano resultado = useCase.calcular(atacante, defensor, Terreno.NEUTRO);

        assertTrue(resultado.isCritico());
        assertEquals(60, resultado.getValorDano());
    }

    @Test
    @DisplayName("Ataque comum sem crítico não deve aplicar bônus e marcar isCritico como false")
    void naoDeveAplicarBonusQuandoNaoCritico() {
        final CalculadorCritico criticoNunca = (atacante, defensor) -> false;
        final CalcularDanoUseCase useCase = new CalcularDanoUseCase(criticoNunca);

        final Pokesal atacante = new Pokesal("Salmander", TipoElemental.FOGO, 100, 30, 20, 50);
        final Pokesal defensor = new Pokesal("Salvasaur", TipoElemental.PLANTA, 100, 25, 20, 45);

        final ResultadoDano resultado = useCase.calcular(atacante, defensor, Terreno.NEUTRO);

        assertFalse(resultado.isCritico());
        assertEquals(40, resultado.getValorDano());
    }

    @Test
    @DisplayName("CalculadorCriticoPadrao deve calcular chance considerando velocidade relativa")
    void deveAumentarChanceQuandoMaisRapido() {

        final Random randomMaisRapido = new Random() {
            @Override
            public double nextDouble() {
                return 0.18;
            }
        };

        final CalculadorCriticoPadrao calculador = new CalculadorCriticoPadrao(randomMaisRapido);
        final Pokesal rapido = new Pokesal("Rapisalt", TipoElemental.ELETRICO, 100, 30, 20, 90);
        final Pokesal lento = new Pokesal("Lentasalt", TipoElemental.PLANTA, 100, 25, 20, 30);

        assertTrue(calculador.isCritico(rapido, lento));
        assertFalse(calculador.isCritico(lento, rapido));
    }
}
