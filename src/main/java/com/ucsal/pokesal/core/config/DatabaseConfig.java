package com.ucsal.pokesal.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configurações centrais de persistência e repositórios JPA.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ucsal.pokesal.features")
public class DatabaseConfig {
    // Configurações globais de conexão e mapeamento ORM
}
