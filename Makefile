.PHONY: help up down status restart build run seed test check sonar clean

# Variáveis
MVN = ./mvnw
ifeq ($(OS),Windows_NT)
    # Se mvnw.cmd existir, usa ele caso mvnw falhe no Windows cmd puro
    MVN_CMD = mvnw.cmd
else
    MVN_CMD = ./mvnw
endif

help:
	@echo "=================================================="
	@echo "          PokéSal Simulator - Makefile           "
	@echo "=================================================="
	@echo " Comandos disponíveis:"
	@echo "   make up       - Inicia contêineres Docker (PostgreSQL & SonarQube)"
	@echo "   make down     - Para e remove os contêineres Docker"
	@echo "   make status   - Exibe o status dos contêineres"
	@echo "   make restart  - Reinicia os contêineres"
	@echo "   make build    - Compila e empacota o projeto (Maven package)"
	@echo "   make run      - Executa a aplicação Spring Boot"
	@echo "   make seed     - Popula o banco com 12 PokéSals e 4 Treinadores (seed.sql)"
	@echo "   make test     - Executa todos os testes unitários (JUnit 5)"
	@echo "   make check    - Executa a análise estática com Checkstyle"
	@echo "   make sonar    - Executa a análise de qualidade no SonarQube"
	@echo "   make clean    - Limpa artefatos de compilação"
	@echo "=================================================="

up:
	docker compose up -d

down:
	docker compose down

status:
	docker compose ps

restart: down up

build:
	$(MVN_CMD) clean package -DskipTests

run:
	$(MVN_CMD) spring-boot:run

seed:
	docker cp seed.sql pokesal-postgres:/tmp/seed.sql
	docker exec pokesal-postgres psql -U postgres -d pokesal_db -f /tmp/seed.sql

test:
	$(MVN_CMD) test

check:
	$(MVN_CMD) checkstyle:check

sonar:
	$(MVN_CMD) clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000

clean:
	$(MVN_CMD) clean
