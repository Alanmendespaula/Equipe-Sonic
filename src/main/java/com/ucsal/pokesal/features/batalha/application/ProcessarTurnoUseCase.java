package com.ucsal.pokesal.features.batalha.application;

import com.ucsal.pokesal.features.batalha.domain.ResultadoDano;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.batalha.infrastructure.HistoricoBatalhaAdapter;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.springframework.stereotype.Service;

/**
 * Orquestra o processamento de um turno de batalha, determinando prioridade de velocidade,
 * resolução de dano, golpes críticos e ativação de passivas autorais.
 */
@Service
public class ProcessarTurnoUseCase {

    private final CalcularDanoUseCase calcularDanoUseCase;
    private final HistoricoBatalhaAdapter historicoAdapter;

    public ProcessarTurnoUseCase(final CalcularDanoUseCase calcularDanoUseCase,
                                 final HistoricoBatalhaAdapter historicoAdapter) {
        this.calcularDanoUseCase = calcularDanoUseCase;
        this.historicoAdapter = historicoAdapter;
    }

    /**
     * Processa a rodada de ataque entre dois combatentes.
     *
     * @param combatenteA primeiro combatente
     * @param combatenteB segundo combatente
     * @param terreno condição da arena
     */
    public void processarTurno(final Pokesal combatenteA, final Pokesal combatenteB, final Terreno terreno) {
        // Validação de SPD (Velocidade) para definir o primeiro atacante
        final Pokesal primeiro;
        final Pokesal segundo;

        if (combatenteA.getVelocidade() >= combatenteB.getVelocidade()) {
            primeiro = combatenteA;
            segundo = combatenteB;
        } else {
            primeiro = combatenteB;
            segundo = combatenteA;
        }

        // Primeiro ataque
        executarAtaque(primeiro, segundo, terreno, false);

        // Contra-ataque se o segundo combatente não foi derrotado
        if (!segundo.estaDerrotado()) {
            executarAtaque(segundo, primeiro, terreno, true);
        } else {
            historicoAdapter.registrarAcao(String.format("%s foi derrotado!", segundo.getNome()));
        }
    }

    private void executarAtaque(final Pokesal atacante,
                                final Pokesal defensor,
                                final Terreno terreno,
                                final boolean isContraAtaque) {
        final ResultadoDano resultado = calcularDanoUseCase.calcular(atacante, defensor, terreno);
        defensor.sofrerDano(resultado.getValorDano());

        if (resultado.isCritico()) {
            historicoAdapter.registrarAcao(String.format("⚡ GOLPE CRÍTICO! %s acertou em cheio!",
                    atacante.getNome()));
        }
        if (resultado.isFuriaAtivada()) {
            historicoAdapter.registrarAcao(String.format("🔥 ADRENALINA ATIVADA! %s está furioso (+30%% ATK)!",
                    atacante.getNome()));
        }

        final String formatoMsg = isContraAtaque
                ? "%s contra-atacou %s causando %d de dano. (HP restante: %d/%d)"
                : "%s atacou %s causando %d de dano. (HP restante: %d/%d)";

        historicoAdapter.registrarAcao(String.format(formatoMsg,
                atacante.getNome(), defensor.getNome(), resultado.getValorDano(),
                defensor.getHpAtual(), defensor.getHpMax()));
    }
}
