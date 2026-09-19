package com.ucsal.pokesal.features.pokesal.application;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import com.ucsal.pokesal.features.pokesal.infrastructure.PokesalRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso responsável pelo gerenciamento e seleção dos PokéSals iniciais.
 */
@Service
public class SelecionarInicialUseCase {

    private final PokesalRepository pokesalRepository;

    public SelecionarInicialUseCase(final PokesalRepository pokesalRepository) {
        this.pokesalRepository = pokesalRepository;
    }

    /**
     * Retorna a lista de todos os PokéSals disponíveis para escolha inicial.
     */
    @Transactional(readOnly = true)
    public List<Pokesal> listarIniciaisDisponiveis() {
        return pokesalRepository.findAll();
    }

    /**
     * Seleciona um PokéSal inicial pelo seu identificador.
     *
     * @param id identificador do PokéSal
     * @return entidade do PokéSal selecionado
     */
    @Transactional(readOnly = true)
    public Pokesal selecionarPorId(final Long id) {
        return pokesalRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("PokéSal inicial não encontrado com ID: " + id));
    }
}
