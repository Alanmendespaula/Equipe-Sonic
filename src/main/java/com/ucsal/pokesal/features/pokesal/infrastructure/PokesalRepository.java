package com.ucsal.pokesal.features.pokesal.infrastructure;

import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de persistência para as entidades PokéSal.
 */
@Repository
public interface PokesalRepository extends JpaRepository<Pokesal, Long> {

    Optional<Pokesal> findByNomeIgnoreCase(String nome);
}
