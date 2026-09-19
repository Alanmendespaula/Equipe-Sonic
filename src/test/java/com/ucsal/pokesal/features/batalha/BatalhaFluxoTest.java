package com.ucsal.pokesal.features.batalha;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ucsal.pokesal.core.domain.TipoElemental;
import com.ucsal.pokesal.features.batalha.application.CalcularDanoUseCase;
import com.ucsal.pokesal.features.batalha.application.ProcessarTurnoUseCase;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.batalha.infrastructure.HistoricoBatalhaAdapter;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BatalhaFluxoTest {

    private CalcularDanoUseCase calcularDanoUseCase;
    private HistoricoBatalhaAdapter historicoAdapter;
    private ProcessarTurnoUseCase processarTurnoUseCase;

    @BeforeEach
    void setUp() {
        calcularDanoUseCase = new CalcularDanoUseCase((atacante, defensor) -> false);
        historicoAdapter = new HistoricoBatalhaAdapter();
        processarTurnoUseCase = new ProcessarTurnoUseCase(calcularDanoUseCase, historicoAdapter);
    }

    @Test
    @DisplayName("Dano calculado deve considerar vantagem de tipo e respeitar dano mínimo de 1")
    void deveCalcularDanoCorretamente() {
        final Pokesal atacante = new Pokesal("Salmander", TipoElemental.FOGO, 100, 30, 20, 50);
        final Pokesal defensor = new Pokesal("Salvasaur", TipoElemental.PLANTA, 100, 25, 20, 45);

        // FOGO vs PLANTA: mult 2.0x. Base = 30 - (20 * 0.5) = 20. Dano = 20 * 2.0 = 40.
        final int dano = calcularDanoUseCase.calcularDano(atacante, defensor, Terreno.NEUTRO);
        assertEquals(40, dano);

        // Cenário de defesa extrema: deve garantir dano mínimo de 1
        final Pokesal defensorCouraçado = new Pokesal("Salstoise", TipoElemental.AGUA, 100, 10, 200, 30);
        final int danoMinimo = calcularDanoUseCase.calcularDano(atacante, defensorCouraçado, Terreno.NEUTRO);
        assertEquals(1, danoMinimo);
    }

    @Test
    @DisplayName("Combatente com maior velocidade (SPD) deve atacar primeiro no turno")
    void deveRespeitarOrdemDeVelocidadeNoTurno() {
        // Rapisalt tem SPD 80 e 100 HP, atacará primeiro e derrotará Lentasalt (que tem apenas 10 HP)
        final Pokesal atacanteRapido = new Pokesal("Rapisalt", TipoElemental.FOGO, 100, 50, 20, 80);
        final Pokesal defensorLento = new Pokesal("Lentasalt", TipoElemental.PLANTA, 10, 50, 10, 20);

        processarTurnoUseCase.processarTurno(atacanteRapido, defensorLento, Terreno.NEUTRO);

        assertTrue(defensorLento.estaDerrotado(), "O defensor mais lento deve ser derrotado antes de contra-atacar");
        assertEquals(100, atacanteRapido.getHpAtual(), "O atacante mais rápido não deve ter sofrido contra-ataque");
    }
}
