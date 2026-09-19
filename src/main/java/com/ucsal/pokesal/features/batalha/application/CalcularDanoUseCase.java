package com.ucsal.pokesal.features.batalha.application;

import com.ucsal.pokesal.features.batalha.domain.MatrizVantagem;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.springframework.stereotype.Service;

/**
 * Caso de uso responsável pelo cálculo de dano entre dois PokéSals considerando terreno e tipos elementais.
 */
@Service
public class CalcularDanoUseCase {

    private static final int DANO_MINIMO = 1;
    private static final double FATOR_DEFESA = 0.5;

    /**
     * Calcula o dano final causado pelo atacante ao defensor.
     * Fórmula: max(1, round((ataque - defesa * 0.5) * multElemental * multTerreno))
     *
     * @param atacante PokéSal que está executando o golpe
     * @param defensor PokéSal que está recebendo o golpe
     * @param terreno condição atual da arena
     * @return valor inteiro de dano calculado
     */
    public int calcularDano(final Pokesal atacante, final Pokesal defensor, final Terreno terreno) {
        final double multElemental = MatrizVantagem.calcularMultiplicador(atacante.getTipo(), defensor.getTipo());
        final double multTerreno = (terreno != null)
                ? terreno.obterMultiplicador(atacante.getTipo())
                : Terreno.NEUTRO.obterMultiplicador(atacante.getTipo());

        final double baseDano = atacante.getAtaque() - (defensor.getDefesa() * FATOR_DEFESA);
        final double danoCalculado = Math.max(0.0, baseDano) * multElemental * multTerreno;

        final int danoFinal = (int) Math.round(danoCalculado);
        return Math.max(DANO_MINIMO, danoFinal);
    }
}
