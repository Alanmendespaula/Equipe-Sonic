package com.ucsal.pokesal.features.batalha.application;

import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.batalha.infrastructure.HistoricoBatalhaAdapter;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.springframework.stereotype.Service;

/**
 * Orquestra o processamento de um turno de batalha, determinando prioridade de velocidade e resolução de dano.
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
        final int danoPrimeiro = calcularDanoUseCase.calcularDano(primeiro, segundo, terreno);
        segundo.sofrerDano(danoPrimeiro);
        historicoAdapter.registrarAcao(String.format("%s atacou %s causando %d de dano. (HP restante: %d/%d)",
                primeiro.getNome(), segundo.getNome(), danoPrimeiro, segundo.getHpAtual(), segundo.getHpMax()));

        // Contra-ataque se o segundo combatente não foi derrotado
        if (!segundo.estaDerrotado()) {
            final int danoSegundo = calcularDanoUseCase.calcularDano(segundo, primeiro, terreno);
            final String mensagemContraAtaque = "%s contra-atacou %s causando %d de dano. (HP restante: %d/%d)";
            historicoAdapter.registrarAcao(String.format(mensagemContraAtaque,
                    segundo.getNome(), primeiro.getNome(), danoSegundo, primeiro.getHpAtual(), primeiro.getHpMax()));
        } else {
            historicoAdapter.registrarAcao(String.format("%s foi derrotado!", segundo.getNome()));
        }
    }
}
