package com.ucsal.pokesal.features.treinador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.features.treinador.application.ComprarItemUseCase;
import com.ucsal.pokesal.features.treinador.domain.Item;
import com.ucsal.pokesal.features.treinador.domain.LimiteItensExcedidoException;
import com.ucsal.pokesal.features.treinador.domain.SaldoInsuficienteException;
import com.ucsal.pokesal.features.treinador.domain.Treinador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes Unitários para o Requisito Autoral 3: Sistema de Economia do Treinador e Loja.
 */
class LojaEconomiaTest {

    private ComprarItemUseCase comprarItemUseCase;
    private Treinador treinador;

    @BeforeEach
    void setUp() {
        comprarItemUseCase = new ComprarItemUseCase(null);
        treinador = new Treinador("Ash UCSAL", 100);
    }

    @Test
    @DisplayName("Treinador deve conseguir comprar item tendo saldo e espaço na mochila")
    void deveComprarItemComSucesso() {
        final Item pocao = new Item("Poção de Cura", 50);
        final int preco = 40;

        comprarItemUseCase.comprarItem(treinador, pocao, preco);

        assertEquals(60, treinador.getMoedas());
        assertEquals(1, treinador.getMochila().getQuantidadeItens());
        assertTrue(treinador.getMochila().getItens().contains(pocao));
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException ao tentar comprar item mais caro que o saldo")
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        final Item superPocao = new Item("Super Poção", 100);
        final int precoElevado = 150;

        final SaldoInsuficienteException exception = assertThrows(
                SaldoInsuficienteException.class,
                () -> comprarItemUseCase.comprarItem(treinador, superPocao, precoElevado)
        );

        assertTrue(exception.getMessage().contains("Saldo insuficiente"));
        assertEquals(100, treinador.getMoedas());
        assertEquals(0, treinador.getMochila().getQuantidadeItens());
    }

    @Test
    @DisplayName("Deve lançar LimiteItensExcedidoException ao tentar comprar item com a mochila cheia")
    void deveLancarExcecaoQuandoMochilaCheia() {
        // Enche a mochila com 2 itens
        treinador.getMochila().adicionarItem(new Item("Item 1", 20));
        treinador.getMochila().adicionarItem(new Item("Item 2", 20));

        final Item novoItem = new Item("Item Extra", 30);

        final LimiteItensExcedidoException exception = assertThrows(
                LimiteItensExcedidoException.class,
                () -> comprarItemUseCase.comprarItem(treinador, novoItem, 30)
        );

        assertTrue(exception.getMessage().contains("Mochila cheia"));
        assertEquals(100, treinador.getMoedas(), "Nenhuma moeda deve ser debitada se a compra for rejeitada");
    }

    @Test
    @DisplayName("Deve permitir adicionar moedas de recompensa de batalha ao saldo do treinador")
    void deveAdicionarMoedasComSucesso() {
        treinador.adicionarMoedas(100);
        assertEquals(200, treinador.getMoedas());
    }

    @Test
    @DisplayName("Deve validar valores inválidos de moedas lançando RegraNegocioException")
    void deveValidarOperacoesInvalidasDeMoedas() {
        assertThrows(RegraNegocioException.class, () -> treinador.adicionarMoedas(0));
        assertThrows(RegraNegocioException.class, () -> treinador.adicionarMoedas(-10));
        assertThrows(RegraNegocioException.class, () -> treinador.debitarMoedas(0));
        assertThrows(RegraNegocioException.class, () -> treinador.debitarMoedas(-5));
    }
}
