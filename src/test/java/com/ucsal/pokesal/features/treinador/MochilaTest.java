package com.ucsal.pokesal.features.treinador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ucsal.pokesal.features.treinador.domain.Item;
import com.ucsal.pokesal.features.treinador.domain.LimiteItensExcedidoException;
import com.ucsal.pokesal.features.treinador.domain.Mochila;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MochilaTest {

    private Mochila mochila;

    @BeforeEach
    void setUp() {
        mochila = new Mochila();
    }

    @Test
    @DisplayName("Deve permitir adicionar até 2 itens na mochila com sucesso")
    void devePermitirAteDoisItens() {
        final Item item1 = new Item("Poção", 20);
        final Item item2 = new Item("Super Poção", 50);

        mochila.adicionarItem(item1);
        mochila.adicionarItem(item2);

        assertEquals(2, mochila.getQuantidadeItens());
        assertTrue(mochila.estaCheia());
    }

    @Test
    @DisplayName("Deve lançar LimiteItensExcedidoException ao tentar adicionar um 3º item na mochila")
    void deveLancarExcecaoAoExcederLimiteDeItens() {
        final Item item1 = new Item("Poção A", 20);
        final Item item2 = new Item("Poção B", 30);
        final Item item3 = new Item("Poção C", 50);

        mochila.adicionarItem(item1);
        mochila.adicionarItem(item2);

        final LimiteItensExcedidoException exception = assertThrows(
                LimiteItensExcedidoException.class,
                () -> mochila.adicionarItem(item3)
        );

        assertTrue(exception.getMessage().contains("limite máximo de 2"));
        assertEquals(2, mochila.getQuantidadeItens());
    }

    @Test
    @DisplayName("Deve permitir remover itens e liberar espaço na mochila")
    void devePermitirRemoverItens() {
        final Item item = new Item("Poção", 20);
        mochila.adicionarItem(item);

        final boolean removido = mochila.removerItem(item);

        assertTrue(removido);
        assertEquals(0, mochila.getQuantidadeItens());
    }
}
