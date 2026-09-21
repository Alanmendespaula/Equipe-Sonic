package com.ucsal.pokesal.features.batalha.infrastructure;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HistoricoBatalhaAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricoBatalhaAdapter.class);

    private final List<String> registros = new ArrayList<>();

    public synchronized void registrarAcao(final String mensagem) {
        final String entrada = String.format("[%s] %s", LocalDateTime.now(), mensagem);
        registros.add(entrada);
        LOGGER.info(entrada);
    }

    public synchronized List<String> obterHistorico() {
        return Collections.unmodifiableList(new ArrayList<>(registros));
    }

    public synchronized void limpar() {
        registros.clear();
    }
}
