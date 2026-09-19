package com.ucsal.pokesal.features.pokesal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ucsal.pokesal.core.domain.TipoElemental;
import com.ucsal.pokesal.features.batalha.application.CalcularDanoUseCase;
import com.ucsal.pokesal.features.batalha.domain.ResultadoDano;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes Unitários para o Requisito Autoral 2: Habilidade Passiva Adrenalina / Fúria.
 */
class FuriaPassivaTest {

    @Test
    @DisplayName("Habilidade Fúria deve permanecer inativa quando HP estiver acima de 30%")
    void furiaDevePermanecerInativaComHpAlto() {
        final Pokesal pokesal = new Pokesal("Salmander", TipoElemental.FOGO, 100, 30, 20, 50);

        // 100% HP
        assertFalse(pokesal.isFuriaAtiva());

        // Reduzindo para 31% HP (31/100) -> ainda inativa
        pokesal.sofrerDano(69);
        assertEquals(31, pokesal.getHpAtual());
        assertFalse(pokesal.isFuriaAtiva());
    }

    @Test
    @DisplayName("Habilidade Fúria deve ativar quando HP estiver menor ou igual a 30%")
    void furiaDeveAtivarComHpBaixo() {
        final Pokesal pokesal = new Pokesal("Salmander", TipoElemental.FOGO, 100, 30, 20, 50);

        // Reduzindo para exatamente 30% HP (30/100) -> deve ativar
        pokesal.sofrerDano(70);
        assertEquals(30, pokesal.getHpAtual());
        assertTrue(pokesal.isFuriaAtiva());

        // Reduzindo para 10% HP -> continua ativa
        pokesal.sofrerDano(20);
        assertEquals(10, pokesal.getHpAtual());
        assertTrue(pokesal.isFuriaAtiva());

        // Ao ser derrotado (0 HP) -> desativa
        pokesal.sofrerDano(10);
        assertTrue(pokesal.estaDerrotado());
        assertFalse(pokesal.isFuriaAtiva());
    }

    @Test
    @DisplayName("CalcularDanoUseCase deve aplicar bônus de 30% de dano quando o atacante estiver em Fúria")
    void deveAplicarBonusDeDanoComFuriaAtiva() {
        final CalcularDanoUseCase useCase = new CalcularDanoUseCase((atacante, defensor) -> false);

        final Pokesal atacante = new Pokesal("Salmander", TipoElemental.FOGO, 100, 30, 20, 50);
        final Pokesal defensor = new Pokesal("Salvasaur", TipoElemental.PLANTA, 100, 25, 20, 45);

        // Ativa a fúria no atacante deixando com 25 HP (<= 30%)
        atacante.sofrerDano(75);
        assertTrue(atacante.isFuriaAtiva());

        // Base: (30 - 10) * 2.0 = 40. Com Fúria (+30% = 1.3): round(40 * 1.3) = 52.
        final ResultadoDano resultado = useCase.calcular(atacante, defensor, Terreno.NEUTRO);

        assertTrue(resultado.isFuriaAtivada());
        assertEquals(52, resultado.getValorDano());
    }
}
