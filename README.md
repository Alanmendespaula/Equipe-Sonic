# ⚔️ PokéSal Simulator

Simulador de batalha de criaturas por turnos desenvolvido em **Java 17** com **Spring Boot**, **PostgreSQL** e **Docker**, seguindo os princípios de **Clean Architecture** e **Domain-Driven Design (DDD)** estruturado em **Vertical Slices**.

Projeto acadêmico desenvolvido para a **Universidade Católica do Salvador (UCSAL)**.

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:
* **Java JDK 17** ou superior
* **Docker** e **Docker Compose**
* **Make** *(opcional, mas recomendado para executar atalhos)*

---

## 🚀 Como Rodar o Projeto (Passo a Passo)

### 1. Iniciar a Infraestrutura (PostgreSQL & SonarQube)
Inicie os contêineres Docker em segundo plano:
```bash
make up
```
*(Alternativa manual sem make: `docker compose up -d`)*

---

### 2. Popular o Banco de Dados (Seed Inicial)
Carregue os dados iniciais contendo **12 PokéSals** e **4 Treinadores**:
```bash
make seed
```
*(Alternativa manual sem make:*
```bash
docker cp seed.sql pokesal-postgres:/tmp/seed.sql
docker exec pokesal-postgres psql -U postgres -d pokesal_db -f /tmp/seed.sql
```
*)*

---

### 3. Executar o Simulador no Terminal
Inicie a aplicação interativa via linha de comando (CLI):
```bash
make run
```
*(Alternativa manual no Windows:*
```powershell
.\mvnw.cmd spring-boot:run
```
*No Linux/macOS: `./mvnw spring-boot:run`)*

---

## 🧪 Comandos de Testes e Qualidade

O projeto conta com automações no [Makefile](Makefile) para auditoria e qualidade de código:

| Comando | Descrição | Equivalente Maven |
| :--- | :--- | :--- |
| `make test` | Executa os **24 testes unitários** (JUnit 5) com relatório JaCoCo | `.\mvnw.cmd test` |
| `make check` | Valida as regras de código limpo com **Checkstyle (Google Style)** | `.\mvnw.cmd checkstyle:check` |
| `make sonar` | Envia as métricas de cobertura e qualidade ao **SonarQube** | `.\mvnw.cmd clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000` |
| `make status` | Exibe o status de saúde dos contêineres Docker | `docker compose ps` |
| `make down` | Para e remove os contêineres | `docker compose down` |

> 🌐 **SonarQube Dashboard:** Acesse via navegador em `http://localhost:9000` após rodar `make up`.

---

## 🌟 Funcionalidades e Regras de Negócio

* **Matriz Elemental:** Vantagens e desvantagens entre tipos (Fogo, Planta, Água, Elétrico e Normal).
* **Efeitos de Terreno / Arena:** Vulcânico (+20% Fogo), Floresta (+20% Planta), Aquático (+20% Água) e Elétrico (+20% Elétrico).
* **Mochila e Itens:** Limite estrito de no máximo 2 itens por treinador (Poção e Super Poção).
* **Velocidade de Combate (SPD):** O PokéSal mais rápido tem prioridade de ataque no turno.
* **3 Requisitos Autorais Exclusivos:**
  1. ⚡ **Golpe Crítico Dinâmico:** Chance de desferir dano massivo (+50%) baseada na velocidade relativa.
  2. 🔥 **Habilidade Passiva Adrenalina/Fúria:** Ativada quando HP $\le 30\%$, aumentando o ataque em +30%.
  3. 💰 **Economia do Treinador e Loja:** Premiação de PokéMoedas por vitória e loja integrada no Centro Pokémon.
