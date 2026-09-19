package com.ucsal.pokesal.features.treinador.infrastructure;

import com.ucsal.pokesal.features.treinador.domain.Treinador;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreinadorRepository extends JpaRepository<Treinador, Long> {

    Optional<Treinador> findByNomeIgnoreCase(String nome);
}
