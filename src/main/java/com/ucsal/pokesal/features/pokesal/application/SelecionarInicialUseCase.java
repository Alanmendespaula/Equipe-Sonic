package com.ucsal.pokesal.features.pokesal.application;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import com.ucsal.pokesal.features.pokesal.infrastructure.PokesalRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SelecionarInicialUseCase {

    private final PokesalRepository pokesalRepository;

    public SelecionarInicialUseCase(final PokesalRepository pokesalRepository) {
        this.pokesalRepository = pokesalRepository;
    }

    @Transactional(readOnly = true)
    public List<Pokesal> listarIniciaisDisponiveis() {
        return pokesalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pokesal selecionarPorId(final Long id) {
        return pokesalRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("PokéSal inicial não encontrado com ID: " + id));
    }
}
