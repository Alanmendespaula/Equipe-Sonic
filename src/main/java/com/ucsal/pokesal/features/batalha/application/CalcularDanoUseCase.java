package com.ucsal.pokesal.features.batalha.application;

import com.ucsal.pokesal.features.batalha.domain.CalculadorCritico;
import com.ucsal.pokesal.features.batalha.domain.CalculadorCriticoPadrao;
import com.ucsal.pokesal.features.batalha.domain.MatrizVantagem;
import com.ucsal.pokesal.features.batalha.domain.ResultadoDano;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.springframework.stereotype.Service;

@Service
public class CalcularDanoUseCase {

    private static final int DANO_MINIMO = 1;
    private static final double FATOR_DEFESA = 0.5;
    private static final double FATOR_CRITICO = 1.5;
    private static final double MULTIPLICADOR_FURIA = 1.30;
    private static final double MULTIPLICADOR_PADRAO = 1.0;
    private static final double DANO_BASE_MINIMO = 0.0;

    private final CalculadorCritico calculadorCritico;

    public CalcularDanoUseCase() {
        this(new CalculadorCriticoPadrao());
    }

    public CalcularDanoUseCase(final CalculadorCritico calculadorCritico) {
        this.calculadorCritico = calculadorCritico;
    }

    public ResultadoDano calcular(final Pokesal atacante, final Pokesal defensor, final Terreno terreno) {
        final double multElemental = MatrizVantagem.calcularMultiplicador(atacante.getTipo(), defensor.getTipo());
        final double multTerreno = (terreno != null)
                ? terreno.obterMultiplicador(atacante.getTipo())
                : Terreno.NEUTRO.obterMultiplicador(atacante.getTipo());

        final boolean critico = calculadorCritico.isCritico(atacante, defensor);
        final double multCritico = critico ? FATOR_CRITICO : MULTIPLICADOR_PADRAO;

        final boolean furia = atacante.isFuriaAtiva();
        final double multFuria = furia ? MULTIPLICADOR_FURIA : MULTIPLICADOR_PADRAO;

        final double baseDano = atacante.getAtaque() - (defensor.getDefesa() * FATOR_DEFESA);
        final double danoCalculado = Math.max(DANO_BASE_MINIMO, baseDano)
                * multElemental * multTerreno * multCritico * multFuria;

        final int danoFinal = (int) Math.round(danoCalculado);
        final int valorEfetivo = Math.max(DANO_MINIMO, danoFinal);

        return new ResultadoDano(valorEfetivo, critico, furia);
    }

    public int calcularDano(final Pokesal atacante, final Pokesal defensor, final Terreno terreno) {
        return calcular(atacante, defensor, terreno).getValorDano();
    }
}
