package com.ucsal.pokesal.features.pokesal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.core.domain.TipoElemental;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PokesalBoundaryTest {

    @Test
    @DisplayName("Deve lançar RegraNegocioException ao criar PokéSal com valores de borda inválidos (zero ou negativos)")
    void deveValidarValoresDeBordaDosAtributos() {
        // HP inválido
        assertThrows(RegraNegocioException.class, () ->
                new Pokesal("Sal", TipoElemental.FOGO, 0, 10, 10, 10));

        // Ataque inválido
        assertThrows(RegraNegocioException.class, () ->
                new Pokesal("Sal", TipoElemental.FOGO, 50, -5, 10, 10));

        // Defesa inválida
        assertThrows(RegraNegocioException.class, () ->
                new Pokesal("Sal", TipoElemental.FOGO, 50, 10, 0, 10));

        // Velocidade inválida
        assertThrows(RegraNegocioException.class, () ->
                new Pokesal("Sal", TipoElemental.FOGO, 50, 10, 10, -1));
    }

    @Test
    @DisplayName("HP atual não deve ficar negativo mesmo sofrendo dano massivo (Boundary Zero)")
    void hpNaoDeveFicarNegativoAposDanoMassivo() {
        final Pokesal pokesal = new Pokesal("Sal", TipoElemental.AGUA, 100, 20, 20, 30);
        pokesal.sofrerDano(999);

        assertEquals(0, pokesal.getHpAtual());
        assertTrue(pokesal.estaDerrotado());
    }

    @Test
    @DisplayName("Cura não deve ultrapassar o HP máximo do PokéSal (Boundary HP Max)")
    void curaNaoDeveUltrapassarHpMax() {
        final Pokesal pokesal = new Pokesal("Sal", TipoElemental.PLANTA, 100, 20, 20, 30);
        pokesal.sofrerDano(30);
        assertEquals(70, pokesal.getHpAtual());

        pokesal.curar(50);
        assertEquals(100, pokesal.getHpAtual(), "HP curado deve ser limitado ao HP máximo");
        assertFalse(pokesal.estaDerrotado());
    }
}
