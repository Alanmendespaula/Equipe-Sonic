package com.ucsal.pokesal;

import com.ucsal.pokesal.core.domain.RegraNegocioException;
import com.ucsal.pokesal.core.domain.TipoElemental;
import com.ucsal.pokesal.features.batalha.application.CalcularDanoUseCase;
import com.ucsal.pokesal.features.batalha.application.ProcessarTurnoUseCase;
import com.ucsal.pokesal.features.batalha.domain.Terreno;
import com.ucsal.pokesal.features.batalha.infrastructure.HistoricoBatalhaAdapter;
import com.ucsal.pokesal.features.pokesal.application.SelecionarInicialUseCase;
import com.ucsal.pokesal.features.pokesal.domain.Pokesal;
import com.ucsal.pokesal.features.pokesal.infrastructure.PokesalRepository;
import com.ucsal.pokesal.features.batalha.domain.ResultadoDano;
import com.ucsal.pokesal.features.treinador.application.ComprarItemUseCase;
import com.ucsal.pokesal.features.treinador.application.UsarItemUseCase;
import com.ucsal.pokesal.features.treinador.domain.Item;
import com.ucsal.pokesal.features.treinador.domain.LimiteItensExcedidoException;
import com.ucsal.pokesal.features.treinador.domain.Treinador;
import com.ucsal.pokesal.features.treinador.infrastructure.TreinadorRepository;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private static final String LINHA_POKESAL =
            "+----+--------------+----------+-----------+---------+---------+---------+";
    private static final String CABECALHO_POKESAL =
            "| ID | Nome         | Tipo     | HP        | Ataque  | Defesa  | Vel(SPD)|";

    private static final String LINHA_TREINADOR =
            "+----+----------------------+-------------------------------+-----------+";
    private static final String CABECALHO_TREINADOR =
            "| ID | Nome do Treinador    | PokéSal Parceiro Ativo        | Moedas 🪙 |";

    private static final String LINHA_MOCHILA =
            "+------+----------------------+-----------------+";
    private static final String CABECALHO_MOCHILA =
            "| Slot | Item                 | Efeito de Cura  |";

    private static final String LINHA_ARENA =
            "+----+----------------------+-------------------------+";
    private static final String CABECALHO_ARENA =
            "| ID | Arena / Terreno      | Bônus Elemental         |";

    private static final String LINHA_HUD =
            "+-------------------------------------------------------------------------+";

    private static final String OPCAO_ESCOLHER = "1";
    private static final String OPCAO_TROCAR_TREINADOR = "2";
    private static final String OPCAO_MOCHILA = "3";
    private static final String OPCAO_COLETAR_ITEM = "4";
    private static final String OPCAO_CENTRO_POKESAL = "5";
    private static final String OPCAO_BATALHA = "6";
    private static final String OPCAO_CATALOGO = "7";
    private static final String OPCAO_SAIR = "8";

    private static final String ACAO_ATACAR = "1";
    private static final String ACAO_USAR_ITEM = "2";
    private static final String ACAO_FUGIR = "3";

    private static final String TERRENO_NEUTRO = "1";
    private static final String TERRENO_VULCANICO = "2";
    private static final String TERRENO_AQUATICO = "3";
    private static final String TERRENO_FLORESTAL = "4";
    private static final String TERRENO_ELETRICO = "5";

    private static final int SALMANDER_HP = 100;
    private static final int SALMANDER_ATK = 32;
    private static final int SALMANDER_DEF = 18;
    private static final int SALMANDER_SPD = 52;

    private static final int SALVASAUR_HP = 110;
    private static final int SALVASAUR_ATK = 25;
    private static final int SALVASAUR_DEF = 24;
    private static final int SALVASAUR_SPD = 45;

    private static final int SALSTOISE_HP = 105;
    private static final int SALSTOISE_ATK = 22;
    private static final int SALSTOISE_DEF = 30;
    private static final int SALSTOISE_SPD = 43;

    private static final int RAPISALT_HP = 95;
    private static final int RAPISALT_ATK = 30;
    private static final int RAPISALT_DEF = 16;
    private static final int RAPISALT_SPD = 75;

    private static final int CURA_POCAO = 30;
    private static final int CURA_SUPER_POCAO = 60;
    private static final int PRECO_POCAO = 50;
    private static final int PRECO_SUPER_POCAO = 100;
    private static final int RECOMPENSA_VITORIA = 100;

    private final PokesalRepository pokesalRepository;
    private final TreinadorRepository treinadorRepository;
    private final SelecionarInicialUseCase selecionarInicialUseCase;
    private final ProcessarTurnoUseCase processarTurnoUseCase;
    private final UsarItemUseCase usarItemUseCase;
    private final ComprarItemUseCase comprarItemUseCase;
    private final HistoricoBatalhaAdapter historicoAdapter;
    private final CalcularDanoUseCase calcularDanoUseCase;

    public Main(final PokesalRepository pokesalRepository,
            final TreinadorRepository treinadorRepository,
            final SelecionarInicialUseCase selecionarInicialUseCase,
            final ProcessarTurnoUseCase processarTurnoUseCase,
            final UsarItemUseCase usarItemUseCase,
            final ComprarItemUseCase comprarItemUseCase,
            final HistoricoBatalhaAdapter historicoAdapter,
            final CalcularDanoUseCase calcularDanoUseCase) {
        this.pokesalRepository = pokesalRepository;
        this.treinadorRepository = treinadorRepository;
        this.selecionarInicialUseCase = selecionarInicialUseCase;
        this.processarTurnoUseCase = processarTurnoUseCase;
        this.usarItemUseCase = usarItemUseCase;
        this.comprarItemUseCase = comprarItemUseCase;
        this.historicoAdapter = historicoAdapter;
        this.calcularDanoUseCase = calcularDanoUseCase;
    }

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(final String... args) {
        inicializarDadosSeNecessario();
        Treinador treinador = obterOuCriarTreinador();

        System.out.println("==================================================");
        System.out.println("    BEM-VINDO AO SIMULADOR DE BATALHA POKÉSAL     ");
        System.out.println("==================================================");

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
            boolean executando = true;
            while (executando) {
                exibirMenuPrincipal(treinador);
                System.out.print("Escolha uma opção: ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                final String opcao = scanner.nextLine().trim();

                switch (opcao) {
                    case OPCAO_ESCOLHER -> escolherPokesalAtivo(treinador, scanner);
                    case OPCAO_TROCAR_TREINADOR -> treinador = trocarTreinador(scanner, treinador);
                    case OPCAO_MOCHILA -> gerenciarMochila(treinador, scanner);
                    case OPCAO_COLETAR_ITEM -> coletarItem(treinador);
                    case OPCAO_CENTRO_POKESAL -> visitarCentroPokesal(treinador, scanner);
                    case OPCAO_BATALHA -> iniciarBatalha(treinador, scanner);
                    case OPCAO_CATALOGO -> listarCatalogo();
                    case OPCAO_SAIR -> {
                        System.out.println("\nObrigado por jogar PokéSal Simulator! Até a próxima.");
                        executando = false;
                    }
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }
    }

    private void exibirMenuPrincipal(final Treinador treinador) {
        final String nomePokesal = treinador.getPokesalAtivo() != null
                ? String.format("%s (HP: %d/%d)", treinador.getPokesalAtivo().getNome(),
                        treinador.getPokesalAtivo().getHpAtual(), treinador.getPokesalAtivo().getHpMax())
                : "Nenhum";

        System.out.println("\n--------------------------------------------------");
        System.out.println(" Treinador: " + treinador.getNome() + " | PokéSal: " + nomePokesal);
        System.out.println(" Mochila: " + treinador.getMochila().getQuantidadeItens() + "/2 itens | PokéMoedas: "
                + treinador.getMoedas() + " 🪙");
        System.out.println("--------------------------------------------------");
        System.out.println(" 1. Escolher / Trocar PokéSal Ativo");
        System.out.println(" 2. Trocar de Treinador");
        System.out.println(" 3. Ver Mochila e Usar Item");
        System.out.println(" 4. Coletar Item no Chão (Testar limite de 2 itens)");
        System.out.println(" 5. Centro PokéSal & Loja de Itens");
        System.out.println(" 6. Iniciar Batalha");
        System.out.println(" 7. Listar Catálogo Completo de PokéSals");
        System.out.println(" 8. Sair");
        System.out.println("--------------------------------------------------");
    }

    private Treinador trocarTreinador(final Scanner scanner, final Treinador atual) {
        System.out.println("\n============================= TREINADORES NO BANCO =============================");
        System.out.println(LINHA_TREINADOR);
        System.out.println(CABECALHO_TREINADOR);
        System.out.println(LINHA_TREINADOR);
        final List<Treinador> todos = treinadorRepository.findAll().stream()
                .sorted(Comparator.comparing(Treinador::getId))
                .toList();
        for (final Treinador t : todos) {
            final String nomeP = t.getPokesalAtivo() != null
                    ? String.format("%s (%s)", t.getPokesalAtivo().getNome(), t.getPokesalAtivo().getTipo())
                    : "Nenhum";
            System.out.printf("| %2d | %-20s | %-29s | %-9d |%n", t.getId(), t.getNome(), nomeP, t.getMoedas());
        }
        System.out.println(LINHA_TREINADOR);
        System.out.print("Digite o ID do Treinador desejado: ");
        if (!scanner.hasNextLine()) {
            return atual;
        }
        final String entrada = scanner.nextLine().trim();
        try {
            final Long id = Long.parseLong(entrada);
            final Treinador selecionado = treinadorRepository.findById(id).orElse(atual);
            garantirItensMochila(selecionado);
            System.out.println("✅ Treinador ativo alterado para: " + selecionado.getNome());
            return selecionado;
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido. Mantendo treinador atual.");
            return atual;
        }
    }

    private void escolherPokesalAtivo(final Treinador treinador, final Scanner scanner) {
        System.out.println("\n=========================== ESCOLHA SEU POKÉSAL ATIVO ===========================");
        System.out.println(LINHA_POKESAL);
        System.out.println(CABECALHO_POKESAL);
        System.out.println(LINHA_POKESAL);
        final List<Pokesal> ordenados = selecionarInicialUseCase.listarIniciaisDisponiveis().stream()
                .sorted(Comparator.comparing(Pokesal::getId))
                .toList();
        for (final Pokesal p : ordenados) {
            System.out.printf("| %2d | %-12s | %-8s |  %3d/%-3d  |   %3d   |   %3d   |   %3d   |%n",
                    p.getId(), p.getNome(), p.getTipo(), p.getHpAtual(), p.getHpMax(),
                    p.getAtaque(), p.getDefesa(), p.getVelocidade());
        }
        System.out.println(LINHA_POKESAL);
        System.out.print("Digite o ID do PokéSal desejado: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        final String entrada = scanner.nextLine().trim();
        try {
            final Long id = Long.parseLong(entrada);
            final Pokesal escolhido = selecionarInicialUseCase.selecionarPorId(id);

            final List<Treinador> outros = treinadorRepository.findAll();
            for (final Treinador outro : outros) {
                if (!outro.getId().equals(treinador.getId()) && outro.getPokesalAtivo() != null
                        && outro.getPokesalAtivo().getId().equals(escolhido.getId())) {
                    outro.setPokesalAtivo(null);
                    treinadorRepository.save(outro);
                }
            }

            treinador.setPokesalAtivo(escolhido);
            treinadorRepository.save(treinador);
            System.out.println("✅ " + escolhido.getNome() + " agora é o seu PokéSal ativo!");
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido. Por favor, digite um número.");
        } catch (RegraNegocioException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void gerenciarMochila(final Treinador treinador, final Scanner scanner) {
        System.out.println("\n====================== MOCHILA DO TREINADOR ======================");
        final List<Item> itens = treinador.getMochila().getItens();
        if (itens.isEmpty()) {
            System.out.println("Sua mochila está vazia no momento.");
            return;
        }

        System.out.println(LINHA_MOCHILA);
        System.out.println(CABECALHO_MOCHILA);
        System.out.println(LINHA_MOCHILA);
        for (int i = 0; i < itens.size(); i++) {
            final Item item = itens.get(i);
            System.out.printf("|  %d   | %-20s | +%-4d HP        |%n",
                    i + 1, item.getNome(), item.getPontosCura());
        }
        System.out.println(LINHA_MOCHILA);
        System.out.println(" [0] Voltar");
        System.out.print("Escolha o item para usar no seu PokéSal ativo: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        final String entrada = scanner.nextLine().trim();
        try {
            final int index = Integer.parseInt(entrada) - 1;
            if (index >= 0 && index < itens.size()) {
                final Item item = itens.get(index);
                usarItemUseCase.usarItem(treinador, item);
                if (treinador.getPokesalAtivo() != null) {
                    pokesalRepository.save(treinador.getPokesalAtivo());
                }
                System.out.printf("✅ Você usou %s! HP atual: %d/%d%n",
                        item.getNome(),
                        treinador.getPokesalAtivo().getHpAtual(),
                        treinador.getPokesalAtivo().getHpMax());
            }
        } catch (NumberFormatException | RegraNegocioException e) {
            System.out.println("❌ Erro ao usar item: " + e.getMessage());
        }
    }

    private void coletarItem(final Treinador treinador) {
        System.out.println("\n--- Procurando itens pelo caminho... ---");
        try {
            final Item novoItem = new Item("Poção", CURA_POCAO);
            treinador.getMochila().adicionarItem(novoItem);
            System.out.println("✅ Você encontrou uma Poção (+30 HP) e colocou na mochila!");
        } catch (LimiteItensExcedidoException e) {
            System.out.println("⚠️ " + e.getMessage());
            System.out.println("Dica: Use um item antes de coletar novos.");
        }
    }

    private void visitarCentroPokesal(final Treinador treinador, final Scanner scanner) {
        final Pokesal ativo = treinador.getPokesalAtivo();
        System.out.println("\n=================== CENTRO POKÉSAL & LOJA ===================");
        System.out.printf(" Treinador: %s | Saldo: %d PokéMoedas 🪙%n",
                treinador.getNome(), treinador.getMoedas());
        if (ativo != null) {
            System.out.printf(" Parceiro: %s (HP: %d/%d)%n",
                    ativo.getNome(), ativo.getHpAtual(), ativo.getHpMax());
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println(" 1. Curar e Restaurar PokéSal Ativo (Gratuito)");
        System.out.println(" 2. Comprar Poção (+30 HP | 50 PokéMoedas)");
        System.out.println(" 3. Comprar Super Poção (+60 HP | 100 PokéMoedas)");
        System.out.println(" 4. Voltar ao Menu Principal");
        System.out.print("Escolha uma opção: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        final String opc = scanner.nextLine().trim();
        switch (opc) {
            case "1" -> {
                if (ativo == null) {
                    System.out.println("❌ Você não possui nenhum PokéSal ativo para curar.");
                    return;
                }
                ativo.restaurar();
                pokesalRepository.save(ativo);
                System.out.printf("💖 %s foi totalmente restaurado (%d/%d HP)!%n",
                        ativo.getNome(), ativo.getHpAtual(), ativo.getHpMax());
            }
            case "2" -> {
                try {
                    comprarItemUseCase.comprarItem(treinador, new Item("Poção", CURA_POCAO), PRECO_POCAO);
                    System.out.printf("✅ Poção comprada com sucesso! Saldo restante: %d PokéMoedas.%n",
                            treinador.getMoedas());
                } catch (RegraNegocioException e) {
                    System.out.println("❌ Não foi possível comprar: " + e.getMessage());
                }
            }
            case "3" -> {
                try {
                    comprarItemUseCase.comprarItem(treinador,
                            new Item("Super Poção", CURA_SUPER_POCAO), PRECO_SUPER_POCAO);
                    System.out.printf("✅ Super Poção comprada com sucesso! Saldo restante: %d PokéMoedas.%n",
                            treinador.getMoedas());
                } catch (RegraNegocioException e) {
                    System.out.println("❌ Não foi possível comprar: " + e.getMessage());
                }
            }
            default -> System.out.println("Retornando ao menu principal...");
        }
    }

    private void iniciarBatalha(final Treinador treinador, final Scanner scanner) {
        final Pokesal seuPokesal = treinador.getPokesalAtivo();
        if (seuPokesal == null) {
            System.out.println("❌ Você precisa escolher um PokéSal ativo antes de batalhar!");
            return;
        }
        if (seuPokesal.estaDerrotado()) {
            System.out.println("❌ Seu PokéSal está desmaiado (0 HP)! Visite o Centro PokéSal primeiro.");
            return;
        }

        final Terreno terreno = selecionarTerreno(scanner);
        final Pokesal oponente = sortearOponente(seuPokesal);

        System.out.println("\n==================================================");
        System.out.printf("  BATALHA INICIADA NA ARENA: %s%n", terreno.getDescricao());
        System.out.printf("  [VOCÊ] %s (%s) vs [ADVERSÁRIO] %s (%s)%n",
                seuPokesal.getNome(), seuPokesal.getTipo(), oponente.getNome(), oponente.getTipo());
        System.out.println("==================================================");

        historicoAdapter.limpar();
        boolean emCombate = true;

        while (emCombate) {
            System.out.println("\n" + LINHA_HUD);
            final String descTerreno = String.format("ARENA: %s", terreno.getDescricao());
            System.out.printf("| %-71s |%n", descTerreno);
            System.out.println(LINHA_HUD);
            final String infoVoce = String.format("%s (%s)", seuPokesal.getNome(), seuPokesal.getTipo());
            final String infoOpo = String.format("%s (%s)", oponente.getNome(), oponente.getTipo());
            System.out.printf("| [VOCÊ]       %-22s | HP: %3d/%-3d | SPD: %-3d            |%n",
                    infoVoce, seuPokesal.getHpAtual(), seuPokesal.getHpMax(), seuPokesal.getVelocidade());
            System.out.printf("| [OPONENTE]   %-22s | HP: %3d/%-3d | SPD: %-3d            |%n",
                    infoOpo, oponente.getHpAtual(), oponente.getHpMax(), oponente.getVelocidade());
            System.out.println(LINHA_HUD);
            System.out.println(" 1. Atacar");
            System.out.println(" 2. Usar Item da Mochila");
            System.out.println(" 3. Fugir");
            System.out.print("Ação: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            final String acao = scanner.nextLine().trim();

            switch (acao) {
                case ACAO_ATACAR -> {
                    processarTurnoUseCase.processarTurno(seuPokesal, oponente, terreno);
                    pokesalRepository.save(seuPokesal);
                    exibirHistoricoRecente();

                    if (oponente.estaDerrotado()) {
                        System.out.println("\n🏆 VITÓRIA! Você derrotou " + oponente.getNome() + "!");
                        treinador.adicionarMoedas(RECOMPENSA_VITORIA);
                        treinadorRepository.save(treinador);
                        System.out.printf("💰 Você recebeu +%d PokéMoedas de premiação! (Saldo: %d)%n",
                                RECOMPENSA_VITORIA, treinador.getMoedas());
                        emCombate = false;
                    } else if (seuPokesal.estaDerrotado()) {
                        System.out.println("\n💀 DERROTA! Seu " + seuPokesal.getNome() + " desmaiou.");
                        emCombate = false;
                    }
                }
                case ACAO_USAR_ITEM -> {
                    gerenciarMochila(treinador, scanner);
                    if (!oponente.estaDerrotado() && !seuPokesal.estaDerrotado()) {
                        System.out.println("\nO adversário aproveitou sua ação para contra-atacar!");
                        final ResultadoDano resOponente = calcularDanoUseCase.calcular(
                                oponente, seuPokesal, terreno);
                        seuPokesal.sofrerDano(resOponente.getValorDano());
                        pokesalRepository.save(seuPokesal);
                        if (resOponente.isCritico()) {
                            System.out.println("⚡ GOLPE CRÍTICO DO ADVERSÁRIO! Dano massivo sofrido!");
                        }
                        if (resOponente.isFuriaAtivada()) {
                            System.out.println("🔥 O ADVERSÁRIO ENTROU EM FÚRIA (+30% ATK)!");
                        }
                        System.out.printf("%s causou %d de dano no seu %s! (HP: %d/%d)%n",
                                oponente.getNome(), resOponente.getValorDano(), seuPokesal.getNome(),
                                seuPokesal.getHpAtual(), seuPokesal.getHpMax());
                        if (seuPokesal.estaDerrotado()) {
                            System.out.println("\n💀 DERROTA! Seu " + seuPokesal.getNome() + " desmaiou.");
                            emCombate = false;
                        }
                    }
                }
                case ACAO_FUGIR -> {
                    System.out.println("🏃 Você fugiu da batalha com segurança!");
                    emCombate = false;
                }
                default -> System.out.println("Ação inválida!");
            }
        }
    }

    private Terreno selecionarTerreno(final Scanner scanner) {
        System.out.println("\n======================== ARENAS DE BATALHA ========================");
        System.out.println(LINHA_ARENA);
        System.out.println(CABECALHO_ARENA);
        System.out.println(LINHA_ARENA);
        System.out.println("|  1 | Campo Neutro         | Nenhum (1.0x padrão)    |");
        System.out.println("|  2 | Terreno Vulcânico    | FOGO (+20% de dano)     |");
        System.out.println("|  3 | Terreno Aquático     | AGUA (+20% de dano)     |");
        System.out.println("|  4 | Terreno Florestal    | PLANTA (+20% de dano)   |");
        System.out.println("|  5 | Terreno Elétrico     | ELETRICO (+20% de dano) |");
        System.out.println(LINHA_ARENA);
        System.out.print("Escolha o terreno da arena [1-5]: ");

        if (!scanner.hasNextLine()) {
            return Terreno.NEUTRO;
        }
        final String escolha = scanner.nextLine().trim();
        return switch (escolha) {
            case TERRENO_VULCANICO -> Terreno.VULCANICO;
            case TERRENO_AQUATICO -> Terreno.AQUATICO;
            case TERRENO_FLORESTAL -> Terreno.FLORESTAL;
            case TERRENO_ELETRICO -> Terreno.ELETRICO;
            default -> Terreno.NEUTRO;
        };
    }

    private Pokesal sortearOponente(final Pokesal seuPokesal) {
        final List<Pokesal> todos = pokesalRepository.findAll();
        final List<Pokesal> outros = todos.stream()
                .filter(p -> !p.getId().equals(seuPokesal.getId()))
                .toList();
        final Pokesal base = outros.isEmpty() ? seuPokesal : outros.get(new Random().nextInt(outros.size()));

        return new Pokesal("Selvagem " + base.getNome(),
                base.getTipo(),
                base.getHpMax(),
                base.getAtaque(),
                base.getDefesa(),
                base.getVelocidade());
    }

    private void exibirHistoricoRecente() {
        System.out.println("\n--- Eventos da Rodada ---");
        for (final String registro : historicoAdapter.obterHistorico()) {
            System.out.println("  " + registro);
        }
        historicoAdapter.limpar();
    }

    private void listarCatalogo() {
        System.out.println("\n=========================== CATÁLOGO GERAL DE POKÉSAL ===========================");
        System.out.println(LINHA_POKESAL);
        System.out.println(CABECALHO_POKESAL);
        System.out.println(LINHA_POKESAL);
        final List<Pokesal> ordenados = pokesalRepository.findAll().stream()
                .sorted(Comparator.comparing(Pokesal::getId))
                .toList();
        for (final Pokesal p : ordenados) {
            System.out.printf("| %2d | %-12s | %-8s |  %3d/%-3d  |   %3d   |   %3d   |   %3d   |%n",
                    p.getId(), p.getNome(), p.getTipo(), p.getHpAtual(), p.getHpMax(),
                    p.getAtaque(), p.getDefesa(), p.getVelocidade());
        }
        System.out.println(LINHA_POKESAL);
    }

    private void inicializarDadosSeNecessario() {
        if (pokesalRepository.count() == 0) {
            final Pokesal salmander = new Pokesal("Salmander", TipoElemental.FOGO,
                    SALMANDER_HP, SALMANDER_ATK, SALMANDER_DEF, SALMANDER_SPD);
            final Pokesal salvasaur = new Pokesal("Salvasaur", TipoElemental.PLANTA,
                    SALVASAUR_HP, SALVASAUR_ATK, SALVASAUR_DEF, SALVASAUR_SPD);
            final Pokesal salstoise = new Pokesal("Salstoise", TipoElemental.AGUA,
                    SALSTOISE_HP, SALSTOISE_ATK, SALSTOISE_DEF, SALSTOISE_SPD);
            final Pokesal rapisalt = new Pokesal("Rapisalt", TipoElemental.ELETRICO,
                    RAPISALT_HP, RAPISALT_ATK, RAPISALT_DEF, RAPISALT_SPD);

            pokesalRepository.saveAll(List.of(salmander, salvasaur, salstoise, rapisalt));
        }
    }

    private Treinador obterOuCriarTreinador() {
        final List<Treinador> existentes = treinadorRepository.findAll();
        if (!existentes.isEmpty()) {
            final Treinador selecionado = existentes.get(0);
            garantirItensMochila(selecionado);
            return selecionado;
        }

        final Treinador novo = new Treinador("Ash UCSAL");
        final List<Pokesal> disponiveis = pokesalRepository.findAll();
        if (!disponiveis.isEmpty()) {
            novo.setPokesalAtivo(disponiveis.get(0));
        }
        garantirItensMochila(novo);
        return treinadorRepository.save(novo);
    }

    private void garantirItensMochila(final Treinador treinador) {
        if (treinador.getMochila().getQuantidadeItens() == 0) {
            treinador.getMochila().adicionarItem(new Item("Poção", CURA_POCAO));
            treinador.getMochila().adicionarItem(new Item("Super Poção", CURA_SUPER_POCAO));
        }
    }
}
