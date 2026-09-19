package com.ucsal.pokesal.features.batalha.infrastructure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Adaptador de infraestrutura para persistência e logging de eventos do histórico de batalha.
 */
@Component
public class HistoricoBatalhaAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricoBatalhaAdapter.class);

    private final List<String> registros = new ArrayList<>();

    /**
     * Registra uma ação ou evento ocorrido durante a batalha.
     *
     * @param mensagem descrição do evento
     */
    public synchronized void registrarAcao(final String mensagem) {
        final String entrada = String.format("[%s] %s", LocalDateTime.now(), mensagem);
        registros.add(entrada);
        LOGGER.info(entrada);
    }

    /**
     * Retorna a lista imutável dos registros de batalha até o momento.
     */
    public synchronized List<String> obterHistorico() {
        return Collections.unmodifiableList(new ArrayList<>(registros));
    }

    /**
     * Limpa os registros de combate da sessão atual.
     */
    public synchronized void limpar() {
        registros.clear();
    }
}
