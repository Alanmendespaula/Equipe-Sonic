package com.ucsal.pokesal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada principal da aplicação PokéSal Simulator.
 */
@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(final String... args) {
        System.out.println("==================================================");
        System.out.println("    BEM-VINDO AO SIMULADOR DE BATALHA POKÉSAL     ");
        System.out.println("            Arquitetura: Clean + DDD              ");
        System.out.println("==================================================");
    }
}
