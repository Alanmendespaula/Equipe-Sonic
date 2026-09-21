package com.ucsal.pokesal.features.batalha;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ucsal.pokesal.core.domain.TipoElemental;
import com.ucsal.pokesal.features.batalha.domain.MatrizVantagem;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MatrizVantagemTest {

    @Test
    @DisplayName("Fogo deve ter vantagem 2.0x contra Planta e desvantagem 0.5x contra Água")
    void deveValidarMultiplicadoresFogo() {
        assertEquals(2.0, MatrizVantagem.calcularMultiplicador(TipoElemental.FOGO, TipoElemental.PLANTA));
        assertEquals(0.5, MatrizVantagem.calcularMultiplicador(TipoElemental.FOGO, TipoElemental.AGUA));
        assertEquals(1.0, MatrizVantagem.calcularMultiplicador(TipoElemental.FOGO, TipoElemental.NORMAL));
    }

    @Test
    @DisplayName("Água deve ter vantagem 2.0x contra Fogo e desvantagem 0.5x contra Planta e Elétrico")
    void deveValidarMultiplicadoresAgua() {
        assertEquals(2.0, MatrizVantagem.calcularMultiplicador(TipoElemental.AGUA, TipoElemental.FOGO));
        assertEquals(0.5, MatrizVantagem.calcularMultiplicador(TipoElemental.AGUA, TipoElemental.PLANTA));
        assertEquals(0.5, MatrizVantagem.calcularMultiplicador(TipoElemental.AGUA, TipoElemental.ELETRICO));
    }

    @Test
    @DisplayName("Planta deve ter vantagem 2.0x contra Água e desvantagem 0.5x contra Fogo")
    void deveValidarMultiplicadoresPlanta() {
        assertEquals(2.0, MatrizVantagem.calcularMultiplicador(TipoElemental.PLANTA, TipoElemental.AGUA));
        assertEquals(0.5, MatrizVantagem.calcularMultiplicador(TipoElemental.PLANTA, TipoElemental.FOGO));
    }

    @Test
    @DisplayName("Elétrico deve ter vantagem 2.0x contra Água e desvantagem 0.5x contra Planta")
    void deveValidarMultiplicadoresEletrico() {
        assertEquals(2.0, MatrizVantagem.calcularMultiplicador(TipoElemental.ELETRICO, TipoElemental.AGUA));
        assertEquals(0.5, MatrizVantagem.calcularMultiplicador(TipoElemental.ELETRICO, TipoElemental.PLANTA));
    }

    @Test
    @DisplayName("Terreno vulcânico deve aplicar bônus de 1.2x para ataques de Fogo e 1.0x para outros")
    void deveValidarEfeitosDeTerreno() {
        assertEquals(1.2, Terreno.VULCANICO.obterMultiplicador(TipoElemental.FOGO));
        assertEquals(1.0, Terreno.VULCANICO.obterMultiplicador(TipoElemental.AGUA));
        assertEquals(1.2, Terreno.AQUATICO.obterMultiplicador(TipoElemental.AGUA));
        assertEquals(1.0, Terreno.NEUTRO.obterMultiplicador(TipoElemental.FOGO));
    }
}
