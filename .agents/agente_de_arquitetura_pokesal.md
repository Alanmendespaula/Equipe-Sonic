# Agente Arquitetural - Simulador de Batalha PokeSal

## 1. Visão Arquitetural
O projeto utiliza **Java 17.0.20.1** com um banco de dados **PostgreSQL**. A arquitetura é baseada em **Clean Architecture** e **DDD**, estruturada através de **Vertical Slices**. Isso significa que, em vez de agrupar o código por camadas técnicas (todos os controllers juntos, todos os repositórios juntos), o código é agrupado por *Features* (ex: Batalha, Treinador, Pokesal). Dentro de cada fatia vertical, as regras de negócio (Domínio) ficam isoladas da infraestrutura (ORM, Banco de Dados).

Para orquestrar a infraestrutura local (PostgreSQL e SonarQube), o projeto adotará `docker-compose`, com um `Makefile` centralizando os comandos de build, testes e linting para facilitar o fluxo de desenvolvimento.

## 2. Árvore da Arquitetura do Projeto

```text
pokesal-simulator/
├── Makefile                                # Comandos atalhos (make up, make test, make sonar)
├── docker-compose.yml                      # Contêineres do PostgreSQL e SonarQube
├── AI_DECLARATION.md                       # Registro obrigatório de interações com IA
├── src/
│   ├── main/
│   │   ├── java/com/ucsal/pokesal/
│   │   │   ├── core/                       # Shared Kernel (Elementos compartilhados)
│   │   │   │   ├── domain/                 # Enums globais (TipoElemental) e Exceções base
│   │   │   │   └── config/                 # Configurações globais do ORM/DB
│   │   │   ├── features/                   # Vertical Slices (Crescimento Verticalizado)
│   │   │   │   ├── pokesal/                # Feature: Gerenciamento dos Iniciais
│   │   │   │   │   ├── domain/             # Entidade Pokesal (HP, ATK, DEF, SPD)
│   │   │   │   │   ├── application/        # Casos de uso (Ex: SelecionarInicial)
│   │   │   │   │   └── infrastructure/     # Repositório JPA/Hibernate e mapeamento ORM
│   │   │   │   ├── batalha/                # Feature: Sistema de Batalha
│   │   │   │   │   ├── domain/             # Matriz de Vantagens e Efeitos de Terreno
│   │   │   │   │   ├── application/        # Casos de uso (Ex: ProcessarTurno, CalcularDano)
│   │   │   │   │   └── infrastructure/     # Adaptadores para salvar histórico da batalha
│   │   │   │   └── treinador/              # Feature: Treinadores e Mochila
│   │   │   │       ├── domain/             # Regras de limite de 2 itens
│   │   │   │       ├── application/        # Casos de uso (Ex: UsarItem)
│   │   │   │       └── infrastructure/     # Repositório de dados do treinador
│   │   │   └── Main.java                   # Ponto de entrada (CLI ou Setup da API)
│   │   └── resources/
│   │       ├── application.properties      # Conexão com o PostgreSQL
│   │       └── checkstyle.xml              # Regras do Google Java Style Guide
│   └── test/                               # Suítes de Testes JUnit
│       └── java/com/ucsal/pokesal/
│           ├── features/batalha/           # Testes de terreno, matriz elemental e cálculo de dano
│           └── features/treinador/         # Testes de uso limite de itens
```

## 3. Plano de Implementação Integrado

### Etapa 0: Configuração de Ambiente e Repositório (Setup Inicial)
*   **Git & GitHub:** Inicializar o repositório garantindo que todos os membros tenham chaves SSH configuradas.
*   **Docker & Makefile:** Criar o `docker-compose.yml` subindo a imagem do `postgres` e do `sonarqube`. Configurar um `Makefile` para automatizar tarefas.
*   **Declaração de IA:** Criar imediatamente o arquivo `AI_DECLARATION.md` e um `.txt` anexando os prompts (limite máximo de 10% de uso exclusivo para correções ortográficas e sintáticas).

### Fase 01: Análise, Modelagem e Código Limpo
*   **Análise Estática de Requisitos:** Conduzir e documentar a inspeção dos requisitos fornecidos.
*   **Requisitos Autorais (Ação Humana Obrigatória):** A equipe deve se reunir (e registrar em ata assinada) para definir 3 novos requisitos completamente autorais. **(Proibido o uso de IA)**.
*   **Modelagem UML:** Desenhar Diagramas de Casos de Uso e de Classes.
*   **Desenvolvimento Vertical do Domínio (Clean Code):**
    *   Implementar as entidades base e seus atributos.
    *   Configurar o Checkstyle no projeto aplicando o *Google Java Style Guide* (nomes corretos, Javadoc, sem Magic Numbers).
*   **Entrega da Fase 1:** Relatório de contribuição individual e preparação para a arguição com o professor.

### Fase 02: Qualidade, Testes Unitários e Análise Sonar
*   **Integração do PostgreSQL (ORM):** Finalizar a conexão da camada de infraestrutura usando a biblioteca ORM escolhida.
*   **Desenvolvimento de Testes Unitários (JUnit 5):**
    *   Validar multiplicadores de dano da Matriz Elemental.
    *   Validar Efeitos de Terreno.
    *   Validar a ordem de ataque (SPD).
    *   Testar a exceção do limite de itens de mochila.
    *   Validar *Boundary Values* de HP, ATK e DEF.
    *   **Criar no mínimo 2 testes unitários para os requisitos autorais criados na Fase 01.**
*   **Checklist de Teste Estático (Manual):** Preencher o checklist exigido documentando a saúde básica do código.
*   **Análise de Qualidade de Código (SonarQube):** Executar o SonarQube e extrair o relatório (Code Smells, Bugs, Vulnerabilities, Coverage %).
*   **Entrega Final:** Documento de testes, documentação de bugs encontrados/corrigidos, relatório individual da Fase 2, atas das reuniões e preparação para a arguição final.